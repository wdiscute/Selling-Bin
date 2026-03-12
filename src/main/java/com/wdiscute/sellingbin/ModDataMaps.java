package com.wdiscute.sellingbin;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.sellingbin.processors.AbstractProcessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import java.util.ArrayList;
import java.util.List;

public interface ModDataMaps
{
    DataMapType<Item, ItemValue> SELLING_BIN_VALUE = DataMapType.builder(
            SellingBin.rl("selling_bin_value"), Registries.ITEM, ItemValue.CODEC).build();

    DataMapType<Item, Integer> SELLING_BIN_CURRENCIES = DataMapType.builder(
            SellingBin.rl("selling_bin_currencies"), Registries.ITEM, Codec.INT).build();


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

        public static ItemValue empty()
        {
            return new ItemValue(0, List.of());
        }

        public static ItemValue empty(int baseValue)
        {
            return new ItemValue(baseValue, List.of());
        }

        public ItemValue add(AbstractProcessor processor)
        {
            List<AbstractProcessor> list = new ArrayList<>(this.processors);
            list.add(processor);
            return new ItemValue(0, list);
        }
    }
}
