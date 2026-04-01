package com.wdiscute.sellingbin.registry;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.bin.SellingBinBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface ModBlocks
{
    DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SellingBin.MOD_ID);

    DeferredBlock<Block> SELLING_BIN = registerBlock("selling_bin", SellingBinBlock::new);

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block)
    {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () -> new BlockItem(toReturn.get(),
                new Item.Properties().setId(ResourceKey.create(BuiltInRegistries.ITEM.key(), SellingBin.rl("selling_bin")))));
        return toReturn;
    }

    static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }
}
