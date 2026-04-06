package com.wdiscute.sellingbin.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

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
    public Identifier getIdentifier()
    {
        return SBProcessors.EMPTY_PROCESSOR;
    }

    @Override
    public boolean showDescriptionOnEmi()
    {
        return false;
    }

    @Override
    public List<Text> getDescription()
    {
        return List.of();
    }

    @Override
    public int addValue(int baseValue, int currentValue, ItemStack itemStack, BlockEntity blockEntity, PlayerEntity player)
    {
        return 0;
    }
}
