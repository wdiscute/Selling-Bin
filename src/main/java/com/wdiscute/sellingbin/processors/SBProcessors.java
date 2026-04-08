package com.wdiscute.sellingbin.processors;

import com.wdiscute.sellingbin.SellingBin;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.ModStatus;

public class SBProcessors
{
    public static final Identifier EMPTY_PROCESSOR = register("empty_processor", new EmptyProcessor());
    public static final Identifier DURABILITY = register("durability_processor", new DurabilityProcessor());
    public static final Identifier FOOD_PROCESSOR = register("food_processor", new FoodProcessor());
    public static Identifier ENCHANTMENTS_PROCESSOR = register("enchantments_processor", new EnchantmentProcessor());
    public static Identifier QUALITY_FOODS_PROCESSOR;
    public static Identifier WINE_AGE_PROCESSOR;
    public static Identifier BEER_QUALITY_PROCESSOR;

    public static void registerOptionals()
    {
        if (FabricLoader.getInstance().isModLoaded("quality_food"))
            QUALITY_FOODS_PROCESSOR = register("quality_foods_processor", new QualityFoodsProcessor());

        if (FabricLoader.getInstance().isModLoaded("vinery"))
            WINE_AGE_PROCESSOR = register("wine_age_processor", new WineAgeProcessor());

        if (FabricLoader.getInstance().isModLoaded("brewery"))
            BEER_QUALITY_PROCESSOR = register("beer_quality_processor", new BeerQualityProcessor());
    }

    public static Identifier register(String name, AbstractProcessor abstractProcessor)
    {
        Registry.register(SellingBin.SELLING_BIN_REGISTRY, name, abstractProcessor);
        return SellingBin.rl(name);
    }

    public static void init()
    {
        registerOptionals();
    }

}
