package com.wdiscute.sellingbin.bin;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wdiscute.sellingbin.SellingBin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SellingBinScreen extends AbstractContainerScreen<SellingBinMenu>
{
    private static final ResourceLocation TEXTURE = SellingBin.rl("textures/gui/selling_bin/selling_bin_background.png");
    private static final ResourceLocation CARD = SellingBin.rl("textures/gui/selling_bin/card_slot.png");

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

        //System.out.println("clicked relative x: " + x);
        //System.out.println("clicked relative y: " + y);


        //sell / sell all
        if (x > 80 && x < 121 && y > 10 && y < 24 && !menu.be.instaSell)
        {
            mousePressed = true;
            if (menu.getSlot(0).getItem().isEmpty())
                Minecraft.getInstance().player.playSound(SoundEvents.DISPENSER_FAIL, 0.7f, SellingBin.r.nextFloat() / 8 + 1f);

            if (Screen.hasShiftDown())
                //sell all
                Minecraft.getInstance().gameMode.handleInventoryButtonClick(this.menu.containerId, 68);
            else
                //sell
                Minecraft.getInstance().gameMode.handleInventoryButtonClick(this.menu.containerId, 67);
        }

        //toggle currency
        if (x > 126 && x < 137 && y > 40 && y < 51)
        {
            Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.7f, 1f);
            Minecraft.getInstance().gameMode.handleInventoryButtonClick(this.menu.containerId, 70);
        }

        //toggle sound
        if (x > 140 && x < 151 && y > 40 && y < 51)
        {
            Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.7f, 1f);
            Minecraft.getInstance().gameMode.handleInventoryButtonClick(this.menu.containerId, 71);
        }

        //toggle insta-sell
        if (x > 56 && x < 70 && y > 10 && y < 24)
        {
            Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.7f, 1f);
            Minecraft.getInstance().gameMode.handleInventoryButtonClick(this.menu.containerId, 69);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        double x = mouseX - uiX;
        double y = mouseY - uiY;


        int progressAvailable = menu.be.getProgressAvailable();

        //arrow tooltip
        if (x > 79 && x < 96 && y > 37 && y < 54)
            guiGraphics.renderTooltip(this.font, Currency.getListOfCurrenciesFromValue(menu.be.currencies, progressAvailable, true), Optional.empty(), mouseX, mouseY);

        //render arrow
        //scales [0, SELLING_BIN_LOWEST_VALUE]   ->   [0, 16]
        Currency currency = menu.be.currencySelected;
        if (currency.isNone()) currency = menu.be.currencies.getFirst();
        int arrow = (int) ((((float) progressAvailable) / ((float) currency.value())) * 16);
        guiGraphics.blit(TEXTURE, uiX + 80, uiY + 37, 192, 16, Math.clamp(arrow, 0, 16), 16, 256, 256);

        //insta sell pressed
        if (menu.be.instaSell)
            guiGraphics.blit(TEXTURE, uiX + 80, uiY + 11, 192, 128, 42, 13, 256, 256);

        //sell button pressed down
        if (mousePressed)
            guiGraphics.blit(TEXTURE, uiX + 80, uiY + 11, 192, 128, 42, 13, 256, 256);

        //sell / sell all
        MutableComponent sellComp;
        if (Screen.hasShiftDown())
            sellComp = Component.translatable("gui.selling_bin.sell_all");
        else
            sellComp = Component.translatable("gui.selling_bin.sell");

        //sell text
        renderCenteredString(guiGraphics, this.font, sellComp, uiX + 101, uiY + 14, 0x87583a, false);

        //sell outline when hovering
        if (x > 80 && x < 121 && y > 10 && y < 24 && !menu.be.instaSell)
        {
            guiGraphics.blit(TEXTURE, uiX + 79, uiY + 10, 192, 80, 48, 16, 256, 256);
            renderCenteredFatString(guiGraphics, this.font, sellComp, uiX + 101, uiY + 14, 0x87583a);
        }

        //insta sell outline when hovering
        if (x > 56 && x < 70 && y > 10 && y < 24)
            guiGraphics.blit(TEXTURE, uiX + 56, uiY + 10, 192, 96, 16, 16, 256, 256);


        //insta sell checkmark
        if (menu.be.instaSell)
            guiGraphics.blit(TEXTURE, uiX + 56, uiY + 9, 208, 16, 16, 16, 256, 256);

        //auto sell tooltip
        if (x > 58 && x < 69 && y > 12 && y < 23)
        {
            if (menu.be.instaSell)
                guiGraphics.blit(TEXTURE, uiX + 55, uiY + 10, 192, 112, 18, 16, 256, 256);

            guiGraphics.renderTooltip(this.font, Component.translatable("gui.selling_bin.auto_sell"), mouseX, mouseY);
        }

        //sound tooltip
        if (x > 140 && x < 151 && y > 40 && y < 51)
            guiGraphics.renderTooltip(this.font, Component.translatable("gui.selling_bin.sell_sound"), mouseX, mouseY);

        //render currency selected
        ItemStack currencyStack = new ItemStack(menu.be.currencySelected.item());
        if (currencyStack.isEmpty())
            guiGraphics.blit(TEXTURE, uiX + 128, uiY + 42, 192, 144, 10, 10, 256, 256);
        else
            renderItem(currencyStack, uiX + 124, uiY + 38, 0.5f);

        //render sound
        ItemStack bell = new ItemStack(Items.BELL);
        renderItem(bell, uiX + 138, uiY + 38, 0.5f);
        if (!menu.be.sound)
            renderItem(new ItemStack(Items.BARRIER), uiX + 138, uiY + 38, 0.5f);

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
            guiGraphics.renderTooltip(this.font, components, Optional.empty(), mouseX, mouseY);
        }

        //numismatic card compat
        if (numismatics)
        {
            int nix = uiX + 29;
            int niy = uiY + 37;

            if (hoveredSlot != null && !hoveredSlot.hasItem() && mouseX > nix && mouseX < nix + 18 && mouseY > niy && mouseY < niy + 18)
                guiGraphics.renderTooltip(font, Component.translatable("gui.selling_bin.card_slot"), mouseX, mouseY);

        }

    }

    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY)
    {
        int nix = uiX + 29;
        int niy = uiY + 37;
        guiGraphics.blit(TEXTURE, uiX, uiY, 0, 0, this.imageWidth, this.imageHeight);
        if (numismatics)
            guiGraphics.blit(CARD, nix, niy, 18, 18, 0, 0, 18, 18, 18, 18);
    }

    public SellingBinScreen(SellingBinMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title);
        ++this.imageHeight;
        numismatics = ModList.get().isLoaded("numismatics");
    }

    private void renderItem(ItemStack stack, int x, int y, float scale)
    {

        Level level = Minecraft.getInstance().level;
        LivingEntity entity = Minecraft.getInstance().player;

        if (!stack.isEmpty())
        {
            BakedModel bakedmodel = this.minecraft.getItemRenderer().getModel(stack, level, entity, 234234);

            PoseStack pose = new PoseStack();

            pose.pushPose();
            pose.translate((float) (x + 8), (float) (y + 8), (float) (150));

            pose.scale(16F * scale, -16F * scale, 16F * scale);
            boolean usesBlockLight = !bakedmodel.usesBlockLight();
            if (usesBlockLight)
            {
                Lighting.setupForFlatItems();
            }

            this.minecraft.getItemRenderer().render(
                    stack, ItemDisplayContext.GUI, false, pose, Minecraft.getInstance().renderBuffers().bufferSource(),
                    15728880, OverlayTexture.NO_OVERLAY, bakedmodel);

            //flush()
            RenderSystem.disableDepthTest();
            Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
            RenderSystem.enableDepthTest();

            if (usesBlockLight)
            {
                Lighting.setupFor3DItems();
            }

            pose.popPose();
        }
    }


    public void renderCenteredString(GuiGraphics guiGraphics, Font font, Component text, int x, int y, int color, boolean shadow)
    {
        FormattedCharSequence formattedcharsequence = text.getVisualOrderText();
        guiGraphics.drawString(font, formattedcharsequence, x - font.width(formattedcharsequence) / 2, y, color, shadow);
    }

    public void renderCenteredFatString(GuiGraphics guiGraphics, Font font, Component c, int x, int y, int color)
    {
        renderCenteredString(guiGraphics, this.font, c, x + 1, y, 0xffffff, false);
        renderCenteredString(guiGraphics, this.font, c, x - 1, y, 0xffffff, false);
        renderCenteredString(guiGraphics, this.font, c, x, y + 1, 0xffffff, false);
        renderCenteredString(guiGraphics, this.font, c, x, y - 1, 0xffffff, false);
        renderCenteredString(guiGraphics, this.font, c, x, y, color, false);
    }
}
