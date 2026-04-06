package com.wdiscute.sellingbin.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SellingBinCurrencyEmiRecipe implements EmiRecipe
{

    private final Identifier id;
    private final List<EmiStack> output;
    private final int value;

    public SellingBinCurrencyEmiRecipe(Item item, int value)
    {
        this.output = List.of(EmiStack.of(item));
        this.id = Registries.ITEM.getId(item);
        this.value = value;
    }

    @Override
    public EmiRecipeCategory getCategory()
    {
        return SellingBinEmiPlugin.SELLING_BIN_CURRENCIES_CATEGORY;
    }

    @Override
    public @Nullable Identifier getId()
    {
        return Identifier.of(id.getNamespace(), "/currency/" + id.getPath());
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
        widgets.addSlot(EmiIngredient.of(SellingBinEmiPlugin.sellable.isEmpty() ? List.of(EmiIngredient.of(Ingredient.ofItems(Items.BARRIER))) : SellingBinEmiPlugin.sellable), 5, 2);

        widgets.addTexture(SellingBinEmiPlugin.ARROW, 25, 2);

        widgets.addSlot(EmiIngredient.of(output), 45, 2).recipeContext(this);

        widgets.addText(Text.literal(value + " ").append(Text.translatable("gui.selling_bin.value")),
                67, 7, 0x000000, false);
    }
}
