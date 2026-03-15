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
    DeferredHolder<AbstractProcessor, AbstractProcessor > FOOD_PROCESSOR = register("food_processor", FoodProcessor::new);
    DeferredHolder<AbstractProcessor, AbstractProcessor > ENCHANTMENTS_PROCESSOR = register("enchantments_processor", EnchantmentProcessor::new);

    static DeferredHolder<AbstractProcessor, AbstractProcessor> register(String name, Supplier<AbstractProcessor> sup)
    {
        return SELLING_BIN_PROCESSORS.register(name, sup);
    }

    static void register(IEventBus eventBus)
    {
        SELLING_BIN_PROCESSORS.register(eventBus);
    }

}
