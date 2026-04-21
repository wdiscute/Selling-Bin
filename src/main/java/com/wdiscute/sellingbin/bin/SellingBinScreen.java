package com.wdiscute.sellingbin.bin;

import com.wdiscute.sellingbin.SellingBin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class SellingBinScreen extends AbstractContainerScreen<SellingBinMenu>
{
    private static final Identifier TEXTURE = SellingBin.rl("textures/gui/selling_bin/selling_bin_background.png");
    private static final Identifier CARD = SellingBin.rl("textures/gui/selling_bin/card_slot.png");

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
    public boolean mouseReleased(MouseButtonEvent event)
    {
        mousePressed = false;
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick)
    {
        double x = event.x() - uiX;
        double y = event.y() - uiY;

        //System.out.println("clicked relative x: " + x);
        //System.out.println("clicked relative y: " + y);


        //sell / sell all
        if (x > 80 && x < 121 && y > 10 && y < 24 && !menu.be.instaSell)
        {
            mousePressed = true;
            if (menu.be.getItem(0).isEmpty())
                Minecraft.getInstance().player.playSound(SoundEvents.DISPENSER_FAIL, 0.7f, SellingBin.r.nextFloat() / 8 + 1f);

            if (Minecraft.getInstance().hasShiftDown())
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

        return super.mouseClicked(event, doubleClick);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym)
    {
    }

    public void renderTooltip(GuiGraphicsExtractor guiGraphics, Component component, int mouseX, int mouseY)
    {
        renderTooltip(guiGraphics, List.of(component), mouseX, mouseY);
    }

    public void renderTooltip(GuiGraphicsExtractor guiGraphics, List<Component> components, int mouseX, int mouseY)
    {
        var clientTooltipComponents = components.stream().map(Component::getVisualOrderText).map(ClientTooltipComponent::create).toList();

        Identifier identifier = ItemStack.EMPTY.get(DataComponents.TOOLTIP_STYLE);

        guiGraphics.tooltip(this.font,
                clientTooltipComponents,
                mouseX,
                mouseY,
                DefaultTooltipPositioner.INSTANCE,
                identifier
        );
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);

        double x = mouseX - uiX;
        double y = mouseY - uiY;


        int progressAvailable = menu.be.getProgressAvailable();

        //render arrow
        //scales [0, SELLING_BIN_LOWEST_VALUE]   ->   [0, 16]
        Currency currency = menu.be.currencySelected;
        if (currency.isNone()) currency = menu.be.currencies.getFirst();
        int arrow = (int) ((((float) progressAvailable) / ((float) currency.value())) * 16);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE,
                uiX + 80, uiY + 37,
                192, 16,
                Math.clamp(arrow, 0, 16), 16,
                256, 256);

        //insta sell pressed
        if (menu.be.instaSell)
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, uiX + 80, uiY + 11, 192, 128, 42, 13, 256, 256);

        //sell button pressed down
        if (mousePressed)
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, uiX + 80, uiY + 11, 192, 128, 42, 13, 256, 256);

        //sell / sell all
        MutableComponent sellComp;
        if (Minecraft.getInstance().hasShiftDown())
            sellComp = Component.translatable("gui.selling_bin.sell_all");
        else
            sellComp = Component.translatable("gui.selling_bin.sell");

        //sell text
        renderCenteredString(guiGraphics, this.font, sellComp, uiX + 101, uiY + 14, 0xff87583a, false);

        //sell outline when hovering
        if (x > 80 && x < 121 && y > 10 && y < 24 && !menu.be.instaSell)
        {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, uiX + 79, uiY + 10, 192, 80, 48, 16, 256, 256);
            renderCenteredFatString(guiGraphics, sellComp, uiX + 101, uiY + 14, 0xff87583a);
        }

        //insta sell outline when hovering
        if (x > 56 && x < 70 && y > 10 && y < 24)
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, uiX + 56, uiY + 10, 192, 96, 16, 16, 256, 256);


        //insta sell checkmark
        if (menu.be.instaSell)
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, uiX + 56, uiY + 9, 208, 16, 16, 16, 256, 256);

        //sound tooltip
        if (x > 140 && x < 151 && y > 40 && y < 51)
            renderTooltip(guiGraphics, Component.translatable("gui.selling_bin.sell_sound"), mouseX, mouseY);

        //render currency selected
        ItemStack currencyStack = new ItemStack(menu.be.currencySelected.item());
        if (currencyStack.isEmpty())
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, uiX + 128, uiY + 42, 192, 144, 10, 10, 256, 256);
        else
            renderItem(guiGraphics, currencyStack, uiX + 124, uiY + 38, 0.5f);

        //render sound
        ItemStack bell = new ItemStack(Items.BELL);
        renderItem(guiGraphics, bell, uiX + 138, uiY + 38, 0.5f);
        if (!menu.be.sound)
            renderItem(guiGraphics, new ItemStack(Items.BARRIER), uiX + 138, uiY + 38, 0.5f);

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
                mutableComponent.append(Component.translatable(menu.be.currencySelected.item().getDescriptionId()));
                if (Minecraft.getInstance().hasShiftDown())
                    mutableComponent.append(" (" + menu.be.currencySelected.value() + ")");

                components.add(mutableComponent);
            }
            renderTooltip(guiGraphics, components, mouseX, mouseY);
        }

        //auto sell tooltip
        if (x > 58 && x < 69 && y > 12 && y < 23)
        {
            if (menu.be.instaSell)
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, uiX + 55, uiY + 10, 192, 112, 18, 16, 256, 256);

            renderTooltip(guiGraphics, Component.translatable("gui.selling_bin.auto_sell"), mouseX, mouseY);
        }

        //arrow tooltip
        if (x > 79 && x < 96 && y > 37 && y < 54)
            renderTooltip(
                    guiGraphics,
                    Currency.getListOfCurrenciesFromValue(menu.be.currencies, progressAvailable, true),
                    mouseX,
                    mouseY);
    }

    private void renderItem(GuiGraphicsExtractor guiGraphics, ItemStack itemStack, int x, int y, float scale)
    {
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x + 4, y + 4);
        guiGraphics.pose().scale(0.5f);
        guiGraphics.item(itemStack, 0, 0);
        guiGraphics.pose().popMatrix();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a)
    {
        super.extractBackground(graphics, mouseX, mouseY, a);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, uiX, uiY, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }

    public SellingBinScreen(SellingBinMenu menu, Inventory playerInventory, Component title)
    {
        super(menu, playerInventory, title);
        ++this.imageHeight;
    }

    public void renderCenteredString(GuiGraphicsExtractor guiGraphics, Font font, Component text, int x, int y, int color, boolean shadow)
    {
        FormattedCharSequence formattedcharsequence = text.getVisualOrderText();
        guiGraphics.text(font, formattedcharsequence, x - font.width(formattedcharsequence) / 2, y, color, shadow);
    }

    public void renderCenteredFatString(GuiGraphicsExtractor guiGraphics, Component c, int x, int y, int color)
    {
        renderCenteredString(guiGraphics, this.font, c, x + 1, y, 0xffffffff, false);
        renderCenteredString(guiGraphics, this.font, c, x - 1, y, 0xffffffff, false);
        renderCenteredString(guiGraphics, this.font, c, x, y + 1, 0xffffffff, false);
        renderCenteredString(guiGraphics, this.font, c, x, y - 1, 0xffffffff, false);
        renderCenteredString(guiGraphics, this.font, c, x, y, color, false);
    }
}
