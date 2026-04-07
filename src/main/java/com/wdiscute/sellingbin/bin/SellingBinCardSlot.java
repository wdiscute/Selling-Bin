package com.wdiscute.sellingbin.bin;

import com.wdiscute.sellingbin.SellingBin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SellingBinCardSlot extends Slot
{
    SellingBinMenu menu;
    boolean isServer;

    public SellingBinCardSlot(SellingBinMenu menu, Container container, int slot, int x, int y, boolean isServer)
    {
        super(container, slot, x, y);
        this.menu = menu;
        this.isServer = isServer;
    }

    @Override
    public void setChanged()
    {
        super.setChanged();
        menu.be.forceUpdate();
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return stack.is(TagKey.create(Registries.ITEM, SellingBin.rl("numismatics", "cards")));
    }
}
