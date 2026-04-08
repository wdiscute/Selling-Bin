package com.wdiscute.sellingbin.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

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
    public Identifier getIdentifier()
    {
        return SBProcessors.BEER_QUALITY_PROCESSOR;
    }

    @Override
    public boolean showDescriptionOnEmi()
    {
        return true;
    }

    @Override
    public List<Text> getDescription()
    {
        return List.of(Text.translatable("gui.selling_bin.processor.beer_quality_processor"));
    }

    @Override
    public int addValue(int baseValue, int currentValue, ItemStack itemStack, BlockEntity blockEntity, PlayerEntity player)
    {
        //DrinkBlockItem.addQuality(itemStack, 3);
        NbtCompound tag = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        int quality = tag.contains("brewery.beer_quality") ? tag.getInt("brewery.beer_quality") : 0;

        if (qualityMultipliers.containsKey(quality + ""))
            return (int) (currentValue * qualityMultipliers.get(quality + "")) - currentValue;
        else
            return 0;
    }
}
