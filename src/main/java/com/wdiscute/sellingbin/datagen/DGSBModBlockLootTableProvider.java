package com.wdiscute.sellingbin.datagen;

import com.wdiscute.sellingbin.registry.SBBlocks;
import com.wdiscute.sellingbin.registry.SBDataComponents;
import net.minecraft.advancements.predicates.BlockPredicate;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.MatchBlock;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.nikdo53.tinymultiblocklib.block.BaseMultiblock;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DGSBModBlockLootTableProvider extends BlockLootSubProvider
{
    public DGSBModBlockLootTableProvider(LootTableSubProvider.Context output)
    {
        super(
                Set.of(),
                FeatureFlags.REGISTRY.allFlags(),
                output
        );

    }

    @Override
    protected void generate()
    {
        //selling bin because datagen sucks
        LootTable.Builder builder = LootTable.lootTable()
                .withPool(
                        this.applyExplosionCondition(
                                SBBlocks.SELLING_BIN.get(),
                                LootPool.lootPool()
                                        .when(MatchBlock.blockMatches(BlockPredicate.Builder.block()
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(BaseMultiblock.CENTER, true)))
                                        )
                                        .setRolls(ContextIntProviders.exactly(1))
                                        .add(
                                                LootItem.lootTableItem(SBBlocks.SELLING_BIN.get())
                                                        .apply(
                                                                CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
                                                                        .include(DataComponents.CUSTOM_NAME)
                                                                        .include(DataComponents.CONTAINER)
                                                                        .include(DataComponents.LOCK)
                                                                        .include(DataComponents.CONTAINER_LOOT)
                                                                        .include(SBDataComponents.STORED_VALUE.get())
                                                        )
                                        )
                        )
                );

        add(SBBlocks.SELLING_BIN.get(), builder);
    }

    @Override
    protected Iterable<net.minecraft.world.level.block.Block> getKnownBlocks()
    {
        List<net.minecraft.world.level.block.Block> list = new ArrayList<>();
        list.add(SBBlocks.SELLING_BIN.get());
        return list::iterator;
    }
}
