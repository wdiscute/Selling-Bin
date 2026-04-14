package com.wdiscute.sellingbin.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.nikdo53.neobackports.registry.DeferredHolder;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class EnchantmentProcessor extends AbstractProcessor
{
    private Map<Holder<Enchantment>, Integer> enchantments;

    public EnchantmentProcessor()
    {
    }

    public EnchantmentProcessor(Map<Holder<Enchantment>, Integer> enchantments)
    {
        this.enchantments = enchantments;
    }

    public static final MapCodec<EnchantmentProcessor> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.unboundedMap(BuiltInRegistries.ENCHANTMENT.holderByNameCodec(), Codec.INT)
                            .fieldOf("enchantments")
                            .forGetter(EnchantmentProcessor::getEnchantments)
            ).apply(instance, EnchantmentProcessor::new));

    private Map<Holder<Enchantment>, Integer> getEnchantments()
    {
        return enchantments;
    }


    @Override
    public MapCodec<? extends AbstractProcessor> codec()
    {
        return CODEC;
    }

    @Override
    public DeferredHolder<AbstractProcessor, AbstractProcessor> getRegistryHolder()
    {
        return SBProcessors.ENCHANTMENTS_PROCESSOR;
    }

    @Override
    public boolean showDescriptionOnEmi()
    {
        return true;
    }

    @Override
    public List<Component> getDescription()
    {
        return List.of(Component.translatable("gui.selling_bin.processor.enchantments"));
    }

    @Override
    public int addValue(int baseValue, int currentValue, ItemStack itemStack, BlockEntity blockEntity, Player player)
    {
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemStack);
        if (map.isEmpty()) return 0;

        AtomicInteger value = new AtomicInteger();


        enchantments.forEach((o, i) -> value.addAndGet(map.get(o.value()) * i));

        return value.get();

    }
}
