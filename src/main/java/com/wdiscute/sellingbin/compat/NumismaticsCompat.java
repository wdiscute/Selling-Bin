package com.wdiscute.sellingbin.compat;

import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class NumismaticsCompat
{
    public static boolean deposit(ItemStack result, ItemStack card)
    {
//        BankAccount account = Numismatics.BANK.getAccount(CardItem.get(card));
//
//        if (account == null) return false;
//
//        return Arrays.stream(Coin.values()).anyMatch(o ->
//        {
//            if (BuiltInRegistries.ITEM.getKey(result.getItem()).equals(SellingBin.rl("numismatics", o.toString().toLowerCase())))
//            {
//                account.deposit(o, result.getCount());
//                return true;
//            }
//            return false;
//        });
        return false;
    }
}
