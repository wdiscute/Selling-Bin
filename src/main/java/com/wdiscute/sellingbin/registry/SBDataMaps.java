package com.wdiscute.sellingbin.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.processors.AbstractProcessor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.WorldAccess;
import net.nikdo53.datamapsfabric.datamaps.DataMapType;
import net.nikdo53.datamapsfabric.datamaps.DataMapsManager;
import net.nikdo53.datamapsfabric.extensions.IDataMapHolderExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SBDataMaps
{
    DataMapType<Item, ItemValue> SELLING_BIN_VALUE = DataMapsManager.register(DataMapType.builder(
            SellingBin.rl("selling_bin_value"), RegistryKeys.ITEM, ItemValue.CODEC).synced(ItemValue.CODEC, true).build());

    DataMapType<Item, Integer> SELLING_BIN_CURRENCIES = DataMapsManager.register(DataMapType.builder(
            SellingBin.rl("selling_bin_currencies"), RegistryKeys.ITEM, Codec.INT).synced(Codec.INT, true).build());


    static <T> T getOrDefault(ItemStack stack, DataMapType<Item, T> dataMap, T d)
    {
        T data = (T) ((IDataMapHolderExtension) stack.getRegistryEntry()).getData(dataMap);
        if (data != null)
            System.out.println(data);
        if (data == null) return d;
        return data;
    }

    static void init()
    {
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
