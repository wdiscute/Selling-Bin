package com.wdiscute.sellingbin.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.satisfy.brewery.core.item.DrinkBlockItem;

import java.util.List;
import java.util.Map;

public class BeerQualityProcessor extends AbstractProcessor
{
    private Map<String, Float> qualityMultipliers;

    public BeerQualityProcessor()
    {
    }

    public BeerQualityProcessor(Map<String, Float> ageMultipliers)
    {
        this.qualityMultipliers = ageMultipliers;
    }

    public static final MapCodec<BeerQualityProcessor> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.unboundedMap(Codec.STRING, Codec.FLOAT)
                            .fieldOf("quality_multipliers")
                            .forGetter(BeerQualityProcessor::getQualityMultipliers)
            ).apply(instance, BeerQualityProcessor::new));

    private Map<String, Float> getQualityMultipliers()
    {
        return qualityMultipliers;
    }

    @Override
    public MapCodec<? extends AbstractProcessor> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractProcessor, AbstractProcessor> getRegistryHolder()
    {
        return SBProcessors.BEER_QUALITY_PROCESSOR;
    }

    @Override
    public boolean showDescriptionOnEmi()
    {
        return true;
    }

    @Override
    public List<Component> getDescription()
    {
        return List.of(Component.translatable("gui.selling_bin.processor.beer_quality_processor"));
    }

    @Override
    public int addValue(int baseValue, int currentValue, ItemStack itemStack, BlockEntity blockEntity, Player player)
    {
        //DrinkBlockItem.addQuality(itemStack, 3);
        CompoundTag tag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        int quality = tag.contains("brewery.beer_quality") ? tag.getInt("brewery.beer_quality") : 0;

        if (qualityMultipliers.containsKey(quality + ""))
            return (int) (currentValue * qualityMultipliers.get(quality + "")) - currentValue;
        else
            return 0;
    }
}
