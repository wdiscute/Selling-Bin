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
import net.minecraft.util.Identifier;
import net.nikdo53.datamapsfabric.datamaps.RegisterDataMapTypesEvent;
import net.nikdo53.datamapsfabric.event.FabricEvents;
import net.nikdo53.datamapsfabric.test.TestDataMaps;
import org.slf4j.Logger;

import java.util.Random;

public class SellingBin implements ModInitializer
{
    public static final String MOD_ID = "selling_bin";
    public static final Random r = new Random();
    public static final Logger LOGGER = LogUtils.getLogger();

    //resource keys
    public static final RegistryKey<Registry<AbstractProcessor>> SELLING_BIN =
            RegistryKey.ofRegistry(Identifier.of(MOD_ID, "selling_bin"));

    //registry
    public static final Registry<AbstractProcessor> SELLING_BIN_REGISTRY = FabricRegistryBuilder.createSimple(SELLING_BIN)
            .attribute(RegistryAttribute.SYNCED)
            .buildAndRegister();


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
        SBBlockEntities.init();
        SBMenuTypes.init();
        SBProcessors.init();
        SBItemPredicate.init();
        SBDataMaps.init();

        RegisterDataMapTypesEvent.EVENT.register(SellingBin::onRegisterDataMapTypes);

        //modContainer.registerConfig(ModConfig.Type.CLIENT, SBConfig.SPEC);
        //modContainer.registerConfig(ModConfig.Type.SERVER, SBConfig.SPEC_SERVER);
    }

    private static void onRegisterDataMapTypes(RegisterDataMapTypesEvent event)
    {
        event.register(SBDataMaps.SELLING_BIN_CURRENCIES);
        event.register(SBDataMaps.SELLING_BIN_VALUE);
    }

//    public static class Client
//    {
//        public Client(ModContainer modContainer)
//        {
//            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
//        }
//    }
}
