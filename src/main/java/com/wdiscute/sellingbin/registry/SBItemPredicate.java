package com.wdiscute.sellingbin.registry;

import com.mojang.serialization.Codec;
import com.wdiscute.sellingbin.SellingBin;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.item.ItemSubPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public interface SBItemPredicate
{

    ItemSubPredicate.Type<SellingBinSellablePredicate> SELLING_BIN_SELLABLE =
            Registry.register(Registries.ITEM_SUB_PREDICATE_TYPE, Identifier.of(SellingBin.MOD_ID, "sellable_item"), SellingBinSellablePredicate.TYPE);


    record SellingBinSellablePredicate() implements ItemSubPredicate
    {
        public static final Codec<SellingBinSellablePredicate> CODEC = Codec.unit(SellingBinSellablePredicate::new);
        public static final ItemSubPredicate.Type<SellingBinSellablePredicate> TYPE = new ItemSubPredicate.Type<>(CODEC);

        @Override
        public boolean test(ItemStack stack)
        {
            return SBDataMaps.getOrDefault(stack, SBDataMaps.SELLING_BIN_VALUE, SBDataMaps.ItemValue.EMPTY).baseValue() > 0;
        }
    }
}
