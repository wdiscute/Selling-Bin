package com.wdiscute.sellingbin.bin;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SellingBinBlockedSlot extends Slot
{

    public SellingBinBlockedSlot(Container container, int slot)
    {
        super(container, slot, 3990, 0);
    }

    @Override
    public boolean mayPickup(Player player)
    {
        return false;
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return false;
    }
}
