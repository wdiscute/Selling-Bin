package com.wdiscute.sellingbin.registry;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.bin.SellingBinBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface SBBlocks
{
    DeferredRegister.Items ITEMS = DeferredRegister.createItems(SellingBin.MOD_ID);
    DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SellingBin.MOD_ID);

    DeferredBlock<Block> SELLING_BIN = BLOCKS.registerBlock("selling_bin", SellingBinBlock::new);
    DeferredItem<Item> SELLING_BIN_ITEM = ITEMS.registerItem("selling_bin",
            (p) -> new BlockItem(SELLING_BIN.get(), p));

    static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
}
