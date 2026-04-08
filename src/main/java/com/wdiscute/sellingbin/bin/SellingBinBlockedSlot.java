package com.wdiscute.sellingbin.bin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class SellingBinBlockedSlot extends Slot
{

    public SellingBinBlockedSlot(Inventory container, int slot)
    {
        super(container, slot, 3990, 0);
    }

    @Override
    public boolean canInsert(ItemStack stack)
    {
        return false;
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity)
    {
        return false;
    }
}
