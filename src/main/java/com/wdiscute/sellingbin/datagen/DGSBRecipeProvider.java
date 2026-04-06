package com.wdiscute.sellingbin.datagen;

import com.wdiscute.sellingbin.registry.SBBlocks;
import com.wdiscute.sellingbin.registry.SBItemPredicate;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class DGSBRecipeProvider extends FabricRecipeProvider
{
    public DGSBRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture)
    {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter output)
    {
        //selling bin
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, SBBlocks.SELLING_BIN)
                .input('B', Blocks.BARREL)
                .input('S', ItemTags.WOODEN_SLABS)
                .input('C', ItemTags.WOOL_CARPETS)
                .pattern("CCC")
                .pattern("SBS")
                .pattern("SSS")
                .criterion("selling_bin_sellable",
                        conditionsFromPredicates(ItemPredicate.Builder.create().items()
                                .subPredicate(SBItemPredicate.SELLING_BIN_SELLABLE, new SBItemPredicate.SellingBinSellablePredicate())))
                .offerTo(output);
    }
}
