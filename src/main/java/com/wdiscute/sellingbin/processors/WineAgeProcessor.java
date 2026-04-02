package com.wdiscute.sellingbin.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.cadentem.quality_food.core.codecs.Quality;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.satisfy.vinery.core.block.WineBottleBlock;
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
    public DeferredHolder<AbstractProcessor, AbstractProcessor> getRegistryHolder()
    {
        return SBProcessors.WINE_AGE_PROCESSOR;
    }

    @Override
    public boolean showDescriptionOnEmi()
    {
        return true;
    }

    @Override
    public List<Component> getDescription()
    {
        return List.of(Component.translatable("gui.selling_bin.processor.wine_age_processor"));
    }

    @Override
    public int addValue(int baseValue, int currentValue, ItemStack itemStack, BlockEntity blockEntity, Player player)
    {
        Level level = null;
        if(player != null) level = player.level();
        if(blockEntity != null) level = blockEntity.getLevel();

        WineYearComponent age = itemStack.get(DataComponentRegistry.WINE_YEAR);

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
