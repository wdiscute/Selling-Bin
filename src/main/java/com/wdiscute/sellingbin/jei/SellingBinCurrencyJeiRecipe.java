package com.wdiscute.sellingbin.jei;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.registry.SBBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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
                .add(new ItemStack(recipe.item));

    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY)
    {
        if (SellingBinJeiPlugin.currencies.isEmpty())
        {
            for (int i = 0; i < 10; i++)
                guiGraphics.text(Minecraft.getInstance().font, Component.translatable("gui.selling_bin.currency.empty." + i), 5, 1 + i * 10, 0xff000000, false);
            return;
        }

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SellingBinJeiPlugin.ARROW, 25, 2, 192, 16, 16, 16, 16, 16, 256, 256);

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SellingBinJeiPlugin.SLOT_BACKGROUND, 4, 1, 0, 0, 18, 18, 18, 18, 18, 18);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, SellingBinJeiPlugin.SLOT_BACKGROUND, 44, 1, 0, 0, 18, 18, 18, 18, 18, 18);

        guiGraphics.text(Minecraft.getInstance().font, Component.literal(recipe.itemValue + " ").append(Component.translatable("gui.selling_bin.value")),
                67, 7, 0xff000000, false);
    }

    @Override
    public Identifier getIdentifier(Recipe recipe)
    {
        return BuiltInRegistries.ITEM.getKey(recipe.item());
    }

    public record Recipe(Item item, Integer itemValue)
    {
        //selling_bin_zcurrency because jei sorts alphabetically :)
        public static final IRecipeType<Recipe> TYPE = IRecipeType.create(SellingBin.rl("zselling_bin_currency"), Recipe.class);
    }
}
