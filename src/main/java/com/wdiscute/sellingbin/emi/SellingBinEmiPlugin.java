package com.wdiscute.sellingbin.emi;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.registry.SBBlocks;
import com.wdiscute.sellingbin.registry.SBDataMaps;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.nikdo53.datamapsfabric.datamaps.DataMapType;
import net.nikdo53.datamapsfabric.extensions.IRegistryDataMapExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SellingBinEmiPlugin implements EmiPlugin
{
    public static final EmiStack MY_WORKSTATION = EmiStack.of(SBBlocks.SELLING_BIN);
    public static final EmiRecipeCategory SELLING_BIN_SELLING_CATEGORY
            = new EmiRecipeCategory(
            SellingBin.rl("selling_bin_selling"),
            MY_WORKSTATION);

    public static final EmiRecipeCategory SELLING_BIN_CURRENCIES_CATEGORY
            = new EmiRecipeCategory(
            SellingBin.rl("selling_bin_currencies"),
            MY_WORKSTATION);

    public static final EmiTexture ARROW = new EmiTexture(SellingBin.rl("textures/gui/selling_bin/selling_bin_background.png"),
            192, 16, 16, 16, 16, 16, 256, 256);

    public static List<EmiIngredient> currencies = new ArrayList<>();
    public static List<EmiIngredient> sellable = new ArrayList<>();

    @Override
    public void register(EmiRegistry registry)
    {
        currencies.clear();
        sellable.clear();

        // Tell EMI to add a tab for your category
        registry.addCategory(SELLING_BIN_SELLING_CATEGORY);
        registry.addCategory(SELLING_BIN_CURRENCIES_CATEGORY);

        // Add all the workstations your category uses
        registry.addWorkstation(SELLING_BIN_SELLING_CATEGORY, MY_WORKSTATION);
        registry.addWorkstation(SELLING_BIN_CURRENCIES_CATEGORY, MY_WORKSTATION);


        //add all currencies
        Map<RegistryKey<Item>, Integer> currencies = ((IRegistryDataMapExtension) Registries.ITEM).getDataMap(SBDataMaps.SELLING_BIN_CURRENCIES);
        currencies.forEach((item, value) ->
        {
            if (value != 0)
            {
                SellingBinEmiPlugin.currencies.add(EmiIngredient.of(Ingredient.ofItems(Registries.ITEM.get(item))));
                registry.addRecipe(new SellingBinCurrencyEmiRecipe(Registries.ITEM.get(item), value));
            }
        });

        if(currencies.isEmpty())
            registry.addRecipe(new SellingBinCurrencyEmptyEmiRecipe());


        //add all item with selling bin value
        Map<RegistryKey<Item>, SBDataMaps.ItemValue> sellables = ((IRegistryDataMapExtension) Registries.ITEM).getDataMap(SBDataMaps.SELLING_BIN_VALUE);
        sellables.forEach((item, itemValue) ->
        {
            if (!itemValue.equals(SBDataMaps.ItemValue.EMPTY))
            {
                sellable.add(EmiIngredient.of(Ingredient.ofItems(Registries.ITEM.get(item))));
                registry.addRecipe(new SellingBinSellingEmiRecipe(Registries.ITEM.get(item), itemValue));
            }

        });

        if(sellables.isEmpty())
            registry.addRecipe(new SellingBinSellingEmptyEmiRecipe());


    }
}
