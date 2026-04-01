package com.wdiscute.sellingbin.emi;

import com.wdiscute.sellingbin.SellingBin;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SellingBinCurrencyEmiRecipe implements EmiRecipe
{

    private final ResourceLocation id;
    private final List<EmiStack> output;
    private final int value;

    public SellingBinCurrencyEmiRecipe(Item item, int value)
    {
        this.output = List.of(EmiStack.of(item));
        this.id = BuiltInRegistries.ITEM.getKey(item);
        this.value = value;
    }

    @Override
    public EmiRecipeCategory getCategory()
    {
        return SellingBinEmiPlugin.SELLING_BIN_CURRENCIES_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId()
    {
        return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "/currency/" + id.getPath());
    }

    @Override
    public List<EmiIngredient> getInputs()
    {
        return List.of();
    }

    @Override
    public List<EmiStack> getOutputs()
    {
        return output;
    }

    @Override
    public int getDisplayWidth()
    {
        return 130;
    }

    @Override
    public int getDisplayHeight()
    {
        return 22;
    }


    @Override
    public void addWidgets(WidgetHolder widgets)
    {
        widgets.addSlot(EmiIngredient.of(SellingBinEmiPlugin.sellable.isEmpty() ? List.of(EmiIngredient.of(Ingredient.of(Items.BARRIER))) : SellingBinEmiPlugin.sellable), 5, 2);

        widgets.addTexture(SellingBinEmiPlugin.ARROW, 25, 2);

        widgets.addSlot(EmiIngredient.of(output), 45, 2).recipeContext(this);

        widgets.addText(Component.literal(value + " ").append(Component.translatable("gui.selling_bin.value")),
                67, 7, 0x000000, false);
    }
}
