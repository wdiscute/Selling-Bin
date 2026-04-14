package com.wdiscute.sellingbin.event;

import com.wdiscute.sellingbin.registry.SBMenuTypes;
import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.bin.SellingBinScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SellingBin.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SBClientEvents
{
    @SubscribeEvent
    public static void registerScreens(FMLClientSetupEvent event) {
        MenuScreens.register(SBMenuTypes.SELLING_BIN_MENU.get(), SellingBinScreen::new);
    }
}
