package com.wdiscute.sellingbin.jei;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.registry.SBBlocks;
import com.wdiscute.sellingbin.registry.SBDataMaps;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

import java.util.List;
import java.util.Optional;

public class SellingBinSellingJeiRecipe extends AbstractRecipeCategory<SellingBinSellingJeiRecipe.Recipe>
{

    public SellingBinSellingJeiRecipe(IGuiHelper guiHelper)
    {
        super(
                Recipe.TYPE,
                Component.translatable("emi.category.selling_bin.selling_bin_selling"),
                guiHelper.createDrawableItemLike(SBBlocks.SELLING_BIN),
                SellingBinJeiPlugin.sellables.isEmpty() ? 350 : 120,
                SellingBinJeiPlugin.sellables.isEmpty() ? 100 : 20
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Recipe recipe, IFocusGroup focuses)
    {
        if(SellingBinJeiPlugin.sellables.isEmpty()) return;

        builder.addInputSlot(5, 2)
                .addItemStack(recipe.item().getDefaultInstance());

        builder.addOutputSlot(45, 2)
                .addItemStacks(SellingBinJeiPlugin.currencies.isEmpty() ? List.of(Items.BARRIER.getDefaultInstance()) : SellingBinJeiPlugin.currencies);
    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY)
    {
        if (SellingBinJeiPlugin.sellables.isEmpty())
        {
            for (int i = 0; i < 10; i++)
                guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("gui.selling_bin.selling.empty." + i), 5, 1 + i * 10, 0x000000, false);
            return;
        }

        Font font = Minecraft.getInstance().font;
        guiGraphics.blit(SellingBinJeiPlugin.ARROW, 25, 2, 16, 16, 192, 16, 16, 16, 256, 256);
        guiGraphics.blit(SellingBinJeiPlugin.SLOT_BACKGROUND, 4, 1, 18, 18, 0, 0, 18, 18, 18, 18);
        guiGraphics.blit(SellingBinJeiPlugin.SLOT_BACKGROUND, 44, 1, 18, 18, 0, 0, 18, 18, 18, 18);

        guiGraphics.drawString(font, Component.translatable("gui.selling_bin.base_price"), 67, 1, 0x000000, false);
        guiGraphics.drawString(font, Component.literal(recipe.price + " "), 67, 11, 0x000000, false);

        if (mouseX > 110 && mouseX < 120 && mouseY > 11 && mouseY < 21)
            guiGraphics.renderTooltip(font, recipe.comps, Optional.empty(), (int) mouseX, (int) mouseY);

        if (!recipe.comps.isEmpty())
            guiGraphics.drawString(font, Component.literal("[!]"), 111, 13, 0x880000, false);
    }

    @Override
    public ResourceLocation getRegistryName(Recipe recipe)
    {
        return BuiltInRegistries.ITEM.getKey(recipe.item());
    }

    public record Recipe(Item item, SBDataMaps.ItemValue itemValue, int price, List<Component> comps)
    {
        public static final RecipeType<Recipe> TYPE = new RecipeType<>(SellingBin.rl("selling_bin_sellable"), Recipe.class);
    }
}
