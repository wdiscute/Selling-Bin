package com.wdiscute.sellingbin.event;

import com.wdiscute.sellingbin.SellingBin;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;

public class SBvents
{
//    public static void addPackFinders(AddPackFindersEvent event)
//    {
//        DefaultPackSource packSource = new DefaultPackSource();
//
//        event.addPackFinders(
//                SellingBin.rl("built_in_datapacks/selling_bin_currency_emeralds"),
//                ResourceType.SERVER_DATA,
//                Text.literal("Selling Bin - Emeralds"),
//                packSource,
//                false,
//                ResourcePackProfile.InsertionPosition.TOP
//        );
//
//        event.addPackFinders(
//                SellingBin.rl("built_in_datapacks/selling_bin_sellable_foods"),
//                ResourceType.SERVER_DATA,
//                Text.literal("Selling Bin - Foods"),
//                packSource,
//                false,
//                ResourcePackProfile.InsertionPosition.TOP
//        );
//
//        event.addPackFinders(
//                SellingBin.rl("built_in_datapacks/selling_bin_sellable_enchanted_books"),
//                ResourceType.SERVER_DATA,
//                Text.literal("Selling Bin - Enchanted Books"),
//                packSource,
//                false,
//                ResourcePackProfile.InsertionPosition.TOP
//        );
//
//        event.addPackFinders(
//                SellingBin.rl("built_in_datapacks/selling_bin_quality_foods_compat"),
//                ResourceType.SERVER_DATA,
//                Text.literal("Selling Bin - Quality Food"),
//                packSource,
//                false,
//                ResourcePackProfile.InsertionPosition.TOP
//        );
//    }

    public static class DefaultPackSource implements ResourcePackSource
    {
        @Override
        public Text decorate(Text name)
        {
            return name;
        }

        @Override
        public boolean canBeEnabledLater()
        {
            return false;
        }
    }
}
