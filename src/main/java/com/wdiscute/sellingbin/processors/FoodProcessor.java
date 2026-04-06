package com.wdiscute.sellingbin.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class FoodProcessor extends AbstractProcessor
{
    public FoodProcessor(){}

    public static final MapCodec<FoodProcessor> CODEC = MapCodec.unit(FoodProcessor::new);

    @Override
    public MapCodec<? extends AbstractProcessor> codec()
    {
        return CODEC;
    }

    @Override
    public Identifier getIdentifier()
    {
        return SBProcessors.FOOD_PROCESSOR;
    }

    @Override
    public boolean showDescriptionOnEmi()
    {
        return true;
    }

    @Override
    public List<Text> getDescription()
    {
        return List.of(Text.translatable("gui.selling_bin.processor.food"));
    }

    @Override
    public int addValue(int baseValue, int currentValue, ItemStack itemStack, BlockEntity blockEntity, PlayerEntity player)
    {
        FoodComponent foodProperties = itemStack.get(DataComponentTypes.FOOD);

        if (foodProperties == null) return 0;

        return (int) (foodProperties.nutrition() + foodProperties.saturation() * 2);
    }
}
