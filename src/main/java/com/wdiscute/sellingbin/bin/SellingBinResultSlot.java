package com.wdiscute.sellingbin.bin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class SellingBinResultSlot extends Slot
{
    SellingBinMenu menu;
    boolean isServer;

    public SellingBinResultSlot(SellingBinMenu menu, Inventory container, int slot, int x, int y, boolean isServer)
    {
        super(container, slot, x, y);
        this.menu = menu;
        this.isServer = isServer;
    }

    @Override
    public boolean canTakeItems(PlayerEntity player)
    {
        if(isServer) menu.be.update();
        return true;
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack)
    {
        super.onTakeItem(player, stack);
        if(isServer) menu.be.update();
    }

    @Override
    public boolean canInsert(ItemStack stack)
    {
        if(isServer) menu.be.update();
        return false;
    }
}
