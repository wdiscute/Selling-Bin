package com.wdiscute.sellingbin.datagen;

import com.wdiscute.sellingbin.registry.SBBlocks;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Blocks;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DGSBRecipeProvider extends RecipeProvider
{
    public DGSBRecipeProvider(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput)
    {
        super(recipeOutput, advancementOutput);
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


    public static MultiRegistryBootstrap create()
    {
        return new MultiRegistryBootstrap()
        {
            @Override
            public Set<ResourceKey<? extends Registry<?>>> requestedRegistries()
            {
                // Return the registries we are adding entries to.
                return Set.of(Registries.RECIPE, Registries.ADVANCEMENT);
            }

            @Override
            public void run(MultiRegistryBootstrap.BootstrapGetter registries)
            {
                // Run the recipe provider.
                new DGSBRecipeProvider(registries.get(Registries.RECIPE), registries.get(Registries.ADVANCEMENT)).buildRecipes();
            }
        };
    }
}
