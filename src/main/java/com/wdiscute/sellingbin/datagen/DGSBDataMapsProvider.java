package com.wdiscute.sellingbin.datagen;

import com.wdiscute.sellingbin.registry.SBDataMaps;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class DGSBDataMapsProvider extends DataMapProvider
{
    private HolderLookup.Provider holderProvider;

    protected DGSBDataMapsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider)
    {
        holderProvider = provider;

        var bin = this.builder(SBDataMaps.SELLING_BIN_VALUE);
        var currencies = this.builder(SBDataMaps.SELLING_BIN_CURRENCIES);

        //datagen is not ran in the 26.1 branch. Built-in datapacks are copied from 1.21.1

    }
}
