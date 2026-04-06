package com.wdiscute.sellingbin.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class EnchantmentProcessor extends AbstractProcessor
{
    private Map<RegistryEntry<Enchantment>, Integer> enchantments;

    public EnchantmentProcessor()
    {
    }

    public EnchantmentProcessor(Map<RegistryEntry<Enchantment>, Integer> enchantments)
    {
        this.enchantments = enchantments;
    }

    public static final MapCodec<EnchantmentProcessor> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.unboundedMap(Enchantment.CODEC, Codec.INT)
                            .fieldOf("enchantments")
                            .forGetter(EnchantmentProcessor::getEnchantments)
            ).apply(instance, EnchantmentProcessor::new));

    private Map<RegistryEntry<Enchantment>, Integer> getEnchantments()
    {
        return enchantments;
    }


    @Override
    public MapCodec<? extends AbstractProcessor> codec()
    {
        return CODEC;
    }

    @Override
    public Identifier getIdentifier()
    {
        return SBProcessors.ENCHANTMENTS_PROCESSOR;
    }

    @Override
    public boolean showDescriptionOnEmi()
    {
        return true;
    }

    @Override
    public List<Text> getDescription()
    {
        return List.of(Text.translatable("gui.selling_bin.processor.enchantments"));
    }

    @Override
    public int addValue(int baseValue, int currentValue, ItemStack itemStack, BlockEntity blockEntity, PlayerEntity player)
    {
        if (!EnchantmentHelper.hasAnyEnchantments(itemStack)) return 0;

        AtomicInteger value = new AtomicInteger();

        ItemEnchantments storedEnchants = itemStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
        ItemEnchantments appliedEnchants = itemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        ItemEnchantments enchants = storedEnchants.isEmpty() ? appliedEnchants : storedEnchants;


        enchantments.forEach((o, i) -> value.addAndGet(enchants.getLevel(o) * i));

        return value.get();

    }
}
