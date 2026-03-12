package com.wdiscute.sellingbin.datagen;

import com.wdiscute.sellingbin.ModDataMaps;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class DGDataMapsProvider extends DataMapProvider
{
    protected DGDataMapsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider)
    {
        var bin = this.builder(ModDataMaps.SELLING_BIN_VALUE);
        var currencies = this.builder(ModDataMaps.SELLING_BIN_CURRENCIES);

        bin.add(Items.STRUCTURE_VOID.builtInRegistryHolder(), ModDataMaps.ItemValue.empty(20), false);

        currencies.add(Items.EMERALD.builtInRegistryHolder(), 100, false);
        currencies.add(Items.EMERALD_BLOCK.builtInRegistryHolder(), 900, false);
    }
}
