package com.wdiscute.sellingbin.emi;

import com.wdiscute.sellingbin.bin.Currency;
import com.wdiscute.sellingbin.processors.AbstractProcessor;
import com.wdiscute.sellingbin.registry.ModDataMaps;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SellingBinSellingEmiRecipe implements EmiRecipe
{

    private final ResourceLocation id;
    private final List<EmiIngredient> input;
    private final Item item;
    private final ModDataMaps.ItemValue itemValue;
    private final List<Component> description;

    public SellingBinSellingEmiRecipe(Item item, ModDataMaps.ItemValue itemValue)
    {
        this.id = BuiltInRegistries.ITEM.getKey(item);
        this.input = List.of(EmiIngredient.of(Ingredient.of(item)));
        this.item = item;
        this.itemValue = itemValue;

        List<Component> comps = new ArrayList<>();

        for (int i = 0; i < itemValue.processors().size(); i++)
        {
            AbstractProcessor processor = itemValue.processors().get(i);
            if (processor.showDescriptionOnEmi())
                comps.addAll(processor.getDescription());
        }

        description = comps;
    }

    @Override
    public EmiRecipeCategory getCategory()
    {
        return SellingBinEmiPlugin.SELLING_BIN_SELLING_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId()
    {
        return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "/selling/" + id.getPath());
    }

    @Override
    public List<EmiIngredient> getInputs()
    {
        return input;
    }

    @Override
    public List<EmiStack> getOutputs()
    {
        List<EmiStack> list = new ArrayList<>();

        for (EmiIngredient emiIng : SellingBinEmiPlugin.currencies)
        {
            list.addAll(emiIng.getEmiStacks());
        }
        return list;
    }

    @Override
    public int getDisplayWidth()
    {
        return 120;
    }

    @Override
    public int getDisplayHeight()
    {
        return 22;
    }

    @Override
    public void addWidgets(WidgetHolder widgets)
    {

        widgets.addSlot(input.get(0), 5, 2);

        widgets.addTexture(SellingBinEmiPlugin.ARROW, 25, 2);

        widgets.addSlot(EmiIngredient.of(SellingBinEmiPlugin.currencies), 45, 2).recipeContext(this);

        widgets.addText(Component.translatable("gui.selling_bin.base_price"), 67, 1, 0x000000, false);

        int price = Currency.calculateValueFromSingleStack(new ItemStack(item), Minecraft.getInstance().player);

        widgets.addText(Component.literal(price + " "), 67, 11, 0x000000, false);

        widgets.add(new HoverTextWidget(108, 13, 10, 10, description));

        widgets.addText(Component.literal("[!]"), 111, 15, 0x880000, false);
    }
}
