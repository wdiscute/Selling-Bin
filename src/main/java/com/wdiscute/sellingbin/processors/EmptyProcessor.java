package com.wdiscute.sellingbin.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.nikdo53.neobackports.registry.DeferredHolder;

import java.util.List;

public class EmptyProcessor extends AbstractProcessor
{
    public EmptyProcessor(){}

    public static final MapCodec<EmptyProcessor> CODEC = MapCodec.unit(EmptyProcessor::new);

    @Override
    public MapCodec<? extends AbstractProcessor> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractProcessor, AbstractProcessor> getRegistryHolder()
    {
        return SBProcessors.EMPTY_PROCESSOR;
    }

    @Override
    public boolean showDescriptionOnEmi()
    {
        return false;
    }

    @Override
    public List<Component> getDescription()
    {
        return List.of();
    }

    @Override
    public int addValue(int baseValue, int currentValue, ItemStack itemStack, BlockEntity blockEntity, Player player)
    {
        return 0;
    }
}
