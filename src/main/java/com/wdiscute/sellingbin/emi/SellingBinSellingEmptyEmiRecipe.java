package com.wdiscute.sellingbin.emi;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.processors.AbstractProcessor;
import com.wdiscute.sellingbin.registry.ModDataMaps;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SellingBinSellingEmptyEmiRecipe implements EmiRecipe
{
    public SellingBinSellingEmptyEmiRecipe()
    {
    }

    @Override
    public EmiRecipeCategory getCategory()
    {
        return SellingBinEmiPlugin.SELLING_BIN_SELLING_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId()
    {
        return SellingBin.rl("/selling/empty");
    }

    @Override
    public List<EmiIngredient> getInputs()
    {
        return List.of();
    }

    @Override
    public List<EmiStack> getOutputs()
    {
        return List.of();
    }

    @Override
    public int getDisplayWidth()
    {
        return 350;
    }

    @Override
    public int getDisplayHeight()
    {
        return 90;
    }

    @Override
    public void addWidgets(WidgetHolder widgets)
    {
        for (int i = 0; i < 9; i++)
            widgets.addText(Component.translatable("gui.selling_bin.selling.empty." + i), 5, 1 + i * 10, 0x000000, false);
    }
}
