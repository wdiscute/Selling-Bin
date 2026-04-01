package com.wdiscute.sellingbin.bin;

import com.wdiscute.sellingbin.SBConfig;
import com.wdiscute.sellingbin.registry.SBDataMaps;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public record Currency(Item item, int value)
{
    public static final Currency NONE = new Currency(Items.AIR, 0);

    public boolean isNone(){return this.equals(NONE);}

    public static List<Currency> getCurrencies()
    {
        //add all currencies from datamap
        Map<ResourceKey<Item>, Integer> dataMap = BuiltInRegistries.ITEM.getDataMap(SBDataMaps.SELLING_BIN_CURRENCIES);
        List<Currency> currenciesUnfiltered = new ArrayList<>();
        dataMap.forEach((i, v) -> currenciesUnfiltered.add(new Currency(BuiltInRegistries.ITEM.getOptional(i).get(), v)));

        //remove entries with negative value
        List<Currency> currencies = new ArrayList<>(currenciesUnfiltered.stream().filter(o -> o.value() > 0).toList());

        //if no entries remain, use default of just emeralds
        if(currencies.isEmpty()) return List.of(new Currency(Items.AIR, Integer.MAX_VALUE));

        //sort by lowest value
        currencies.sort(Comparator.comparingInt(Currency::value));
        return currencies;
    }

    //this does not take into account stack count!
    public static int calculateValueFromSingleStack(ItemStack is, BlockEntity blockEntity, @Nullable Player player)
    {
        SBDataMaps.ItemValue itemValue = SBDataMaps.getOrDefault(is, SBDataMaps.SELLING_BIN_VALUE, SBDataMaps.ItemValue.EMPTY);

        //if one of the processors returns false on canSell()
        if(itemValue.processors().stream().anyMatch(o -> !o.canSell(is, blockEntity, player))) return 0;

        //calculate value
        int value = itemValue.baseValue();
        for (var p : itemValue.processors())
        {
            value += p.addValue(itemValue.baseValue(), value, is, blockEntity, player);
        }

        return (int) (value * SBConfig.SELLING_BIN_MULTIPLIER.get());
    }

    public static int calculateValueFromSingleStack(ItemStack is, BlockEntity blockEntity)
    {
        return calculateValueFromSingleStack(is, blockEntity, null);
    }

    public static int calculateValueFromSingleStack(ItemStack is, Player player)
    {
        return calculateValueFromSingleStack(is, null, player);
    }

    public static int calculateValueFromSingleStack(ItemStack is)
    {
        return calculateValueFromSingleStack(is, null, null);
    }

    //returns formatted string of highest possible currency for the value
    public static String getStringFromValue(int value)
    {
        List<Currency> currencies = Currency.getCurrencies().reversed();

        boolean found = false;

        String s = "";

        for (Currency c : currencies)
        {
            if (value > c.value())
            {
                float numOfCurrency = (float) value / c.value();

                DecimalFormat df = new DecimalFormat("#.##");

                if (numOfCurrency == 1)
                    s = s + df.format(numOfCurrency) + " " + c.item().getDescriptionId();
                else
                    s = s + df.format(numOfCurrency) + " " + getPluralTranslation(c.item()).getString(100);

                found = true;
                break;
            }
        }

        if (!found)
        {
            float numOfCurrency = (float) value / currencies.getLast().value();
            DecimalFormat df = new DecimalFormat("#.##");

            if (numOfCurrency == 1)
                s = s + df.format(numOfCurrency) + " " + currencies.getLast().item().getDescriptionId();
            else
                s = s + df.format(numOfCurrency) + " " + getPluralTranslation(currencies.getLast().item()).getString(100);
        }

        return s;
    }

    public static List<Component> getListOfCurrenciesFromValue(List<Currency> currencies, int progressAvailable, boolean addCurrenciesText)
    {
        List<Component> comps = new ArrayList<>();

        if(addCurrenciesText)
            comps.add(Component.translatable("gui.selling_bin.selling_bin.currencies"));

        for (Currency c : currencies)
        {
            float numOfCurrency = (float) progressAvailable / c.value();
            DecimalFormat df = new DecimalFormat("#.##");

            String s = "";

            if (numOfCurrency == 1)
                s = s + df.format(numOfCurrency) + " " + c.item().getDescriptionId();
            else
                s = s + df.format(numOfCurrency) + " " + getPluralTranslation(c.item()).getString(100);

            comps.add(Component.literal(s));
        }

        return comps;
    }

    public static Component getPluralTranslation(Item item)
    {
        if(I18n.exists(item.getDescriptionId() + ".plural"))
            return Component.translatable(item.getDescriptionId() + ".plural");
        else
            return Component.translatable(item.getDescriptionId());
    }
}
