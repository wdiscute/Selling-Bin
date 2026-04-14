package com.wdiscute.sellingbin;

import com.mojang.logging.LogUtils;
import com.wdiscute.sellingbin.processors.AbstractProcessor;
import com.wdiscute.sellingbin.processors.SBProcessors;
import com.wdiscute.sellingbin.registry.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import org.slf4j.Logger;

import java.util.Random;

@Mod(SellingBin.MOD_ID)
public class SellingBin
{
    public static final String MOD_ID = "selling_bin";
    public static final Random r = new Random();
    public static final Logger LOGGER = LogUtils.getLogger();

    //resource keys
    public static final ResourceKey<Registry<AbstractProcessor>> SELLING_BIN =
            ResourceKey.createRegistryKey(SellingBin.rl("selling_bin"));

    //registry
    public static IForgeRegistry<AbstractProcessor> SELLING_BIN_REGISTRY = null;

    public static ResourceLocation rl(String s)
    {
        return new ResourceLocation(SellingBin.MOD_ID, s);
    }

    public static ResourceLocation rl(String ns, String path)
    {
        return new ResourceLocation(ns, path);
    }

    public SellingBin()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        SBBlocks.register(modEventBus);
        SBBlockEntities.register(modEventBus);
        SBMenuTypes.register(modEventBus);
        SBProcessors.register(modEventBus);
       // SBItemPredicate.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, SBConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SBConfig.SPEC_SERVER);
    }
}
