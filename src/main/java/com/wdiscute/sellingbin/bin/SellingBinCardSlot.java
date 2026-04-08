package com.wdiscute.sellingbin.bin;

import com.wdiscute.sellingbin.SellingBin;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.slot.Slot;

public class SellingBinCardSlot extends Slot
{
    SellingBinMenu menu;
    boolean isServer;

    public SellingBinCardSlot(SellingBinMenu menu, Inventory container, int slot, int x, int y, boolean isServer)
    {
        super(container, slot, x, y);
        this.menu = menu;
        this.isServer = isServer;
    }

    @Override
    public void markDirty()
    {
        super.markDirty();
        menu.be.forceUpdate();
    }


    @Override
    public boolean canInsert(ItemStack stack)
    {
        return stack.isIn(TagKey.of(RegistryKeys.ITEM, SellingBin.rl("numismatics", "cards")));
    }
}
