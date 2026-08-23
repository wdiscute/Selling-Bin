package com.wdiscute.sellingbin.registry;

import com.wdiscute.sellingbin.SellingBin;
import net.mcexpanded.fancytabsections.FancyTabSections;
import net.mcexpanded.fancytabsections.Section.SectionColored;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;

import java.util.ArrayList;
import java.util.List;

public interface SBCreativeModeTab
{
    static void register(IEventBus eventBus)
    {
        //register creative mode tab
        FancyTabSections.registerCreativeModeTab(eventBus, SellingBin.rl("selling_bin"), SBBlocks.SELLING_BIN_ITEM);

        //Must Have
        FancyTabSections.addSection(SellingBin.rl("selling_bin"),
                new SectionColored(SellingBin.rl("bin"))
                        .setBannerColor(0xff8dc53b)
                        .setCentered(true)
                        .add(SBBlocks.SELLING_BIN)
        );

        FancyTabSections.addSection(SellingBin.rl("selling_bin"),
                new SectionColored(SellingBin.rl("currencies"))
                        .setBannerColor(0xffd3cf68)
                        .setCentered(true)
                        .add((registry) ->
                        {
                            List<ItemStack> currencies = new ArrayList<>();
                            for (Item item : BuiltInRegistries.ITEM)
                                if(SBDataMaps.get(item, SBDataMaps.SELLING_BIN_CURRENCIES) != null)
                                    currencies.add(item.getDefaultInstance());

                            return currencies;
                        })
        );

        FancyTabSections.addSection(SellingBin.rl("selling_bin"),
                new SectionColored(SellingBin.rl("sellables"))
                        .setBannerColor(0xff7592d7)
                        .setCentered(true)
                        .add((registry) ->
                        {
                            List<ItemStack> sellables = new ArrayList<>();
                            for (Item item : BuiltInRegistries.ITEM)
                                if(SBDataMaps.get(item, SBDataMaps.SELLING_BIN_VALUE) != null)
                                    sellables.add(item.getDefaultInstance());

                            return sellables;
                        })
        );
    }
}
