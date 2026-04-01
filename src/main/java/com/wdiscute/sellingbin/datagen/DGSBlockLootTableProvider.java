package com.wdiscute.sellingbin.datagen;

import com.wdiscute.sellingbin.registry.SBBlocks;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.nikdo53.tinymultiblocklib.block.AbstractMultiBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DGSBlockLootTableProvider extends BlockLootSubProvider
{
    protected DGSBlockLootTableProvider(HolderLookup.Provider registries)
    {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate()
    {
        //selling bin because datagen sucks
        LootTable.Builder builder = LootTable.lootTable().withPool(this.applyExplosionCondition(SBBlocks.SELLING_BIN.get(), LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(SBBlocks.SELLING_BIN.get()).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(SBBlocks.SELLING_BIN.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(AbstractMultiBlock.CENTER, true))))));
        add(SBBlocks.SELLING_BIN.get(), builder);
    }

    @Override
    protected Iterable<Block> getKnownBlocks()
    {
        List<Block> list = new ArrayList<>();
        list.add(SBBlocks.SELLING_BIN.get());
        return list::iterator;
    }
}
