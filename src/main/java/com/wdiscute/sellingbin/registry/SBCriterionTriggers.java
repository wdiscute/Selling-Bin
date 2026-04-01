package com.wdiscute.sellingbin.registry;

import com.wdiscute.sellingbin.SellingBin;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface SBCriterionTriggers
{
    DeferredRegister<CriterionTrigger<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, SellingBin.MOD_ID);


    static void register(IEventBus eventBus)
    {
        REGISTRY.register(eventBus);
    }
}
