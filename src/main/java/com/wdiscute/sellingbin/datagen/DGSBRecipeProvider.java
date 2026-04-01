package com.wdiscute.sellingbin.datagen;

import com.wdiscute.sellingbin.registry.SBBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class DGSBRecipeProvider extends RecipeProvider
{
    public DGSBRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput)
    {
        super(provider, recipeOutput);
    }

    public static class Runner extends RecipeProvider.Runner
    {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider)
        {
            super(packOutput, provider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput)
        {
            return new DGSBRecipeProvider(provider, recipeOutput);
        }

        @Override
        public String getName()
        {
            return "Selling Bin Recipes";
        }
    }


    @Override
    protected void buildRecipes()
    {
        shaped(RecipeCategory.REDSTONE, SBBlocks.SELLING_BIN.get())
                .pattern("CCC")
                .pattern("SBS")
                .pattern("SSS")
                .define('B', Blocks.BARREL)
                .define('S', ItemTags.WOODEN_SLABS)
                .define('C', ItemTags.WOOL_CARPETS)
                .unlockedBy("has_barrel", has(Items.BARREL))
                .save(output);
    }
}
