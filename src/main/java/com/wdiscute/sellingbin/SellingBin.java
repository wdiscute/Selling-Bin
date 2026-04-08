package com.wdiscute.sellingbin;

import com.mojang.logging.LogUtils;
import com.wdiscute.sellingbin.processors.AbstractProcessor;
import com.wdiscute.sellingbin.processors.SBProcessors;
import com.wdiscute.sellingbin.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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

        ResourceManagerHelper.registerBuiltinResourcePack(
                SellingBin.rl("selling_bin_currency_emeralds"),
                FabricLoader.getInstance().getModContainer("selling_bin").get(),
                Text.literal("Selling Bin - Emeralds"),
                ResourcePackActivationType.NORMAL
        );

        ResourceManagerHelper.registerBuiltinResourcePack(
                SellingBin.rl("selling_bin_sellable_foods"),
                FabricLoader.getInstance().getModContainer("selling_bin").get(),
                Text.literal("Selling Bin - Foods"),
                ResourcePackActivationType.NORMAL
        );

        ResourceManagerHelper.registerBuiltinResourcePack(
                SellingBin.rl("selling_bin_sellable_enchanted_books"),
                FabricLoader.getInstance().getModContainer("selling_bin").get(),
                Text.literal("Selling Bin - Enchanted Books"),
                ResourcePackActivationType.NORMAL
        );

        ResourceManagerHelper.registerBuiltinResourcePack(
                SellingBin.rl("selling_bin_quality_foods_compat"),
                FabricLoader.getInstance().getModContainer("selling_bin").get(),
                Text.literal("Selling Bin - Quality Food"),
                ResourcePackActivationType.NORMAL
        );

        ResourceManagerHelper.registerBuiltinResourcePack(
                SellingBin.rl("selling_bin_vinery_compat"),
                FabricLoader.getInstance().getModContainer("selling_bin").get(),
                Text.literal("Selling Bin - Vinery Wine"),
                ResourcePackActivationType.NORMAL
        );

        ResourceManagerHelper.registerBuiltinResourcePack(
                SellingBin.rl("selling_bin_brewery_compat"),
                FabricLoader.getInstance().getModContainer("selling_bin").get(),
                Text.literal("Selling Bin - Brewery Beer"),
                ResourcePackActivationType.NORMAL
        );

        ResourceManagerHelper.registerBuiltinResourcePack(
                SellingBin.rl("selling_bin_currency_numismatics_coins"),
                FabricLoader.getInstance().getModContainer("selling_bin").get(),
                Text.literal("Selling Bin - Numismatics"),
                ResourcePackActivationType.NORMAL
        );

        ResourceManagerHelper.registerBuiltinResourcePack(
                SellingBin.rl("selling_bin_currency_lightmanscurrency_compat"),
                FabricLoader.getInstance().getModContainer("selling_bin").get(),
                Text.literal("Selling Bin - Lightmans's Currency"),
                ResourcePackActivationType.NORMAL
        );

        //modContainer.registerConfig(ModConfig.Type.CLIENT, SBConfig.SPEC);
        //modContainer.registerConfig(ModConfig.Type.SERVER, SBConfig.SPEC_SERVER);
    }

//    public static class Client
//    {
//        public Client(ModContainer modContainer)
//        {
//            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
//        }
//    }
}
