package com.wdiscute.sellingbin;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface ModItems
{
    DeferredRegister.Items ITEMS = DeferredRegister.createItems(SellingBin.MOD_ID);


    static void register(IEventBus modEventBus)
    {
        ITEMS.register(modEventBus);
    }
}
