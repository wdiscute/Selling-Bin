package com.wdiscute.sellingbin.event;

import com.wdiscute.sellingbin.SBConfig;
import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.bin.Currency;
import com.wdiscute.sellingbin.bin.SellingBinBlockEntity;
import com.wdiscute.sellingbin.bin.SellingBinScreen;
import com.wdiscute.sellingbin.registry.SBBlocks;
import com.wdiscute.sellingbin.registry.SBMenuTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class SBClientEvents implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        HandledScreens.register(SBMenuTypes.SELLING_BIN_MENU, SellingBinScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlock(SBBlocks.SELLING_BIN, RenderLayer.getCutout());

        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, comp) ->
        {
            //selling bin info
            if (Screen.hasShiftDown() || SBConfig.config.always_show)
            {
                int value = Currency.calculateValueFromSingleStack(stack, MinecraftClient.getInstance().player);
                if (value > 0)
                {
                    ClientWorld world = MinecraftClient.getInstance().world;
                    if (world == null) return;
                    MutableText literal = Text.literal(Currency.getStringFromValue(value));
                    if (stack.getCount() > 1)
                        literal.append(Text.literal(" (" + Currency.getStringFromValue(value * stack.getCount()) + ")"));
                    comp.add(1, literal.formatted(Formatting.DARK_GRAY));
                }
            }
        });

    }
}
