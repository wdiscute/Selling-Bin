package com.wdiscute.sellingbin.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class QualityFoodsProcessor extends AbstractProcessor
{
    private Map<Identifier, Float> qualityValues;

    public QualityFoodsProcessor()
    {
    }

    public QualityFoodsProcessor(Map<Identifier, Float> qualityValues)
    {
        this.qualityValues = qualityValues;
    }

    public static final MapCodec<QualityFoodsProcessor> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.unboundedMap(Identifier.CODEC, Codec.FLOAT)
                            .fieldOf("quality_type")
                            .forGetter(QualityFoodsProcessor::getQualityValues)
            ).apply(instance, QualityFoodsProcessor::new));

    private Map<Identifier, Float> getQualityValues()
    {
        return qualityValues;
    }


    @Override
    public MapCodec<? extends AbstractProcessor> codec()
    {
        return CODEC;
    }

    @Override
    public Identifier getIdentifier()
    {
        return SBProcessors.QUALITY_FOODS_PROCESSOR;
    }

    @Override
    public boolean showDescriptionOnEmi()
    {
        return true;
    }

    @Override
    public List<Text> getDescription()
    {
        return List.of(Text.translatable("gui.selling_bin.processor.quality_foods_processor"));
    }

    @Override
    public int addValue(int baseValue, int currentValue, ItemStack itemStack, BlockEntity blockEntity, PlayerEntity player)
    {
        //Quality quality = itemStack.get(QFComponents.QUALITY_DATA_COMPONENT);

        //if (quality == null)
        {
            return 0;
        }

        //Identifier rl = quality.getType().getKey().location();

        //if (qualityValues.containsKey(rl))
        //    return (int) (currentValue * qualityValues.get(rl)) - currentValue;
        //else
        //    return 0;
    }
}
