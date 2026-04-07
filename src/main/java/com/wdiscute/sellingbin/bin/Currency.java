package com.wdiscute.sellingbin.bin;

import com.wdiscute.sellingbin.SBConfig;
import com.wdiscute.sellingbin.registry.SBDataMaps;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.nikdo53.datamapsfabric.extensions.IRegistryDataMapExtension;
import org.jetbrains.annotations.Nullable;

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
        Map<RegistryKey<Item>, Integer> dataMap = ((IRegistryDataMapExtension) Registries.ITEM).getDataMap(SBDataMaps.SELLING_BIN_CURRENCIES);

        Map<?, Map> dataMaps = ((IRegistryDataMapExtension) Registries.ITEM).getDataMaps();

        System.out.println(dataMaps.size());

        dataMaps.forEach((k, i) -> System.out.println("   -" + i.entrySet().stream().findFirst()));

        List<Currency> currenciesUnfiltered = new ArrayList<>();
        dataMap.forEach((i, v) -> currenciesUnfiltered.add(new Currency(Registries.ITEM.get(i), v)));

        //remove entries with negative value
        List<Currency> currencies = new ArrayList<>(currenciesUnfiltered.stream().filter(o -> o.value() > 0).toList());

        //if no entries remain, use default of just emeralds
        if(currencies.isEmpty()) return List.of(new Currency(Items.AIR, Integer.MAX_VALUE));

        //sort by lowest value
        currencies.sort(Comparator.comparingInt(Currency::value));
        return currencies;
    }

    //this does not take into account stack count!
    public static int calculateValueFromSingleStack(ItemStack is, BlockEntity blockEntity, @Nullable PlayerEntity player)
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

        return (int) (value * SBConfig.SELLING_BIN_MULTIPLIER);
    }

    public static int calculateValueFromSingleStack(ItemStack is, BlockEntity blockEntity)
    {
        return calculateValueFromSingleStack(is, blockEntity, null);
    }

    public static int calculateValueFromSingleStack(ItemStack is, PlayerEntity player)
    {
        return calculateValueFromSingleStack(is, null, player);
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
                    s = s + df.format(numOfCurrency) + " " + c.item().getName().getString();
                else
                    s = s + df.format(numOfCurrency) + " " + getPluralTranslation(c.item()).getString();

                found = true;
                break;
            }
        }

        if (!found)
        {
            float numOfCurrency = (float) value / currencies.getLast().value();
            DecimalFormat df = new DecimalFormat("#.##");

            if (numOfCurrency == 1)
                s = s + df.format(numOfCurrency) + " " + currencies.getLast().item().getName().getString();
            else
                s = s + df.format(numOfCurrency) + " " + getPluralTranslation(currencies.getLast().item()).getString();
        }

        return s;
    }

    public static List<Text> getListOfCurrenciesFromValue(List<Currency> currencies, int progressAvailable, boolean addCurrenciesText)
    {
        List<Text> comps = new ArrayList<>();

        if(addCurrenciesText)
            comps.add(Text.translatable("gui.selling_bin.selling_bin.currencies"));

        for (Currency c : currencies)
        {
            float numOfCurrency = (float) progressAvailable / c.value();
            DecimalFormat df = new DecimalFormat("#.##");

            String s = "";

            if (numOfCurrency == 1)
                s = s + df.format(numOfCurrency) + " " + c.item().getName().getString();
            else
                s = s + df.format(numOfCurrency) + " " + getPluralTranslation(c.item()).getString();

            comps.add(Text.literal(s));
        }

        return comps;
    }

    public static Text getPluralTranslation(Item item)
    {
        if(I18n.hasTranslation(item.getTranslationKey() + ".plural"))
            return Text.translatable(item.getTranslationKey() + ".plural");
        else
            return Text.translatable(item.getTranslationKey());
    }
}
