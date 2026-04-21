package com.wdiscute.sellingbin.processors;

import com.wdiscute.sellingbin.SellingBin;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SBProcessors
{
    public static final DeferredRegister<AbstractProcessor> SELLING_BIN_PROCESSORS =
            DeferredRegister.create(SellingBin.SELLING_BIN_REGISTRY, SellingBin.MOD_ID);

    public static final DeferredHolder<AbstractProcessor, AbstractProcessor> EMPTY_PROCESSOR = register("empty_processor", EmptyProcessor::new);
    public static final DeferredHolder<AbstractProcessor, AbstractProcessor> DURABILITY = register("durability_processor", DurabilityProcessor::new);
    public static final DeferredHolder<AbstractProcessor, AbstractProcessor> FOOD_PROCESSOR = register("food_processor", FoodProcessor::new);
    public static final DeferredHolder<AbstractProcessor, AbstractProcessor> ENCHANTMENTS_PROCESSOR = register("enchantments_processor", EnchantmentProcessor::new);
    public static DeferredHolder<AbstractProcessor, AbstractProcessor> QUALITY_FOODS_PROCESSOR;
    public static DeferredHolder<AbstractProcessor, AbstractProcessor> WINE_AGE_PROCESSOR;
    public static DeferredHolder<AbstractProcessor, AbstractProcessor> BEER_QUALITY_PROCESSOR;

    public static void registerOptionals()
    {
        //if (ModList.get().isLoaded("quality_food"))
            //QUALITY_FOODS_PROCESSOR = register("quality_foods_processor", QualityFoodsProcessor::new);

        //if (ModList.get().isLoaded("vinery"))
            //WINE_AGE_PROCESSOR = register("wine_age_processor", WineAgeProcessor::new);

        //if (ModList.get().isLoaded("brewery"))
            //BEER_QUALITY_PROCESSOR = register("beer_quality_processor", BeerQualityProcessor::new);
    }

    public static DeferredHolder<AbstractProcessor, AbstractProcessor> register(String name, Supplier<AbstractProcessor> sup)
    {
        return SELLING_BIN_PROCESSORS.register(name, sup);
    }

    public static void register(IEventBus eventBus)
    {
        registerOptionals();
        SELLING_BIN_PROCESSORS.register(eventBus);
    }

}
