package com.wdiscute.sellingbin.event;

import com.wdiscute.sellingbin.Config;
import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.bin.Currency;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

@EventBusSubscriber(modid = SellingBin.MOD_ID, value = Dist.CLIENT)
public class TooltipEvents
{
    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event)
    {
        List<Component> comp = event.getToolTip();
        ItemStack stack = event.getItemStack();

        //selling bin info
        if (Screen.hasShiftDown() || Config.ALWAYS_SHOW_SELLING_BIN_PRICE.get())
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
