package com.wdiscute.sellingbin.bin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.world.World;

public interface TickableBlockEntity
{
    void tick();

    static <T extends BlockEntity> BlockEntityTicker<T> getTicketHelper(World level) {
        return level.isClient ? null : (level0, pos0, state0, blockEntity) -> ((TickableBlockEntity)blockEntity).tick();
    }
}
