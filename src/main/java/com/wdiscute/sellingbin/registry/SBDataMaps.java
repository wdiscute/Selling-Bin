package com.wdiscute.sellingbin.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.processors.AbstractProcessor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.world.WorldAccess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SBDataMaps
{
    static ItemValue getItemValueOrDefault(ItemStack stack, WorldAccess worldAccess, ItemValue d)
    {
        if (!stack.isEmpty())
        {
            System.out.println("not air");
        }

        Map<Item, ItemValue> allSellableItems = getAllSellableItems(worldAccess);

        return allSellableItems.getOrDefault(stack.getItem(), d);
    }

    static int getCurrencyOrDefault(ItemStack stack, WorldAccess worldAccess, int d)
    {
        Map<Item, Integer> allSellableItems = getAllCurrencies(worldAccess);

        return allSellableItems.getOrDefault(stack.getItem(), d);
    }

    static Map<Item, ItemValue> getAllSellableItems(WorldAccess worldAccess)
    {
        SellingBinValueDataMap sellables = worldAccess.getRegistryManager().get(SellingBin.SELLABLES).get(SellingBin.rl("selling_bin_currencies"));

        if (sellables == null) return new HashMap<>();

        Map<Item, ItemValue> map = new HashMap<>();

        sellables.values.forEach((list, itemValue) -> list.forEach(d -> map.put(d.value(), itemValue)));

        return map;
    }

    static Map<Item, Integer> getAllCurrencies(WorldAccess worldAccess)
    {
        SellingBinCurrencyDataMap currencies = worldAccess.getRegistryManager().get(SellingBin.CURRENCIES).get(SellingBin.rl("selling_bin_currencies"));

        if (currencies == null) return new HashMap<>();

        Map<Item, Integer> map = new HashMap<>();

        currencies.values.forEach((list, currencyValue) -> list.forEach(d -> map.put(d.value(), currencyValue)));

        return map;
    }

    record SellingBinValueDataMap(Map<RegistryEntryList<Item>, ItemValue> values)
    {

        public static final Codec<SellingBinValueDataMap> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.unboundedMap(RegistryCodecs.entryList(RegistryKeys.ITEM), ItemValue.CODEC).fieldOf("values").forGetter(SellingBinValueDataMap::values)
                ).apply(instance, SellingBinValueDataMap::new)
        );
    }

    record SellingBinCurrencyDataMap(Map<RegistryEntryList<Item>, Integer> values)
    {

        public static final Codec<SellingBinCurrencyDataMap> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.unboundedMap(RegistryCodecs.entryList(RegistryKeys.ITEM), Codec.INT).fieldOf("values").forGetter(SellingBinCurrencyDataMap::values)
                ).apply(instance, SellingBinCurrencyDataMap::new)
        );
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
