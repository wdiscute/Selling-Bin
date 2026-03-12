package com.wdiscute.sellingbin.bin;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SellingBinResultSlot extends Slot
{
    SellingBinMenu menu;
    boolean isServer;

    public SellingBinResultSlot(SellingBinMenu menu, Container container, int slot, int x, int y, boolean isServer)
    {
        super(container, slot, x, y);
        this.menu = menu;
        this.isServer = isServer;
    }

    @Override
    public boolean mayPickup(Player player)
    {
        if(isServer) menu.be.update();
        return true;
    }

    @Override
    public void onTake(Player player, ItemStack stack)
    {
        super.onTake(player, stack);
        if(isServer) menu.be.update();
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        if(isServer) menu.be.update();
        return false;
    }
}
