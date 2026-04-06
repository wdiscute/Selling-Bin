package com.wdiscute.sellingbin.registry;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.bin.SellingBinBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public interface SBBlockEntities
{
    BlockEntityType<SellingBinBlockEntity> SELLING_BIN =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(SellingBin.MOD_ID, "selling_bin"),
                    BlockEntityType.Builder.create(SellingBinBlockEntity::new, SBBlocks.SELLING_BIN).build(null));

    static void init()
    {
    }
}
