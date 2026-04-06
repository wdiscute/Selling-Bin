package com.wdiscute.sellingbin.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.satisfy.vinery.core.components.WineYearComponent;
import net.satisfy.vinery.core.registry.DataComponentRegistry;
import net.satisfy.vinery.core.util.WineYears;

import java.util.List;
import java.util.Map;

public class WineAgeProcessor extends AbstractProcessor
{
    private Map<String, Float> ageMultipliers;

    public WineAgeProcessor()
    {
    }

    public WineAgeProcessor(Map<String, Float> ageMultipliers)
    {
        this.ageMultipliers = ageMultipliers;
    }

    public static final MapCodec<WineAgeProcessor> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.unboundedMap(Codec.STRING, Codec.FLOAT)
                            .fieldOf("age_multipliers")
                            .forGetter(WineAgeProcessor::getAgeMultipliers)
            ).apply(instance, WineAgeProcessor::new));

    private Map<String, Float> getAgeMultipliers()
    {
        return ageMultipliers;
    }

    @Override
    public MapCodec<? extends AbstractProcessor> codec()
    {
        return CODEC;
    }

    @Override
    public Identifier getIdentifier()
    {
        return SBProcessors.WINE_AGE_PROCESSOR;
    }

    @Override
    public boolean showDescriptionOnEmi()
    {
        return true;
    }

    @Override
    public List<Text> getDescription()
    {
        return List.of(Text.translatable("gui.selling_bin.processor.wine_age_processor"));
    }

    @Override
    public int addValue(int baseValue, int currentValue, ItemStack itemStack, BlockEntity blockEntity, PlayerEntity player)
    {
        World level = null;
        if(player != null) level = player.getWorld();
        if(blockEntity != null) level = blockEntity.getWorld();

        WineYearComponent age = itemStack.get(DataComponentRegistry.WINE_YEAR.get());

        if (age == null)
        {
            return 0;
        }

        String wineAgeYears = WineYears.getWineAgeYears(itemStack, level) + "";

        if (ageMultipliers.containsKey(wineAgeYears))
            return (int) (currentValue * ageMultipliers.get(wineAgeYears)) - currentValue;
        else
            return 0;
    }
}
