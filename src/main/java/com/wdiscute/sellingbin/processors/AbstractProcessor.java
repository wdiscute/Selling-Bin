package com.wdiscute.sellingbin.processors;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.wdiscute.sellingbin.registry.SBDataMaps;
import com.wdiscute.sellingbin.SellingBin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractProcessor
{
    public static final Codec<AbstractProcessor> ABSTRACT_PROCESSOR_CODEC = Identifier.CODEC
            .dispatch(processor -> processor.getRegistryHolderOrThrow().getId(),
                    loc ->
                    {
                        if(SellingBin.SELLING_BIN_REGISTRY.getOptional(loc).isPresent())
                        {
                            LogUtils.getLogger().error("Selling Bin Processor {} is not registered! " +
                                    "Make sure it's not dependent on another mod, and that you spelt the name correctly. " +
                                    "Using empty processor instead.", loc);
                            return EmptyProcessor.CODEC;
                        }
                        return SellingBin.SELLING_BIN_REGISTRY.getOptional(loc).get().getCodecOrThrow();
                    });

    public static final Codec<List<AbstractProcessor>> ABSTRACT_PROCESSOR_CODEC_LIST = ABSTRACT_PROCESSOR_CODEC.listOf();

    public abstract MapCodec<? extends AbstractProcessor> codec();

    public abstract DeferredHolder<AbstractProcessor, AbstractProcessor> getRegistryHolder();

    abstract public boolean showDescriptionOnEmi();

    abstract public List<Component> getDescription();

    public DeferredHolder<AbstractProcessor, AbstractProcessor> getRegistryHolderOrThrow()
    {
        var holder = getRegistryHolder();
        if (holder == null)
        {
            throw new IllegalStateException("Selling Bin Processor " + this + " does not have a registry holder!");
        }
        return holder;
    }

    public MapCodec<? extends AbstractProcessor> getCodecOrThrow()
    {
        var codec = codec();
        if (codec == null)
        {
            throw new IllegalStateException("Selling Bin Processor " + this + " does not have a codec!");
        }
        return codec;
    }


    abstract public int addValue(int baseValue, int currentValue, ItemStack itemStack, BlockEntity blockEntity, Player player);

    public boolean shouldCancelShrink(ItemStack itemStack)
    {
        return false;
    }

    public void onSellStart(ItemStack itemStack)
    {
    }

    public void onSellComplete(ItemStack itemStack)
    {
    }

    public static SBDataMaps.ItemValue createEmpty(int baseValue)
    {
        return new SBDataMaps.ItemValue(baseValue, List.of());
    }

    public static SBDataMaps.ItemValue createEmpty()
    {
        return new SBDataMaps.ItemValue(0, List.of());
    }

    public SBDataMaps.ItemValue create(int baseValue)
    {
        return new SBDataMaps.ItemValue(baseValue, List.of(this));
    }

    public boolean canSell(ItemStack itemStack, @Nullable BlockEntity blockEntity, @Nullable Player player)
    {
        return true;
    }
}
