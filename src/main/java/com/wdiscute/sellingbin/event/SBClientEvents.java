package com.wdiscute.sellingbin.event;

import com.wdiscute.sellingbin.registry.SBMenuTypes;
import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.bin.SellingBinScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;

@EventBusSubscriber(modid = SellingBin.MOD_ID, value = Dist.CLIENT)
public class SBClientEvents
{
    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event)
    {
        event.register(SBMenuTypes.SELLING_BIN_MENU.get(), SellingBinScreen::new);
    }
}
