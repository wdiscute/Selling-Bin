package com.wdiscute.sellingbin.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;

public class DurabilityProcessor extends AbstractProcessor
{
    public DurabilityProcessor() {}

    public static final MapCodec<DurabilityProcessor> CODEC = MapCodec.unit(DurabilityProcessor::new);

    @Override
    public MapCodec<? extends AbstractProcessor> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractProcessor, AbstractProcessor> getRegistryHolder()
    {
        return ModProcessors.DURABILITY;
    }

    @Override
    public int addValue(int baseValue, int currentValue, ItemStack itemStack)
    {
        if (itemStack.getDamageValue() == 0) return 0;

        //reduces the current value by a percentage of remaining durability
        //100% = full untouched
        //50% = half value
        float durability = (float) (itemStack.getMaxDamage() - itemStack.getDamageValue()) / itemStack.getMaxDamage();

        return (int) (-currentValue * (1 - durability));
    }
}
