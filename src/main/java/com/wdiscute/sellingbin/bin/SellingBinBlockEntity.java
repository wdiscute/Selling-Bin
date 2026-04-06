package com.wdiscute.sellingbin.bin;

import com.wdiscute.sellingbin.registry.SBBlockEntities;
import com.wdiscute.sellingbin.registry.SBDataMaps;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.nikdo53.tinymultiblocklib.blockentities.AbstractMultiBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SellingBinBlockEntity extends AbstractMultiBlockEntity implements SidedInventory, ExtendedScreenHandlerFactory<BlockPos>, TickableBlockEntity
{

    private DefaultedList<ItemStack> itemStacks;
    public int storedProgress;
    public boolean instaSell = false;
    public boolean sound = true;
    public Currency currencySelected = Currency.NONE;
    public List<Currency> currencies;
    public List<Currency> currenciesReversed;

    public SellingBinBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(SBBlockEntities.SELLING_BIN, pos, blockState);
        this.itemStacks = DefaultedList.ofSize(2, ItemStack.EMPTY);
        this.currencies = Currency.getCurrencies();
        this.currenciesReversed = currencies.reversed();
    }



    void playSound(SoundEvent soundEvent)
    {
        if(!sound) return;
        BlockState state = world.getBlockState(getPos());
        Vec3i vec3i = state.get(HorizontalFacingBlock.FACING).getVector();
        double d0 = (double) this.getPos().getX() + 0.5 + (double) vec3i.getX() / 2.0;
        double d1 = (double) this.getPos().getY() + 0.5 + (double) vec3i.getY() / 2.0;
        double d2 = (double) this.getPos().getZ() + 0.5 + (double) vec3i.getZ() / 2.0;
        this.world.playSound(null, d0, d1, d2, soundEvent, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
    }


    @Override
    public void onOpen(PlayerEntity player)
    {
        if (!world.isClient && sound) playSound(SoundEvents.BLOCK_BARREL_OPEN);
        SidedInventory.super.onOpen(player);
    }

    @Override
    public void onClose(PlayerEntity player)
    {
        if (!world.isClient && sound) playSound(SoundEvents.BLOCK_BARREL_CLOSE);
        SidedInventory.super.onClose(player);
    }


    public void sell(boolean all)
    {
        int value = Currency.calculateValueFromSingleStack(getStack(SellingBinMenu.ITEM_SLOT), this);
        SBDataMaps.ItemValue itemValue = SBDataMaps.getOrDefault(getStack(SellingBinMenu.ITEM_SLOT), SBDataMaps.SELLING_BIN_VALUE, SBDataMaps.ItemValue.EMPTY);
        if (value <= 0) return;

        boolean sold = false;

        while (getStack(SellingBinMenu.ITEM_SLOT).getCount() > 0)
        {
            itemValue.processors().forEach(o -> o.onSellStart(getStack(SellingBinMenu.ITEM_SLOT)));
            storedProgress += value;
            if (itemValue.processors().stream().noneMatch(o -> o.shouldCancelShrink(getStack(SellingBinMenu.ITEM_SLOT))))
                getStack(SellingBinMenu.ITEM_SLOT).decrement(1);

            sold = true;
            itemValue.processors().forEach(o -> o.onSellComplete(getStack(SellingBinMenu.ITEM_SLOT)));
            if (!all)
            {
                update();
                if (!world.isClient && sound)
                    world.playSound(null, getPos(), SoundEvents.BLOCK_NOTE_BLOCK_BELL.value(), SoundCategory.BLOCKS, 0.2f, 1.3f);
                return;
            }
        }

        if (sold)
            if (!world.isClient && sound)
                world.playSound(null, getPos(), SoundEvents.BLOCK_NOTE_BLOCK_BELL.value(), SoundCategory.BLOCKS, 0.2f, 1.3f);

        update();
    }

    public int getProgressAvailable()
    {
        return storedProgress + SBDataMaps.getOrDefault(getStack(SellingBinMenu.RESULT_SLOT), SBDataMaps.SELLING_BIN_CURRENCIES, 0) * getStack(SellingBinMenu.RESULT_SLOT).getCount();
    }

    //updates result slot to the highest possible currency
    public void update()
    {
        ItemStack is = getStack(SellingBinMenu.RESULT_SLOT);

        int progressAvailable = getProgressAvailable();

        //if none run through all currencies to find the highest one
        if (currencySelected.isNone())
            for (Currency c : currenciesReversed)
            {
                if (progressAvailable > c.value())
                {
                    if (!is.isOf(c.item())) is = new ItemStack(c.item());

                    int count = Math.clamp(progressAvailable / c.value(), 0, c.item().getMaxCount());

                    is.setCount(count);

                    setStack(SellingBinMenu.RESULT_SLOT, is);

                    storedProgress = progressAvailable - c.value() * count;

                    break;
                }
            }
        else
        {
            //else run the math for the selected currency
            if (!is.isOf(currencySelected.item())) is = new ItemStack(currencySelected.item());

            int count = Math.clamp(progressAvailable / currencySelected.value(), 0, currencySelected.item().getMaxCount());

            is.setCount(count);

            setStack(SellingBinMenu.RESULT_SLOT, is);

            storedProgress = progressAvailable - currencySelected.value() * count;
        }


        updateToClient();
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player)
    {
        if (!player.isSpectator())
            return new SellingBinMenu(syncId, playerInventory, this, this);
        else
            return null;
    }


    @Override
    protected void writeNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registries)
    {
        super.writeNbt(tag, registries);

        if (!isCenter()) return;

        //insta sell
        tag.putBoolean("insta_sell", instaSell);

        //sound
        tag.putBoolean("sound", sound);


        //currency selected
        if (!currencySelected.isNone())
        {
            for (int i = 0; i < currencies.size(); i++)
                if (currencySelected.equals(currencies.get(i))) tag.putInt("currency", i);
        }
        else
            tag.putInt("currency", -1);

        //stored progress
        tag.putInt("stored_progress", storedProgress);

        //save items (from ShulkerBoxBlockEntity)
        Inventories.writeNbt(tag, this.itemStacks, false, registries);
    }

    @Override
    protected void readNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registries)
    {
        super.readNbt(tag, registries);
        if (!isCenter()) return;

        //insta sell
        if (tag.contains("insta_sell")) instaSell = tag.getBoolean("insta_sell");

        //sound
        if (tag.contains("sound")) sound = tag.getBoolean("sound");

        //currency
        if (tag.contains("currency"))
            if (tag.getInt("currency") == -1) currencySelected = Currency.NONE;
            else if (currencies.size() > tag.getInt("currency"))
                currencySelected = currencies.get(tag.getInt("currency"));

        //stored progress
        if (tag.contains("stored_progress")) storedProgress = tag.getInt("stored_progress");

        //retrieve items (from ShulkerBoxBlockEntity)
        this.itemStacks = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (tag.contains("Items", 9))
        {
            Inventories.readNbt(tag, this.itemStacks, registries);
        }
    }

    @Override
    public @NotNull NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries)
    {
        NbtCompound tag = new NbtCompound();
        readNbt(tag, registries);
        return tag;
    }


    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket()
    {
        return BlockEntityUpdateS2CPacket.create(this);
    }


    //container methods copied from ShulkerBoxBlockEntity and it's extends
    protected DefaultedList<ItemStack> getItems()
    {
        return this.itemStacks;
    }

    @Override
    public int size()
    {
        return 2;
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.getItems())
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getStack(int slot)
    {
        return this.getItems().get(slot);
    }


    @Override
    public ItemStack removeStack(int slot, int amount)
    {
        ItemStack itemstack = Inventories.splitStack(this.getItems(), slot, amount);
        if (!itemstack.isEmpty())
        {
            this.markDirty();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeStack(int slot)
    {
        return Inventories.removeStack(this.getItems(), slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack)
    {
        this.getItems().set(slot, stack);
        stack.capCount(getMaxCount(stack));
        this.markDirty();
    }


    @Override
    public boolean canPlayerUse(PlayerEntity player)
    {
        return Inventory.canPlayerUse(this, player);
    }

    @Override
    public void clear()
    {
        this.getItems().clear();
    }


    @Override
    public Text getDisplayName()
    {
        return Text.empty();
    }

    public void updateToClient()
    {
        markDirty();
        if (world instanceof ServerWorld serverLevel)
        {
            serverLevel.updateListeners(getPos(), this.getCachedState(), this.getCachedState(), 3);
        }
    }

    @Override
    public int[] getAvailableSlots(Direction direction)
    {
        BlockState blockState = world.getBlockState(getPos());
        //if (!blockState.is(ModBlocks.SELLING_BIN)) return new int[0];
        //if (!blockState.getValue(AbstractMultiBlock.CENTER)) return new int[0];

        if (direction == Direction.DOWN) return new int[]{1};
        return new int[]{0};
    }

    @Override
    public boolean canInsert(int slot, ItemStack itemStack, @Nullable Direction direction)
    {
        int value = Currency.calculateValueFromSingleStack(itemStack, this);
        return value > 0 && direction != Direction.DOWN;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction direction)
    {
        return direction == Direction.DOWN;
    }

    @Override
    public void tick()
    {
        update();
        if (instaSell) sell(true);
    }

    public void cycleCurrency()
    {
        if (currencySelected.isNone())
        {
            currencySelected = currencies.getFirst();
            update();
            return;
        }


        for (int i = 0; i < currencies.size() - 1; i++)
        {
            if (currencies.get(i).equals(currencySelected))
            {
                currencySelected = currencies.get(i + 1);
                update();
                return;
            }

        }


        currencySelected = Currency.NONE;
        update();
        updateToClient();
    }

    public void toggleSound()
    {
        sound = !sound;
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player)
    {
        return pos;
    }
}
