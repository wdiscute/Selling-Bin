package com.wdiscute.sellingbin.bin;

import com.wdiscute.sellingbin.registry.SBMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.ModList;

public class SellingBinMenu extends AbstractContainerMenu
{
    private final Container container;
    public final SellingBinBlockEntity be;
    public static final int ITEM_SLOT = 0;
    public static final int RESULT_SLOT = 1;
    public static final int CARD_SLOT = 2;

    public SellingBinMenu(int containerId, Inventory inv, FriendlyByteBuf extraData)
    {
        this(containerId, inv, new SimpleContainer(3), inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    @Override
    public boolean clickMenuButton(Player player, int id)
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

        return super.clickMenuButton(player, id);
    }

    public SellingBinMenu(int containerId, Inventory playerInventory, Container container, BlockEntity blockEntity)
    {
        super(SBMenuTypes.SELLING_BIN_MENU.get(), containerId);
        checkContainerSize(container, 3);
        this.be = (SellingBinBlockEntity) blockEntity;
        this.container = container;
        container.startOpen(playerInventory.player);

        this.addSlot(new SellingBinItemSlot(this, container, ITEM_SLOT, 56, 33, blockEntity != null));

        this.addSlot(new SellingBinResultSlot(this, container, RESULT_SLOT, 104, 33, blockEntity != null));

        if(ModList.get().isLoaded("numismatics"))
            this.addSlot(new SellingBinCardSlot(this, container, CARD_SLOT, 30, 33, blockEntity != null));


        for (int i1 = 0; i1 < 3; ++i1)
            for (int k1 = 0; k1 < 9; ++k1)
                this.addSlot(new Slot(playerInventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 83 + i1 * 18));

        for (int j1 = 0; j1 < 9; ++j1)
            this.addSlot(new Slot(playerInventory, j1, 8 + j1 * 18, 141));



    }

    public boolean stillValid(Player player)
    {
        return this.container.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem())
        {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.container.getContainerSize())
            {
                if (!this.moveItemStackTo(itemstack1, this.container.getContainerSize(), this.slots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(itemstack1, 0, this.container.getContainerSize() - 1, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
                slot.setByPlayer(ItemStack.EMPTY);
            else
                slot.setChanged();
        }

        be.update();
        return itemstack;
    }

    public void removed(Player player)
    {
        super.removed(player);
        this.container.stopOpen(player);
    }
}
