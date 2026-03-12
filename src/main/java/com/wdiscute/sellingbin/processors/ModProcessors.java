package com.wdiscute.sellingbin.processors;

import com.wdiscute.sellingbin.SellingBin;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface ModProcessors
{
    DeferredRegister<AbstractProcessor> SELLING_BIN_PROCESSORS =
            DeferredRegister.create(SellingBin.SELLING_BIN_REGISTRY, SellingBin.MOD_ID);

    DeferredHolder<AbstractProcessor, AbstractProcessor> DURABILITY = register("durability_processor", DurabilityProcessor::new);
    //DeferredHolder<AbstractSellingBinProcessor, AbstractSellingBinProcessor> SHULKER_BOX = register("shulker_box_processor", ShulkerBoxProcessor::new);

    static DeferredHolder<AbstractProcessor, AbstractProcessor> register(String name, Supplier<AbstractProcessor> sup)
    {
        return SELLING_BIN_PROCESSORS.register(name, sup);
    }

    static void register(IEventBus eventBus)
    {
        SELLING_BIN_PROCESSORS.register(eventBus);
    }

}
