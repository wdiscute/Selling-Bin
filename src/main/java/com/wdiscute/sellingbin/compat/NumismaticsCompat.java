package com.wdiscute.sellingbin.compat;

import net.minecraft.item.ItemStack;

public class NumismaticsCompat
{
    public static boolean deposit(ItemStack result, ItemStack card)
    {
        //numismatics is not ported yet
        return false;


//        BankAccount account = Numismatics.BANK.getAccount(CardItem.get(card));
//
//        if (account == null) return false;
//
//        return Arrays.stream(Coin.values()).anyMatch(o ->
//        {
//            if (Registries.ITEM.getKey(result.getItem()).equals(SellingBin.rl("numismatics", o.toString().toLowerCase())))
//            {
//                account.deposit(o, result.getCount());
//                return true;
//            }
//            return false;
//        });
    }
}
