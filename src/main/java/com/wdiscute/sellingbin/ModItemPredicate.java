package com.wdiscute.sellingbin;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface ModItemPredicate
{
    DeferredRegister<ItemSubPredicate.Type<?>> ITEM_SUB_PREDICATES = DeferredRegister.create(BuiltInRegistries.ITEM_SUB_PREDICATE_TYPE, SellingBin.MOD_ID);

    DeferredHolder<ItemSubPredicate.Type<?>, ItemSubPredicate.Type<SellingBinSellablePredicate>> SELLING_BIN_SELLABLE =
            ITEM_SUB_PREDICATES.register("item_ability", () -> SellingBinSellablePredicate.TYPE);


    static void register(IEventBus eventBus)
    {
        ITEM_SUB_PREDICATES.register(eventBus);
    }

    record SellingBinSellablePredicate() implements ItemSubPredicate
    {
        public static final Codec<SellingBinSellablePredicate> CODEC = Codec.unit(SellingBinSellablePredicate::new);
        public static final ItemSubPredicate.Type<SellingBinSellablePredicate> TYPE = new ItemSubPredicate.Type<>(CODEC);


        public boolean matches(ItemStack stack)
        {
            return ModDataMaps.getOrDefault(stack, ModDataMaps.SELLING_BIN_VALUE, ModDataMaps.ItemValue.empty()).baseValue() > 0;
        }
    }
}
