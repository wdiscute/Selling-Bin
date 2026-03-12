package com.wdiscute.sellingbin.datagen;

import com.wdiscute.sellingbin.ModBlocks;
import com.wdiscute.sellingbin.ModItemPredicate;
import net.minecraft.advancements.critereon.ItemPredicate.Builder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class DGRecipeProvider extends RecipeProvider
{
    public DGRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output)
    {
        //rod
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.SELLING_BIN)
                .define('B', Blocks.BARREL)
                .define('S', ItemTags.SLABS)
                .define('C', ItemTags.WOOL_CARPETS)
                .pattern("CCC")
                .pattern("SBS")
                .pattern("SSS")
                .unlockedBy("selling_bin_sellable",
                        inventoryTrigger(Builder.item().withSubPredicate(ModItemPredicate.SELLING_BIN_SELLABLE.get(), new ModItemPredicate.SellingBinSellablePredicate())))
                .save(output);
    }




}
