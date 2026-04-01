package com.wdiscute.sellingbin.jei;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.registry.SBBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class SellingBinCurrencyJeiRecipe extends AbstractRecipeCategory<SellingBinCurrencyJeiRecipe.Recipe>
{

    public SellingBinCurrencyJeiRecipe(IGuiHelper guiHelper)
    {
        super(
                Recipe.TYPE,
                Component.translatable("emi.category.selling_bin.selling_bin_currencies"),
                guiHelper.createDrawableItemLike(SBBlocks.SELLING_BIN),
                SellingBinJeiPlugin.currencies.isEmpty() ? 350 : 120,
                SellingBinJeiPlugin.currencies.isEmpty() ? 100 : 20
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Recipe recipe, IFocusGroup focuses)
    {
        if (SellingBinJeiPlugin.currencies.isEmpty()) return;

        builder.addInputSlot(5, 2)
                .addItemStacks(SellingBinJeiPlugin.sellables.isEmpty() ? List.of(Items.BARRIER.getDefaultInstance()) : SellingBinJeiPlugin.sellables);

        builder.addOutputSlot(45, 2)
                .addItemStack(new ItemStack(recipe.item));

    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY)
    {
        if (SellingBinJeiPlugin.currencies.isEmpty())
        {
            for (int i = 0; i < 10; i++)
                guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("gui.selling_bin.currency.empty." + i), 5, 1 + i * 10, 0x000000, false);
            return;
        }

        guiGraphics.blit(SellingBinJeiPlugin.ARROW, 25, 2, 16, 16, 192, 16, 16, 16, 256, 256);

        guiGraphics.blit(SellingBinJeiPlugin.SLOT_BACKGROUND, 4, 1, 18, 18, 0, 0, 18, 18, 18, 18);
        guiGraphics.blit(SellingBinJeiPlugin.SLOT_BACKGROUND, 44, 1, 18, 18, 0, 0, 18, 18, 18, 18);

        guiGraphics.drawString(Minecraft.getInstance().font, Component.literal(recipe.itemValue + " ").append(Component.translatable("gui.selling_bin.value")),
                67, 7, 0x000000, false);
    }

    @Override
    public ResourceLocation getRegistryName(Recipe recipe)
    {
        return BuiltInRegistries.ITEM.getKey(recipe.item());
    }


    public record Recipe(Item item, Integer itemValue)
    {
        public static final RecipeType<Recipe> TYPE = new RecipeType<>(SellingBin.rl("selling_bin_currency"), Recipe.class);
    }
}
