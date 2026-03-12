package com.wdiscute.sellingbin;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface ModCriterionTriggers
{
    DeferredRegister<CriterionTrigger<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, SellingBin.MOD_ID);


    static void register(IEventBus eventBus)
    {
        REGISTRY.register(eventBus);
    }
}
