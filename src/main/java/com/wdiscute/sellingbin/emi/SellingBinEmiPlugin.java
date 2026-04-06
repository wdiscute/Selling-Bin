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
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EmiEntrypoint
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
        Map<RegistryKey<Item>, Integer> currencies = SBDataMaps.getAllItems(SBDataMaps.SELLING_BIN_CURRENCIES);
        currencies.forEach((rk, value) ->
        {
            if (value != 0)
            {
                Item item = Registries.ITEM.get(rk);
                SellingBinEmiPlugin.currencies.add(EmiIngredient.of(Ingredient.ofItems(item)));
                registry.addRecipe(new SellingBinCurrencyEmiRecipe(item, value));
            }
        });

        if(currencies.isEmpty())
            registry.addRecipe(new SellingBinCurrencyEmptyEmiRecipe());


        //add all item with selling bin value
        Map<RegistryKey<Item>, SBDataMaps.ItemValue> sellables = SBDataMaps.getAllItems(SBDataMaps.SELLING_BIN_VALUE);
        sellables.forEach((rk, itemValue) ->
        {
            if (!itemValue.equals(SBDataMaps.ItemValue.EMPTY))
            {

                Item item = Registries.ITEM.get(rk);
                sellable.add(EmiIngredient.of(Ingredient.ofItems(item)));
                registry.addRecipe(new SellingBinSellingEmiRecipe(item, itemValue));
            }

        });

        if(sellables.isEmpty())
            registry.addRecipe(new SellingBinSellingEmptyEmiRecipe());


    }
}
