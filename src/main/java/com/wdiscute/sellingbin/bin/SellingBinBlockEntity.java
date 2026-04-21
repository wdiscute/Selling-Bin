package com.wdiscute.sellingbin.bin;

import com.wdiscute.sellingbin.compat.NumismaticsCompat;
import com.wdiscute.sellingbin.registry.SBBlockEntities;
import com.wdiscute.sellingbin.registry.SBDataMaps;
import net.minecraft.core.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.fml.ModList;
import net.nikdo53.tinymultiblocklib.blockentities.AbstractMultiBlockEntity;

import javax.annotation.Nullable;
import java.util.List;

public class SellingBinBlockEntity extends AbstractMultiBlockEntity implements WorldlyContainer, MenuProvider
{

    private NonNullList<ItemStack> itemStacks;
    public int storedProgress;
    public boolean instaSell = false;
    public boolean sound = true;
    public Currency currencySelected = Currency.NONE;
    public List<Currency> currencies;
    public List<Currency> currenciesReversed;

    public SellingBinBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(SBBlockEntities.SELLING_BIN.get(), pos, blockState);
        this.itemStacks = NonNullList.withSize(2, ItemStack.EMPTY);
        this.currencies = Currency.getCurrencies();
        this.currenciesReversed = currencies.reversed();
    }


    void playSound(SoundEvent soundEvent)
    {
        if (!sound) return;
        BlockState state = level.getBlockState(getBlockPos());
        Vec3i vec3i = state.getValue(HorizontalDirectionalBlock.FACING).getUnitVec3i();
        double d0 = (double) this.worldPosition.getX() + 0.5 + (double) vec3i.getX() / 2.0;
        double d1 = (double) this.worldPosition.getY() + 0.5 + (double) vec3i.getY() / 2.0;
        double d2 = (double) this.worldPosition.getZ() + 0.5 + (double) vec3i.getZ() / 2.0;
        this.level.playSound(null, d0, d1, d2, soundEvent, SoundSource.BLOCKS, 0.5F, this.level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void startOpen(ContainerUser containerUser)
    {
        if (!level.isClientSide() && sound) playSound(SoundEvents.BARREL_OPEN);
        WorldlyContainer.super.startOpen(containerUser);
    }

    @Override
    public void stopOpen(ContainerUser containerUser)
    {
        if (!level.isClientSide() && sound) playSound(SoundEvents.BARREL_CLOSE);
        WorldlyContainer.super.stopOpen(containerUser);
    }

    public void sell(boolean all)
    {
        int value = Currency.calculateValueFromSingleStack(getItem(SellingBinMenu.ITEM_SLOT), this);
        SBDataMaps.ItemValue itemValue = SBDataMaps.getOrDefault(getItem(SellingBinMenu.ITEM_SLOT), SBDataMaps.SELLING_BIN_VALUE, SBDataMaps.ItemValue.EMPTY);
        if (value <= 0) return;

        boolean sold = false;

        while (getItem(SellingBinMenu.ITEM_SLOT).getCount() > 0)
        {
            itemValue.processors().forEach(o -> o.onSellStart(getItem(SellingBinMenu.ITEM_SLOT)));
            storedProgress += value;
            if (itemValue.processors().stream().noneMatch(o -> o.shouldCancelShrink(getItem(SellingBinMenu.ITEM_SLOT))))
                getItem(SellingBinMenu.ITEM_SLOT).shrink(1);

            sold = true;
            itemValue.processors().forEach(o -> o.onSellComplete(getItem(SellingBinMenu.ITEM_SLOT)));
            if (!all)
            {
                update();
                if (!level.isClientSide() && sound)
                    level.playSound(null, getBlockPos(), SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.BLOCKS, 0.2f, 1.3f);
                return;
            }
        }

        if (sold)
            if (!level.isClientSide() && sound)
                level.playSound(null, getBlockPos(), SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.BLOCKS, 0.2f, 1.3f);

        forceUpdate();
    }

    public int getProgressAvailable()
    {
        return storedProgress + SBDataMaps.getOrDefault(getItem(SellingBinMenu.RESULT_SLOT), SBDataMaps.SELLING_BIN_CURRENCIES, 0) * getItem(SellingBinMenu.RESULT_SLOT).getCount();
    }

    public void forceUpdate()
    {
        currencyCached = null;
        extraUpdate = true;
        update();
    }

    //updates result slot to the highest possible currency
    boolean extraUpdate = false;
    Currency currencyCached = null;
    int cachedOutputCount = 0;
    public void update()
    {
        if(level.isClientSide()) return;

        ItemStack result = getItem(SellingBinMenu.RESULT_SLOT);
        ItemStack card = getItem(SellingBinMenu.CARD_SLOT);

        //prevent unnecessary updates for better performance
        boolean shouldRun = false;
        if(currencySelected != currencyCached) shouldRun = true;
        if(extraUpdate) shouldRun = true;
        if(cachedOutputCount != result.getCount()) shouldRun = true;

        if(!shouldRun) return;
        currencyCached = currencySelected;
        cachedOutputCount = result.getCount();
        extraUpdate = false;

        int progressAvailable = getProgressAvailable();

        //if none run through all currencies to find the highest one
        if (currencySelected.isNone())
            for (Currency c : currenciesReversed)
            {
                if (progressAvailable > c.value())
                {
                    if (!result.is(c.item())) result = new ItemStack(c.item());

                    int count = Math.clamp(progressAvailable / c.value(), 0, c.item().getMaxStackSize(new ItemStack(c.item())));

                    result.setCount(count);

                    setItem(SellingBinMenu.RESULT_SLOT, result);

                    storedProgress = progressAvailable - c.value() * count;

                    break;
                }
            }
        else
        {
            //else run the math for the selected currency
            if (!result.is(currencySelected.item())) result = new ItemStack(currencySelected.item());

            int count = Math.clamp(progressAvailable / currencySelected.value(), 0, currencySelected.item().getMaxStackSize(new ItemStack(currencySelected.item())));

            result.setCount(count);

            setItem(SellingBinMenu.RESULT_SLOT, result);

            storedProgress = progressAvailable - currencySelected.value() * count;
        }

        if (ModList.get().isLoaded("numismatics"))
        {
            if(NumismaticsCompat.deposit(result, card))
            {
                setItem(SellingBinMenu.RESULT_SLOT, ItemStack.EMPTY);
                forceUpdate();
            }
        }

        updateToClient();
    }

    @Nullable
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player)
    {
        if (!player.isSpectator())
            return new SellingBinMenu(containerId, playerInventory, this, this);
        else
            return null;
    }

    @Override
    protected void saveAdditional(ValueOutput output)
    {
        super.saveAdditional(output);

        if (!isCenter()) return;

        //insta sell
        output.putBoolean("insta_sell", instaSell);

        //sound
        output.putBoolean("sound", sound);

        //currency selected
        if (!currencySelected.isNone())
        {
            for (int i = 0; i < currencies.size(); i++)
                if (currencySelected.equals(currencies.get(i))) output.putInt("currency", i);
        }
        else
            output.putInt("currency", -1);

        //stored progress
        output.putInt("stored_progress", storedProgress);

        ContainerHelper.saveAllItems(output, this.itemStacks, false);
    }

    @Override
    protected void loadAdditional(ValueInput input)
    {
        super.loadAdditional(input);

        if (!isCenter()) return;

        //insta sell
        instaSell = input.getBooleanOr("insta_sell", false);

        //sound
        sound = input.getBooleanOr("sound", true);

        //currency

        if (input.getInt("currency").isPresent())
            if (input.getInt("currency").get() == -1) currencySelected = Currency.NONE;
            else if (currencies.size() > input.getInt("currency").get())
                currencySelected = currencies.get(input.getInt("currency").get());

        //stored progress
        if (input.getInt("stored_progress").isPresent()) storedProgress = input.getInt("stored_progress").get();

        //retrieve items (from ShulkerBoxBlockEntity)
        this.itemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.itemStacks);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    protected NonNullList<ItemStack> getItems()
    {
        return this.itemStacks;
    }

    @Override
    public int getContainerSize()
    {
        return 3;
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
    public ItemStack getItem(int slot)
    {
        return this.getItems().get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount)
    {
        ItemStack itemstack = ContainerHelper.removeItem(this.getItems(), slot, amount);
        if (!itemstack.isEmpty())
        {
            this.setChanged();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot)
    {
        return ContainerHelper.takeItem(this.getItems(), slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack)
    {
        this.getItems().set(slot, stack);
        stack.limitSize(this.getMaxStackSize(stack));
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player)
    {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent()
    {
        this.getItems().clear();
    }

    @Override
    public Component getDisplayName()
    {
        return Component.empty();
    }

    public void updateToClient()
    {
        setChanged();
        if (level instanceof ServerLevel serverLevel)
        {
            serverLevel.sendBlockUpdated(getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public int[] getSlotsForFace(Direction direction)
    {
        BlockState blockState = level.getBlockState(getBlockPos());
        //if (!blockState.is(ModBlocks.SELLING_BIN)) return new int[0];
        //if (!blockState.getValue(AbstractMultiBlock.CENTER)) return new int[0];

        if (direction == Direction.DOWN) return new int[]{1};
        return new int[]{0};
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @org.jetbrains.annotations.Nullable Direction direction)
    {
        int value = Currency.calculateValueFromSingleStack(itemStack, this);
        return value > 0 && direction != Direction.DOWN;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction)
    {
        return direction == Direction.DOWN;
    }

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
}
