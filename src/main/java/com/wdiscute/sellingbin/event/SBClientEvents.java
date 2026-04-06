package com.wdiscute.sellingbin.event;

import com.wdiscute.sellingbin.SBConfig;
import com.wdiscute.sellingbin.bin.Currency;
import com.wdiscute.sellingbin.bin.SellingBinScreen;
import com.wdiscute.sellingbin.registry.SBMenuTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class SBClientEvents implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        HandledScreens.register(SBMenuTypes.SELLING_BIN_MENU, SellingBinScreen::new);

        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, comp) ->
        {
            //selling bin info
            if (Screen.hasShiftDown() || SBConfig.ALWAYS_SHOW_SELLING_BIN_PRICE)
            {
                int value = Currency.calculateValueFromSingleStack(stack, MinecraftClient.getInstance().player);
                if (value > 0)
                {
                    ClientWorld world = MinecraftClient.getInstance().world;
                    if(world == null) return;
                    MutableText literal = Text.literal(Currency.getStringFromValue(value, world));
                    if (stack.getCount() > 1)
                        literal.append(Text.literal(" (" + Currency.getStringFromValue(value * stack.getCount(), world) + ")"));
                    comp.add(1, literal.formatted(Formatting.DARK_GRAY));
                }
            }


        });

    }


}
