package com.wdiscute.sellingbin.registry;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.bin.SellingBinMenu;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

public interface SBMenuTypes
{

    ScreenHandlerType<SellingBinMenu> SELLING_BIN_MENU = Registry.register(Registries.SCREEN_HANDLER,
            Identifier.of(SellingBin.MOD_ID, "selling_bin_menu"), new ExtendedScreenHandlerType<>(SellingBinMenu::new, BlockPos.PACKET_CODEC));

    static void init()
    {

    }
}
