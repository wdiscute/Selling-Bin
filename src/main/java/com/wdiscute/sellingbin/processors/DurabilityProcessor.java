package com.wdiscute.sellingbin.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class DurabilityProcessor extends AbstractProcessor
{
    public DurabilityProcessor(){}

    public static final MapCodec<DurabilityProcessor> CODEC = MapCodec.unit(DurabilityProcessor::new);

    @Override
    public MapCodec<? extends AbstractProcessor> codec()
    {
        return CODEC;
    }


    @Override
    public Identifier getIdentifier()
    {
        return SBProcessors.DURABILITY;
    }

    @Override
    public boolean showDescriptionOnEmi()
    {
        return true;
    }

    @Override
    public List<Text> getDescription()
    {
        return List.of(Text.translatable("gui.selling_bin.processor.durability"));
    }

    @Override
    public int addValue(int baseValue, int currentValue, ItemStack itemStack, BlockEntity blockEntity, PlayerEntity player)
    {
        if (itemStack.getDamage() == 0) return 0;

        //reduces the current value by a percentage of remaining durability
        //100% = full untouched
        //50% = half value
        float durability = (float) (itemStack.getMaxDamage() - itemStack.getDamage()) / itemStack.getMaxDamage();

        return (int) (-currentValue * (1 - durability));
    }
}
