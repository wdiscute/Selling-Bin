package com.wdiscute.sellingbin.datagen;

import com.wdiscute.sellingbin.registry.ModBlocks;
import com.wdiscute.sellingbin.registry.ModItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class DGRecipeProvider extends RecipeProvider
{
    protected DGRecipeProvider(HolderLookup.Provider registries, RecipeOutput output)
    {
        super(registries, output);
    }

    @Override
    protected void buildRecipes()
    {
        //rod
//        ShapedRecipeBuilder.shaped(ModBlocks.SELLING_BIN, RecipeCategory.REDSTONE)
//                .define('B', Blocks.BARREL)
//                .define('S', ItemTags.SLABS)
//                .define('C', ItemTags.WOOL_CARPETS)
//                .pattern("CCC")
//                .pattern("SBS")
//                .pattern("SSS")
//                .unlockedBy("selling_bin_sellable",
//                        inventoryTrigger(Builder.item().withSubPredicate(ModItemPredicate.SELLING_BIN_SELLABLE.get(), new ModItemPredicate.SellingBinSellablePredicate())))
//                .save(output);
    }
}
