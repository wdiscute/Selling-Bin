package com.wdiscute.sellingbin.emi;

import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;

public class HoverTextWidget extends Widget
{
    protected final int x, y, sizeX, sizeY;
    private final List<Text> components;

    public HoverTextWidget(int x, int y, int sizeX, int sizeY, List<Text> hoverText)
    {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.components = hoverText;
    }

    @Override
    public Bounds getBounds()
    {
        return new Bounds(x, y, 18, 18);
    }



    @Override
    public void render(DrawContext guiGraphics, int mouseX, int mouseY, float delta)
    {
        if (mouseX > x && mouseX < x + sizeX && mouseY > y && mouseY < y + sizeY)
            guiGraphics.drawTooltip(MinecraftClient.getInstance().textRenderer, components, Optional.empty(), mouseX, mouseY);
    }
}
