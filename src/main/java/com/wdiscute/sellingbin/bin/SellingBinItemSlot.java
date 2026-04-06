package com.wdiscute.sellingbin.bin;


import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class SellingBinItemSlot extends Slot
{
    SellingBinMenu menu;
    boolean isServer;

    public SellingBinItemSlot(SellingBinMenu menu, Inventory container, int slot, int x, int y, boolean isServer)
    {
        super(container, slot, x, y);
        this.menu = menu;
        this.isServer = isServer;
    }

    @Override
    public boolean canInsert(ItemStack stack)
    {
        int value = Currency.calculateValueFromSingleStack(stack, menu.be);
        return value > 0;
    }
}
