package com.wdiscute.sellingbin.registry;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.bin.SellingBinBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface SBBlocks
{
    DeferredRegister.Items ITEMS = DeferredRegister.createItems(SellingBin.MOD_ID);
    DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SellingBin.MOD_ID);

    DeferredBlock<Block> SELLING_BIN = registerBlock("selling_bin", SellingBinBlock::new);

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block)
    {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        ITEMS.registerItem(name, (p) -> new BlockItem(toReturn.get(), p));
        return toReturn;
    }

    static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
}
