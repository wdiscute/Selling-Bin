package com.wdiscute.sellingbin.registry;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.bin.SellingBinBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface SBBlockEntities
{
    DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, SellingBin.MOD_ID);

    Supplier<BlockEntityType<SellingBinBlockEntity>> SELLING_BIN = BLOCK_ENTITIES.register("selling_bin",
            () -> BlockEntityType.Builder.of(SellingBinBlockEntity::new,
                    SBBlocks.SELLING_BIN.get()
            ).build(null));


    static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
