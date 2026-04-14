package com.wdiscute.sellingbin.event;

import com.wdiscute.sellingbin.SBConfig;
import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.bin.Currency;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = SellingBin.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SBTooltipEvents
{
    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event)
    {
        List<Component> comp = event.getToolTip();
        ItemStack stack = event.getItemStack();

        //selling bin info
        if (Screen.hasShiftDown() || SBConfig.ALWAYS_SHOW_SELLING_BIN_PRICE.get())
        {
            int value = Currency.calculateValueFromSingleStack(stack, Minecraft.getInstance().player);
            if (value > 0)
            {
                MutableComponent literal = Component.literal(Currency.getStringFromValue(value));
                if (stack.getCount() > 1)
                    literal.append(Component.literal(" (" + Currency.getStringFromValue(value * stack.getCount()) + ")"));
                comp.add(1, literal.withStyle(ChatFormatting.DARK_GRAY));
            }
        }
    }
}
