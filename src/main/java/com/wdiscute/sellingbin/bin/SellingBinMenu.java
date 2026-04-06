package com.wdiscute.sellingbin.bin;

import com.wdiscute.sellingbin.registry.SBMenuTypes;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;

public class SellingBinMenu extends ScreenHandler
{
    private final Inventory container;
    public final SellingBinBlockEntity be;
    public static final int ITEM_SLOT = 0;
    public static final int RESULT_SLOT = 1;

    public SellingBinMenu(int containerId, PlayerInventory inv, BlockPos blockPos)
    {
        this(containerId, inv, new SimpleInventory(2), inv.player.getWorld().getBlockEntity(blockPos));
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id)
    {
        //sell
        if (id == 67)
        {
            be.sell(false);
        }

        //sell all
        if (id == 68)
        {
            be.sell(true);
        }

        //toggle insta sell
        if (id == 69)
        {
            be.instaSell = !be.instaSell;
            be.updateToClient();
        }

        //cycle currency
        if (id == 70)
        {
            be.cycleCurrency();
            be.updateToClient();
        }

        //toggle sound
        if (id == 71)
        {
            be.toggleSound();
            be.updateToClient();
        }

        return super.onButtonClick(player, id);
    }

    public SellingBinMenu(int containerId, PlayerInventory playerInventory, Inventory container, BlockEntity blockEntity)
    {
        super(SBMenuTypes.SELLING_BIN_MENU, containerId);
        checkSize(container, 2);
        this.be = (SellingBinBlockEntity) blockEntity;
        this.container = container;
        container.onOpen(playerInventory.player);

        this.addSlot(new SellingBinItemSlot(this, container, ITEM_SLOT, 56, 33, blockEntity != null));

        this.addSlot(new SellingBinResultSlot(this, container, RESULT_SLOT, 104, 33, blockEntity != null));


        for (int i1 = 0; i1 < 3; ++i1)
            for (int k1 = 0; k1 < 9; ++k1)
                this.addSlot(new Slot(playerInventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 83 + i1 * 18));

        for (int j1 = 0; j1 < 9; ++j1)
            this.addSlot(new Slot(playerInventory, j1, 8 + j1 * 18, 141));

    }

    @Override
    public boolean canUse(PlayerEntity player)
    {
        return this.container.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < this.container.size())
            {
                if (!this.insertItem(itemstack1, this.container.size(), this.slots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.insertItem(itemstack1, 0, this.container.size() - 1, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
                slot.setStack(ItemStack.EMPTY);
            else
                slot.markDirty();
        }

        be.update();
        return itemstack;
    }

    @Override
    public void onClosed(PlayerEntity player)
    {
        super.onClosed(player);
        this.container.onClose(player);
    }

}
