package com.wdiscute.sellingbin.datagen;

import com.wdiscute.sellingbin.SellingBin;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = SellingBin.MOD_ID)
public class SBDataGenerators
{

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();

        PackOutput output = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        //loot table
        gen.addProvider(event.includeServer(), new LootTableProvider(output, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(DGSBlockLootTableProvider::new, LootContextParamSets.BLOCK)), lookupProvider));


        //recipe
        gen.addProvider(event.includeServer(), new DGSBRecipeProvider(output, lookupProvider));

        //data map
        gen.addProvider(event.includeServer(), new DGSBDataMapsProvider(output, lookupProvider));

    }
}
