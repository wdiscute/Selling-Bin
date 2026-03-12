package com.wdiscute.sellingbin.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.wdiscute.sellingbin.ModDataMaps;
import com.wdiscute.sellingbin.SellingBin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

public abstract class AbstractProcessor
{
    public static final Codec<AbstractProcessor> ABSTRACT_PROCESSOR_CODEC = ResourceLocation.CODEC
            .dispatch(processor -> processor.getRegistryHolderOrThrow().getId(),
                    loc -> SellingBin.SELLING_BIN_REGISTRY.get(loc).getCodecOrThrow());

    public static final Codec<List<AbstractProcessor>> ABSTRACT_PROCESSOR_CODEC_LIST = ABSTRACT_PROCESSOR_CODEC.listOf();

    public abstract MapCodec<? extends AbstractProcessor> codec();

    public abstract DeferredHolder<AbstractProcessor, AbstractProcessor> getRegistryHolder();

    public DeferredHolder<AbstractProcessor, AbstractProcessor> getRegistryHolderOrThrow(){
        var holder = getRegistryHolder();
        if (holder == null){
            throw new IllegalStateException("Selling Bin Processor " + this + " does not have a registry holder!");
        }
        return holder;
    }

    public MapCodec<? extends AbstractProcessor> getCodecOrThrow(){
        var codec = codec();
        if (codec == null){
            throw new IllegalStateException("Selling Bin Processor " + this + " does not have a codec!");
        }
        return codec;
    }



    abstract public int addValue(int baseValue, int currentValue, ItemStack itemStack);

    public boolean shouldCancelShrink(ItemStack itemStack)
    {
        return false;
    }

    public void onSellStart(ItemStack itemStack){}

    public void onSellComplete(ItemStack itemStack){}

    public ModDataMaps.ItemValue create(int baseValue)
    {
        return new ModDataMaps.ItemValue(baseValue, List.of(this));
    }
}
