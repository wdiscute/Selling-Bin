package com.wdiscute.sellingbin.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.cadentem.quality_food.core.codecs.Quality;
import de.cadentem.quality_food.registry.QFComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;
import java.util.Map;

public class QualityFoodsProcessor extends AbstractProcessor
{
    private Map<ResourceLocation, Float> qualityValues;

    public QualityFoodsProcessor()
    {
    }

    public QualityFoodsProcessor(Map<ResourceLocation, Float> qualityValues)
    {
        this.qualityValues = qualityValues;
    }

    public static final MapCodec<QualityFoodsProcessor> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.unboundedMap(ResourceLocation.CODEC, Codec.FLOAT)
                            .fieldOf("quality_type")
                            .forGetter(QualityFoodsProcessor::getQualityValues)
            ).apply(instance, QualityFoodsProcessor::new));

    private Map<ResourceLocation, Float> getQualityValues()
    {
        return qualityValues;
    }


    @Override
    public MapCodec<? extends AbstractProcessor> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractProcessor, AbstractProcessor> getIdentifier()
    {
        return SBProcessors.QUALITY_FOODS_PROCESSOR;
    }

    @Override
    public boolean showDescriptionOnEmi()
    {
        return true;
    }

    @Override
    public List<Component> getDescription()
    {
        return List.of(Component.translatable("gui.selling_bin.processor.quality_foods_processor"));
    }

    @Override
    public int addValue(int baseValue, int currentValue, ItemStack itemStack, BlockEntity blockEntity, Player player)
    {
        Quality quality = itemStack.get(QFComponents.QUALITY_DATA_COMPONENT);

        if (quality == null)
        {
            return 0;
        }

        ResourceLocation rl = quality.getType().getKey().location();

        if (qualityValues.containsKey(rl))
            return (int) (currentValue * qualityValues.get(rl)) - currentValue;
        else
            return 0;
    }
}
