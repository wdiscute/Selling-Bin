package com.wdiscute.sellingbin.datagen;

import com.wdiscute.sellingbin.registry.SBBlocks;
import com.wdiscute.sellingbin.registry.SBItemPredicate;
import net.minecraft.advancements.critereon.ItemPredicate.Builder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class DGSBRecipeProvider extends RecipeProvider
{
    public DGSBRecipeProvider(PackOutput output)
    {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> output) {
        {
            //selling bin
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, SBBlocks.SELLING_BIN)
                    .define('B', Blocks.BARREL)
                    .define('S', ItemTags.WOODEN_SLABS)
                    .define('C', ItemTags.WOOL_CARPETS)
                    .pattern("CCC")
                    .pattern("SBS")
                    .pattern("SSS")
                    .unlockedBy("selling_bin_sellable", has(Blocks.BARREL))
                    .save(output);
        }

    }
}
