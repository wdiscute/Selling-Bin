package com.wdiscute.sellingbin.bin;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SellingBinItemSlot extends Slot
{
    SellingBinMenu menu;
    boolean isServer;

    public SellingBinItemSlot(SellingBinMenu menu, Container container, int slot, int x, int y, boolean isServer)
    {
        super(container, slot, x, y);
        this.menu = menu;
        this.isServer = isServer;
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        int value = Currency.calculateValueFromSingleStack(stack);
        return value > 0;
    }
}
