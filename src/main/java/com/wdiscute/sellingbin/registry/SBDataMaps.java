package com.wdiscute.sellingbin.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.sellingbin.processors.AbstractProcessor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SBDataMaps
{
    DataMap<ItemValue> SELLING_BIN_VALUE = new DataMap<>(Items.EMERALD.asItem(), ItemValue.EMPTY);
    DataMap<Integer> SELLING_BIN_CURRENCIES = new DataMap<>(Items.EMERALD.asItem(), 0);

    static <T> T getOrDefault(ItemStack stack, DataMap<T> dataMap, T d)
    {
        //T data = stack.getRegistryEntry().getData(dataMap);
        //if(data == null) return d;
        //return data;
        return d;
    }

     static <T> Map<RegistryKey<Item>, T> getAllItems(DataMap<T> sellingBinCurrencies)
    {
        Map<RegistryKey<Item>, T> wad = new HashMap<>();
        return wad;
    }

    record DataMap<T>(Item item, T data){}

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
