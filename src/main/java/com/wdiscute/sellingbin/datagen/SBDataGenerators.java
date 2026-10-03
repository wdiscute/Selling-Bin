package com.wdiscute.sellingbin.datagen;

import com.wdiscute.sellingbin.SellingBin;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.data.loot.packs.VanillaLootTableProvider;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = SellingBin.MOD_ID)
public class SBDataGenerators
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event)
    {
        RegistrySetBuilder registry = new RegistrySetBuilder()
                .add(
                        Registries.LOOT_TABLE,
                        new LootTableProvider(
                                Set.of(),
                                List.of(
                                        new LootTableProvider.SubProviderEntry(DGSBModBlockLootTableProvider::new, LootContextParamSets.BLOCK)
                                )
                        )
                )
                .add(
                        DGSBRecipeProvider.create()
                );


        //block tags
        event.getGenerator().addProvider(true, new DGSBBlocksTagsProvider(
                event.getDefaultPackGenerator().getPackOutput(),
                event.getReloadableLookupProvider()));

        //data map - not ran on 26+
        //gen.addProvider(true, new DGSBDataMapsProvider(output, lookupProvider));

        event.createReloadableRegistryObjects(registry);
    }
}
