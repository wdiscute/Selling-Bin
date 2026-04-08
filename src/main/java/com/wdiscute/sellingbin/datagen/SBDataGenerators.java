package com.wdiscute.sellingbin.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class SBDataGenerators implements DataGeneratorEntrypoint
{

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator)
    {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(DGSBDatamaps::new);
        pack.addProvider(DGSBRecipeProvider::new);
        pack.addProvider(DGSBlockLootTableProvider::new);
    }
}
