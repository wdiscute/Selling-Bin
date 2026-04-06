package com.wdiscute.sellingbin.jei;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.bin.Currency;
import com.wdiscute.sellingbin.processors.AbstractProcessor;
import com.wdiscute.sellingbin.registry.SBBlocks;
import com.wdiscute.sellingbin.registry.SBDataMaps;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class SellingBinJeiPlugin implements IModPlugin
{
    public static final Identifier ARROW = SellingBin.rl("textures/gui/selling_bin/selling_bin_background.png");
    public static final Identifier SLOT_BACKGROUND = SellingBin.rl("textures/gui/slot_background.png");

    public static List<ItemStack> currencies = new ArrayList<>();
    public static List<ItemStack> sellables = new ArrayList<>();

    public static List<SellingBinCurrencyJeiRecipe.Recipe> listCurrencies = new ArrayList<>();
    public static List<SellingBinSellingJeiRecipe.Recipe> listSellables = new ArrayList<>();

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration)
    {
        currencies.clear();
        sellables.clear();
        listCurrencies.clear();
        listSellables.clear();

        //add all currencies
        SBDataMaps.getAllCurrencies(MinecraftClient.getInstance().world).forEach((item, value) ->
        {
            if (value != 0)
            {
                currencies.add(new ItemStack(item));
                listCurrencies.add(new SellingBinCurrencyJeiRecipe.Recipe(item, value));
            }
        });

        //add all item with selling bin value
        SBDataMaps.getAllSellableItems(MinecraftClient.getInstance().world).forEach((item, itemValue) ->
        {
            if (!itemValue.equals(SBDataMaps.ItemValue.EMPTY))
            {
                List<Text> comps = new ArrayList<>();

                for (int i = 0; i < itemValue.processors().size(); i++)
                {
                    AbstractProcessor processor = itemValue.processors().get(i);
                    if (processor.showDescriptionOnEmi())
                        comps.addAll(processor.getDescription());
                }
                sellables.add(new ItemStack(item));
                listSellables.add(new SellingBinSellingJeiRecipe.Recipe(
                        item,
                        itemValue,
                        Currency.calculateValueFromSingleStack(new ItemStack(item), MinecraftClient.getInstance().player),
                        comps
                ));
            }

        });


        //register categories
        registration.addRecipeCategories(new SellingBinSellingJeiRecipe(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new SellingBinCurrencyJeiRecipe(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {

        //add currencies recipes
        registration.addRecipes(SellingBinCurrencyJeiRecipe.Recipe.TYPE, listCurrencies);

        if (currencies.isEmpty())
            registration.addRecipes(SellingBinCurrencyJeiRecipe.Recipe.TYPE, List.of(new SellingBinCurrencyJeiRecipe.Recipe(Items.BARRIER, 0)));


        //add sellables recipes
        registration.addRecipes(SellingBinSellingJeiRecipe.Recipe.TYPE, listSellables);

        if (sellables.isEmpty())
            registration.addRecipes(SellingBinSellingJeiRecipe.Recipe.TYPE, List.of(new SellingBinSellingJeiRecipe.Recipe(Items.BARRIER, SBDataMaps.ItemValue.EMPTY, 0, List.of())));


    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
    {
        registration.addRecipeCatalyst(SBBlocks.SELLING_BIN, SellingBinSellingJeiRecipe.Recipe.TYPE);
        registration.addRecipeCatalyst(SBBlocks.SELLING_BIN, SellingBinCurrencyJeiRecipe.Recipe.TYPE);
    }

    @Override
    public Identifier getPluginUid()
    {
        return SellingBin.rl("selling_bin_jei_plugin");
    }
}
