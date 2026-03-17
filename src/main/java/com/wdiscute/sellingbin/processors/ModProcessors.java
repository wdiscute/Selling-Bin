package com.wdiscute.sellingbin.processors;

import com.wdiscute.sellingbin.SellingBin;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModProcessors
{
    public static final DeferredRegister<AbstractProcessor> SELLING_BIN_PROCESSORS =
            DeferredRegister.create(SellingBin.SELLING_BIN_REGISTRY, SellingBin.MOD_ID);

    public static final DeferredHolder<AbstractProcessor, AbstractProcessor> EMPTY_PROCESSOR = register("empty_processor", EmptyProcessor::new);
    public static final DeferredHolder<AbstractProcessor, AbstractProcessor> DURABILITY = register("durability_processor", DurabilityProcessor::new);
    public static final DeferredHolder<AbstractProcessor, AbstractProcessor> FOOD_PROCESSOR = register("food_processor", FoodProcessor::new);
    public static DeferredHolder<AbstractProcessor, AbstractProcessor> ENCHANTMENTS_PROCESSOR = register("enchantments_processor", FoodProcessor::new);
    public static DeferredHolder<AbstractProcessor, AbstractProcessor> QUALITY_FOODS_PROCESSOR;

    public static DeferredHolder<AbstractProcessor, AbstractProcessor> register(String name, Supplier<AbstractProcessor> sup)
    {
        return SELLING_BIN_PROCESSORS.register(name, sup);
    }

    public static void registerOptionals()
    {
        if (ModList.get().isLoaded("quality_food"))
            QUALITY_FOODS_PROCESSOR = register("quality_foods_processor", QualityFoodsProcessor::new);
    }

    public static void register(IEventBus eventBus)
    {
        registerOptionals();
        SELLING_BIN_PROCESSORS.register(eventBus);
    }

}
