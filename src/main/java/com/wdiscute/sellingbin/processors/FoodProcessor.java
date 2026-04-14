package com.wdiscute.sellingbin.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.nikdo53.neobackports.registry.DeferredHolder;

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
    public DeferredHolder<AbstractProcessor, AbstractProcessor> getRegistryHolder()
    {
        return SBProcessors.FOOD_PROCESSOR;
    }

    @Override
    public boolean showDescriptionOnEmi()
    {
        return true;
    }

    @Override
    public List<Component> getDescription()
    {
        return List.of(Component.translatable("gui.selling_bin.processor.food"));
    }

    @Override
    public int addValue(int baseValue, int currentValue, ItemStack itemStack, BlockEntity blockEntity, Player player)
    {
        FoodProperties foodProperties = itemStack.getFoodProperties(null);

        if (foodProperties == null) return 0;

        return (int) (foodProperties.getNutrition() + foodProperties.getSaturationModifier() * 2);
    }
}
