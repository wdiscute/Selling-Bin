package com.wdiscute.sellingbin.emi;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.bin.Currency;
import com.wdiscute.sellingbin.registry.SBBlocks;
import com.wdiscute.sellingbin.registry.SBDataMaps;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

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
        Currency.getCurrencies().forEach((currency) ->
        {
            if (currency.equals(Currency.EMPTY)) return;
            currencies.add(EmiIngredient.of(Ingredient.of(currency.item())));
            registry.addRecipe(new SellingBinCurrencyEmiRecipe(currency));
        });

        if (currencies.isEmpty() || currencies.get(0).isEmpty())
            registry.addRecipe(new SellingBinCurrencyEmptyEmiRecipe());


        //add all item with selling bin value
        Map<ResourceKey<Item>, SBDataMaps.ItemValue> sellables = BuiltInRegistries.ITEM.getDataMap(SBDataMaps.SELLING_BIN_VALUE);
        sellables.forEach((rk, itemValue) ->
        {
            if (!itemValue.equals(SBDataMaps.ItemValue.EMPTY))
            {

                Item item = BuiltInRegistries.ITEM.get(rk);
                sellable.add(EmiIngredient.of(Ingredient.of(item)));
                registry.addRecipe(new SellingBinSellingEmiRecipe(item, itemValue));
            }

        });

        if (sellable.isEmpty())
            registry.addRecipe(new SellingBinSellingEmptyEmiRecipe());


    }
}
