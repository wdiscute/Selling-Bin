package com.wdiscute.sellingbin.datagen;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.processors.AbstractProcessor;
import com.wdiscute.sellingbin.processors.FoodProcessor;
import com.wdiscute.sellingbin.registry.SBDataMaps;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.nikdo53.datamapsfabric.datagen.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class DGSBDatamaps extends DataMapProvider
{

    protected DGSBDatamaps(FabricDataOutput packOutput, CompletableFuture<RegistryWrapper.WrapperLookup> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(RegistryWrapper.WrapperLookup provider) {
        super.gather(provider);

        var bin = this.builder(SBDataMaps.SELLING_BIN_VALUE);
        var currencies = this.builder(SBDataMaps.SELLING_BIN_CURRENCIES);

        //datagen is done in the 1.21.1 neoforge branch and copied to this branch


        if(false)
        {
            currencies.add(Items.EMERALD.getRegistryEntry(), 100, false);
            currencies.add(Items.EMERALD_BLOCK.getRegistryEntry(), 900, false);
        }

        //foods built-in datapack
        if (false)
        {
            bin.add(TagKey.of(RegistryKeys.ITEM, SellingBin.rl("c", "foods")), new FoodProcessor().create(10), false);

            bin.add(Items.GOLDEN_APPLE.getRegistryEntry(), AbstractProcessor.createEmpty(200), false);
            bin.add(Items.ENCHANTED_GOLDEN_APPLE.getRegistryEntry(), AbstractProcessor.createEmpty(3700), false);
            bin.add(Items.HONEY_BOTTLE.getRegistryEntry(), AbstractProcessor.createEmpty(150), false);
            bin.add(Items.CAKE.getRegistryEntry(), AbstractProcessor.createEmpty(200), false);

            bin.add(Items.OMINOUS_BOTTLE.getRegistryEntry(), AbstractProcessor.createEmpty(0), false);
            bin.add(Items.OMINOUS_BOTTLE.getRegistryEntry(), AbstractProcessor.createEmpty(0), false);
        }




    }
}
