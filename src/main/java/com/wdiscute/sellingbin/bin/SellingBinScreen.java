package com.wdiscute.sellingbin.bin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.utils.ScreenUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public class SellingBinScreen extends AbstractContainerScreen<SellingBinMenu>
{
    private static final ScreenUtils.Image TEXTURE = new ScreenUtils.Image(SellingBin.rl("textures/gui/selling_bin/selling_bin_background.png"), 256, 256);
    private static final ScreenUtils.Image CARD = new ScreenUtils.Image(SellingBin.rl("textures/gui/selling_bin/card_slot.png"), 18, 18);

    private int uiX = 0;
    private int uiY = 0;
    int imageHeight = 176;
    private static boolean numismatics = false;

    private boolean mousePressed;

    @Override
    protected void init()
    {
        super.init();
        imageHeight = 176;
        uiX = (this.width - this.imageWidth) / 2;
        uiY = (this.height - this.imageHeight) / 2;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
        mousePressed = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        double x = mouseX - uiX;
        double y = mouseY - uiY;

        //sell / sell all
        LocalPlayer player = Minecraft.getInstance().player;
        MultiPlayerGameMode gameMode = Minecraft.getInstance().gameMode;
        if (x > 80 && x < 121 && y > 10 && y < 24 && !menu.be.instaSell)
        {
            mousePressed = true;
            if (menu.getSlot(0).getItem().isEmpty())
                player.playSound(SoundEvents.DISPENSER_FAIL, 0.7f, SellingBin.r.nextFloat() / 8 + 1f);

            if (Screen.hasShiftDown())
                //sell all
                gameMode.handleInventoryButtonClick(this.menu.containerId, 68);
            else
                //sell
                gameMode.handleInventoryButtonClick(this.menu.containerId, 67);
        }

        //toggle currency
        if (x > 126 && x < 137 && y > 40 && y < 51)
        {
            player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.7f, 1f);
            gameMode.handleInventoryButtonClick(this.menu.containerId, 70);
        }

        //toggle sound
        if (x > 140 && x < 151 && y > 40 && y < 51)
        {
            player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.7f, 1f);
            gameMode.handleInventoryButtonClick(this.menu.containerId, 71);
        }

        //toggle insta-sell
        if (x > 56 && x < 70 && y > 10 && y < 24)
        {
            player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.7f, 1f);
            gameMode.handleInventoryButtonClick(this.menu.containerId, 69);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick)
    {
        super.render(g, mouseX, mouseY, partialTick);
        this.renderTooltip(g, mouseX, mouseY);

        double x = mouseX - uiX;
        double y = mouseY - uiY;
        PoseStack pose = g.pose();

        int progressAvailable = menu.be.getProgressAvailable();

        //arrow tooltip
        if (x > 79 && x < 96 && y > 37 && y < 54)
            ScreenUtils.Tooltip.set(Currency.getListOfCurrenciesFromValue(menu.be.currencies, progressAvailable, true));

        //render arrow
        //scales [0, SELLING_BIN_LOWEST_VALUE]   ->   [0, 16]
        Currency currency = menu.be.currencySelected;
        if (currency.isNone()) currency = menu.be.currencies.getFirst();
        int arrow = (int) ((((float) progressAvailable) / ((float) currency.value())) * 16);
        TEXTURE.render(g, uiX + 80, uiY + 37, 192, 16, Math.clamp(arrow, 0, 16), 16);

        //insta sell pressed
        if (menu.be.instaSell)
            TEXTURE.render(g, uiX + 80, uiY + 11, 192, 128, 42, 13);

        //sell button pressed down
        if (mousePressed)
            TEXTURE.render(g, uiX + 80, uiY + 11, 192, 128, 42, 13);

        //sell / sell all
        MutableComponent sellComp;
        if (Screen.hasShiftDown())
            sellComp = Component.translatable("gui.selling_bin.sell_all");
        else
            sellComp = Component.translatable("gui.selling_bin.sell");

        //sell text
        ScreenUtils.centeredText(g, this.font, sellComp, uiX + 101, uiY + 14, 0xff87583a, false);

        //sell outline when hovering
        if (x > 80 && x < 121 && y > 10 && y < 24 && !menu.be.instaSell)
        {
            TEXTURE.render(g, uiX + 79, uiY + 10, 192, 80, 48, 16);
            ScreenUtils.centeredText(g, this.font, sellComp, uiX + 101 + 1, uiY + 14, 0xffffffff, false);
            ScreenUtils.centeredText(g, this.font, sellComp, uiX + 101 - 1, uiY + 14, 0xffffffff, false);
            ScreenUtils.centeredText(g, this.font, sellComp, uiX + 101, uiY + 14 + 1, 0xffffffff, false);
            ScreenUtils.centeredText(g, this.font, sellComp, uiX + 101, uiY + 14 - 1, 0xffffffff, false);
            ScreenUtils.centeredText(g, this.font, sellComp, uiX + 101, uiY + 14, 0xff87583a, false);
        }

        //insta sell outline when hovering
        if (x > 56 && x < 70 && y > 10 && y < 24)
            TEXTURE.render(g, uiX + 56, uiY + 10, 192, 96, 16, 16);

        //insta sell checkmark
        if (menu.be.instaSell)
            TEXTURE.render(g, uiX + 56, uiY + 9, 208, 16, 16, 16);

        //auto sell tooltip
        if (x > 58 && x < 69 && y > 12 && y < 23)
        {
            if (menu.be.instaSell)
                TEXTURE.render(g, uiX + 55, uiY + 10, 192, 112, 18, 16);

            ScreenUtils.Tooltip.set(Component.translatable("gui.selling_bin.auto_sell"));
        }

        //sound tooltip
        if (x > 140 && x < 151 && y > 40 && y < 51)
            ScreenUtils.Tooltip.set(Component.translatable("gui.selling_bin.sell_sound"));

        //render currency selected
        ItemStack currencyStack = new ItemStack(menu.be.currencySelected.item());
        if (currencyStack.isEmpty())
            TEXTURE.render(g, uiX + 128, uiY + 42, 192, 144, 10, 10);
        else
            ScreenUtils.item(g, currencyStack, uiX + 136, uiY + 50, pose, 0.5f);

        //render sound
        ItemStack bell = new ItemStack(Items.BELL);
        ScreenUtils.item(g, bell, uiX + 150, uiY + 50, pose, 0.5f);
        if (!menu.be.sound)
            ScreenUtils.item(g, new ItemStack(Items.BARRIER), uiX + 150, uiY + 50, pose, 0.5f);

        //currency selected tooltip
        if (x > 126 && x < 137 && y > 40 && y < 51)
        {
            List<Component> components = new ArrayList<>();
            components.add(Component.translatable("gui.selling_bin.currency_selected"));
            if (menu.be.currencySelected.isNone())
                components.add(Component.translatable("gui.selling_bin.highest"));
            else
            {
                MutableComponent mutableComponent = Component.empty();
                mutableComponent.append(menu.be.currencySelected.item().getDescription());
                if (Screen.hasShiftDown())
                    mutableComponent.append(" (" + menu.be.currencySelected.value() + ")");

                components.add(mutableComponent);
            }
            ScreenUtils.Tooltip.set(components);
        }

        //numismatic card compat
        if (numismatics)
        {
            int nix = uiX + 29;
            int niy = uiY + 37;

            if (hoveredSlot != null && !hoveredSlot.hasItem() && mouseX > nix && mouseX < nix + 18 && mouseY > niy && mouseY < niy + 18)
                ScreenUtils.Tooltip.set(Component.translatable("gui.selling_bin.card_slot"));
        }

        ScreenUtils.Tooltip.render(g, font, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
    }


    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY)
    {
        int nix = uiX + 29;
        int niy = uiY + 37;
        TEXTURE.render(g, uiX, uiY, 0, 0, this.imageWidth, this.imageHeight);
        if (numismatics)
            CARD.render(g, nix, niy);
    }

    public SellingBinScreen(SellingBinMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title);
        ++this.imageHeight;
        numismatics = ModList.get().isLoaded("numismatics");
    }
}
