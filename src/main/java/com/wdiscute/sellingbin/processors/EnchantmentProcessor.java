package com.wdiscute.sellingbin.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
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
                    Codec.unboundedMap(RegistryFixedCodec.of(RegistryKeys.ENCHANTMENT), Codec.INT)
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
        if (!EnchantmentHelper.hasEnchantments(itemStack)) return 0;

        AtomicInteger value = new AtomicInteger();

        ItemEnchantmentsComponent storedEnchants = itemStack.getOrDefault(DataComponentTypes.STORED_ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
        ItemEnchantmentsComponent appliedEnchants = itemStack.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
        ItemEnchantmentsComponent enchants = storedEnchants.isEmpty() ? appliedEnchants : storedEnchants;


        enchantments.forEach((o, i) -> value.addAndGet(enchants.getLevel(o) * i));

        return value.get();

    }
}
