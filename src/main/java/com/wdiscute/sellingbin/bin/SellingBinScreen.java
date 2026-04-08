package com.wdiscute.sellingbin.bin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wdiscute.sellingbin.SellingBin;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.Font;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SellingBinScreen extends HandledScreen<SellingBinMenu>
{
    private static final Identifier TEXTURE = SellingBin.rl("textures/gui/selling_bin/selling_bin_background.png");
    private static final Identifier CARD = SellingBin.rl("textures/gui/selling_bin/card_slot.png");

    private int uiX = 0;
    private int uiY = 0;
    int backgroundHeight = 176;
    private static boolean numismatics = false;

    private boolean mousePressed;

    @Override
    protected void init()
    {
        super.init();
        backgroundHeight = 176;
        uiX = (this.width - this.backgroundWidth) / 2;
        uiY = (this.height - this.backgroundHeight) / 2;
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
        if (x > 80 && x < 121 && y > 10 && y < 24 && !handler.be.instaSell)
        {
            mousePressed = true;
            if (handler.getSlot(0).getStack().isEmpty())
                MinecraftClient.getInstance().player.playSound(SoundEvents.BLOCK_DISPENSER_FAIL, 0.7f, SellingBin.r.nextFloat() / 8 + 1f);

            if (Screen.hasShiftDown())
                //sell all
                MinecraftClient.getInstance().interactionManager.clickButton(this.handler.syncId, 68);
            else
                //sell
                MinecraftClient.getInstance().interactionManager.clickButton(this.handler.syncId, 67);
        }

        //toggle currency
        if (x > 126 && x < 137 && y > 40 && y < 51)
        {
            MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.7f, 1f);
            MinecraftClient.getInstance().interactionManager.clickButton(this.handler.syncId, 70);
        }

        //toggle sound
        if (x > 140 && x < 151 && y > 40 && y < 51)
        {
            MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.7f, 1f);
            MinecraftClient.getInstance().interactionManager.clickButton(this.handler.syncId, 71);
        }

        //toggle insta-sell
        if (x > 56 && x < 70 && y > 10 && y < 24)
        {
            MinecraftClient.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.7f, 1f);
            MinecraftClient.getInstance().interactionManager.clickButton(this.handler.syncId, 69);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY)
    {
    }

    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.drawMouseoverTooltip(guiGraphics, mouseX, mouseY);

        double x = mouseX - uiX;
        double y = mouseY - uiY;


        int progressAvailable = handler.be.getProgressAvailable();

        //arrow tooltip
        if (x > 79 && x < 96 && y > 37 && y < 54)
            guiGraphics.drawTooltip(this.textRenderer, Currency.getListOfCurrenciesFromValue(handler.be.currencies, progressAvailable, true), Optional.empty(), mouseX, mouseY);

        //render arrow
        //scales [0, SELLING_BIN_LOWEST_VALUE]   ->   [0, 16]
        Currency currency = handler.be.currencySelected;
        if (currency.isNone()) currency = handler.be.currencies.getFirst();
        int arrow = (int) ((((float) progressAvailable) / ((float) currency.value())) * 16);
        guiGraphics.drawTexture(TEXTURE, uiX + 80, uiY + 37, 192, 16, Math.clamp(arrow, 0, 16), 16, 256, 256);

        //insta sell pressed
        if (handler.be.instaSell)
            guiGraphics.drawTexture(TEXTURE, uiX + 80, uiY + 11, 192, 128, 42, 13, 256, 256);

        //sell button pressed down
        if (mousePressed)
            guiGraphics.drawTexture(TEXTURE, uiX + 80, uiY + 11, 192, 128, 42, 13, 256, 256);

        //sell / sell all
        MutableText sellComp;
        if (Screen.hasShiftDown())
            sellComp = Text.translatable("gui.selling_bin.sell_all");
        else
            sellComp = Text.translatable("gui.selling_bin.sell");

        //sell text
        renderCenteredString(guiGraphics, this.textRenderer, sellComp, uiX + 101, uiY + 14, 0x87583a, false);

        //sell outline when hovering
        if (x > 80 && x < 121 && y > 10 && y < 24 && !handler.be.instaSell)
        {
            guiGraphics.drawTexture(TEXTURE, uiX + 79, uiY + 10, 192, 80, 48, 16, 256, 256);
            renderCenteredFatString(guiGraphics, this.textRenderer, sellComp, uiX + 101, uiY + 14, 0x87583a);
        }

        //insta sell outline when hovering
        if (x > 56 && x < 70 && y > 10 && y < 24)
            guiGraphics.drawTexture(TEXTURE, uiX + 56, uiY + 10, 192, 96, 16, 16, 256, 256);


        //insta sell checkmark
        if (handler.be.instaSell)
            guiGraphics.drawTexture(TEXTURE, uiX + 56, uiY + 9, 208, 16, 16, 16, 256, 256);

        //auto sell tooltip
        if (x > 58 && x < 69 && y > 12 && y < 23)
        {
            if (handler.be.instaSell)
                guiGraphics.drawTexture(TEXTURE, uiX + 55, uiY + 10, 192, 112, 18, 16, 256, 256);

            guiGraphics.drawTooltip(this.textRenderer, Text.translatable("gui.selling_bin.auto_sell"), mouseX, mouseY);
        }

        //sound tooltip
        if (x > 140 && x < 151 && y > 40 && y < 51)
            guiGraphics.drawTooltip(this.textRenderer, Text.translatable("gui.selling_bin.sell_sound"), mouseX, mouseY);

        //render currency selected
        ItemStack currencyStack = new ItemStack(handler.be.currencySelected.item());
        if (currencyStack.isEmpty())
            guiGraphics.drawTexture(TEXTURE, uiX + 128, uiY + 42, 192, 144, 10, 10, 256, 256);
        else
            renderItem(currencyStack, uiX + 124, uiY + 38, 0.5f);

        //render sound
        ItemStack bell = new ItemStack(Items.BELL);
        renderItem(bell, uiX + 138, uiY + 38, 0.5f);
        if (!handler.be.sound)
            renderItem(new ItemStack(Items.BARRIER), uiX + 138, uiY + 38, 0.5f);

        //currency selected tooltip
        if (x > 126 && x < 137 && y > 40 && y < 51)
        {
            List<Text> components = new ArrayList<>();
            components.add(Text.translatable("gui.selling_bin.currency_selected"));
            if (handler.be.currencySelected.isNone())
                components.add(Text.translatable("gui.selling_bin.highest"));
            else
            {
                MutableText mutableComponent = Text.empty();
                mutableComponent.append(handler.be.currencySelected.item().getName());
                if (Screen.hasShiftDown())
                    mutableComponent.append(" (" + handler.be.currencySelected.value() + ")");

                components.add(mutableComponent);
            }
            guiGraphics.drawTooltip(this.textRenderer, components, Optional.empty(), mouseX, mouseY);
        }

        //numismatic card compat
        if (numismatics)
        {
            int nix = uiX + 29;
            int niy = uiY + 37;

            if (focusedSlot != null && !focusedSlot.hasStack() && mouseX > nix && mouseX < nix + 18 && mouseY > niy && mouseY < niy + 18)
                guiGraphics.drawTooltip(textRenderer, Text.translatable("gui.selling_bin.card_slot"), mouseX, mouseY);

        }
    }

    @Override
    protected void drawBackground(DrawContext guiGraphics, float delta, int mouseX, int mouseY)
    {
        int nix = uiX + 29;
        int niy = uiY + 37;
        guiGraphics.drawTexture(TEXTURE, uiX, uiY, 0, 0, this.backgroundWidth, this.backgroundHeight);
        if (numismatics)
            guiGraphics.drawTexture(CARD, nix, niy, 18, 18, 0, 0, 18, 18, 18, 18);
    }

    public SellingBinScreen(SellingBinMenu menu, PlayerInventory playerInventory, Text title)
    {
        super(menu, playerInventory, title);
        ++this.backgroundHeight;
        numismatics = FabricLoader.getInstance().isModLoaded("numismatics");
    }

    private void renderItem(ItemStack stack, int x, int y, float scale)
    {

        World level = MinecraftClient.getInstance().world;
        LivingEntity entity = MinecraftClient.getInstance().player;

        if (!stack.isEmpty())
        {
            BakedModel bakedmodel = this.client.getItemRenderer().getModel(stack, level, entity, 234234);

            MatrixStack pose = new MatrixStack();

            pose.push();
            pose.translate((float) (x + 8), (float) (y + 8), (float) (150));

            pose.scale(16F * scale, -16F * scale, 16F * scale);
            boolean usesBlockLight = !bakedmodel.isSideLit();
            if (usesBlockLight)
            {
                DiffuseLighting.disableGuiDepthLighting();
            }

            this.client.getItemRenderer().renderItem(
                    stack, ModelTransformationMode.GUI, false, pose, MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers(),
                    15728880, OverlayTexture.DEFAULT_UV, bakedmodel);

            //flush()
            RenderSystem.disableDepthTest();
            MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().draw();
            RenderSystem.enableDepthTest();

            if (usesBlockLight)
            {
                DiffuseLighting.enableGuiDepthLighting();
            }

            pose.pop();
        }
    }


    public void renderCenteredString(DrawContext guiGraphics, TextRenderer font, Text text, int x, int y, int color, boolean shadow)
    {
        OrderedText formattedcharsequence = text.asOrderedText();
        guiGraphics.drawText(font, formattedcharsequence, x - font.getWidth(formattedcharsequence) / 2, y, color, shadow);
    }

    public void renderCenteredFatString(DrawContext guiGraphics, TextRenderer font, Text c, int x, int y, int color)
    {
        renderCenteredString(guiGraphics, font, c, x + 1, y, 0xffffff, false);
        renderCenteredString(guiGraphics, font, c, x - 1, y, 0xffffff, false);
        renderCenteredString(guiGraphics, font, c, x, y + 1, 0xffffff, false);
        renderCenteredString(guiGraphics, font, c, x, y - 1, 0xffffff, false);
        renderCenteredString(guiGraphics, font, c, x, y, color, false);
    }
}
