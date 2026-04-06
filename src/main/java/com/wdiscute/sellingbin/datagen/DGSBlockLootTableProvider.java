package com.wdiscute.sellingbin.datagen;

import com.wdiscute.sellingbin.registry.SBBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryWrapper;
import net.nikdo53.tinymultiblocklib.block.AbstractMultiBlock;

import java.util.concurrent.CompletableFuture;

public class DGSBlockLootTableProvider extends FabricBlockLootTableProvider
{
    public DGSBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup)
    {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate()
    {
        addDrop(SBBlocks.SELLING_BIN,
                LootTable.builder().pool(this.addSurvivesExplosionCondition(SBBlocks.SELLING_BIN,
                        LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(ItemEntry.builder(SBBlocks.SELLING_BIN)
                                        .conditionally(BlockStatePropertyLootCondition.builder(SBBlocks.SELLING_BIN)
                                                .properties(StatePredicate.Builder.create()
                                                        .exactMatch(AbstractMultiBlock.CENTER, true)
                                                )))))


        );
    }
}
