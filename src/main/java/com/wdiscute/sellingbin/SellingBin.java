package com.wdiscute.sellingbin;

import com.mojang.logging.LogUtils;
import com.wdiscute.sellingbin.processors.AbstractProcessor;
import com.wdiscute.sellingbin.processors.SBProcessors;
import com.wdiscute.sellingbin.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerSyncHandler;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import java.util.Random;

public class SellingBin implements ModInitializer
{
    public static final String MOD_ID = "selling_bin";
    public static final Random r = new Random();
    public static final Logger LOGGER = LogUtils.getLogger();

    //resource keys
    public static final ResourceKey<Registry<AbstractProcessor>> SELLING_BIN =
            ResourceKey.createRegistryKey(SellingBin.rl("selling_bin"));

    //registry
    public static final Registry<AbstractProcessor> SELLING_BIN_REGISTRY = new RegistryBuilder<>(SELLING_BIN)
            .sync(true)
            .create();

    public static Identifier rl(String s)
    {
        return Identifier.of(SellingBin.MOD_ID, s);
    }

    public static Identifier rl(String ns, String path)
    {
        return Identifier.of(ns, path);
    }

    @Override
    public void onInitialize()
    {
        SBBlocks.init();
        SBMenuTypes.init();
        //SBBlockEntities.register(modEventBus);
        //SBMenuTypes.register(modEventBus);
        //SBProcessors.register(modEventBus);
        //SBCriterionTriggers.register(modEventBus);
        //SBItemPredicate.register(modEventBus);

        //modContainer.registerConfig(ModConfig.Type.CLIENT, SBConfig.SPEC);
        //modContainer.registerConfig(ModConfig.Type.SERVER, SBConfig.SPEC_SERVER);




        RegistryKey<Registry<AbstractProcessor>> registryKey = RegistryKey.ofRegistry(Identifier.of(MOD_ID, "selling_bin"));
        Registry<AbstractProcessor> registry = FabricRegistryBuilder.createSimple(registryKey)
                .attribute(RegistryAttribute.SYNCED)
                .buildAndRegister();
    }

//    public static class Client
//    {
//        public Client(ModContainer modContainer)
//        {
//            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
//        }
//    }
}
