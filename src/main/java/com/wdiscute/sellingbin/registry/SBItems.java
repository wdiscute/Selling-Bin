package com.wdiscute.sellingbin.registry;

import com.wdiscute.sellingbin.SellingBin;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface SBItems
{
    DeferredRegister.Items ITEMS = DeferredRegister.createItems(SellingBin.MOD_ID);


    static void register(IEventBus modEventBus)
    {
        ITEMS.register(modEventBus);
    }
}
