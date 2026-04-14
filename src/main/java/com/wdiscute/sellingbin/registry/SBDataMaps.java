package com.wdiscute.sellingbin.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.processors.AbstractProcessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.nikdo53.neobackports.datamaps.DataMapType;

import java.util.List;

public interface SBDataMaps
{
    DataMapType<Item, ItemValue> SELLING_BIN_VALUE = DataMapType.builder(
            SellingBin.rl("selling_bin_value"), Registries.ITEM, ItemValue.CODEC).synced(ItemValue.CODEC, true).build();

    DataMapType<Item, Integer> SELLING_BIN_CURRENCIES = DataMapType.builder(
            SellingBin.rl("selling_bin_currencies"), Registries.ITEM, Codec.INT).synced(Codec.INT, true).build();


    static <T> T getOrDefault(ItemStack stack, DataMapType<Item, T> dataMap, T d)
    {
        T data = stack.getItemHolder().getData(dataMap);
        if(data == null) return d;
        return data;
    }

    record ItemValue(int baseValue, List<AbstractProcessor> processors)
    {
        public static final Codec<ItemValue> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.INT.fieldOf("base_value").forGetter(ItemValue::baseValue),
                        AbstractProcessor.ABSTRACT_PROCESSOR_CODEC_LIST.fieldOf("processors").forGetter(ItemValue::processors)
                ).apply(instance, ItemValue::new));

        public static final ItemValue EMPTY = new ItemValue(0, List.of());
    }
}
