package com.wdiscute.sellingbin.event;

import com.wdiscute.sellingbin.bin.SellingBinBlockEntity;
import com.wdiscute.sellingbin.registry.SBBlockEntities;
import com.wdiscute.sellingbin.registry.SBBlocks;
import com.wdiscute.sellingbin.registry.SBDataMaps;
import com.wdiscute.sellingbin.SellingBin;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.registries.NewRegistryEvent;
import net.nikdo53.neobackports.event.RegisterDataMapTypesEvent;
import net.nikdo53.neobackports.registry.ForgeRegistryHelper;

@Mod.EventBusSubscriber(modid = SellingBin.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SBevents
{

    @SubscribeEvent
    public static void addRegistry(NewRegistryEvent event)
    {
        ForgeRegistryHelper.getInstance(SellingBin.SELLING_BIN)
                .create(event, reg -> SellingBin.SELLING_BIN_REGISTRY = reg);

    }

    @SubscribeEvent
    public static void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS)
        {
            event.accept(SBBlocks.SELLING_BIN.get());
        }
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS)
        {
            event.accept(SBBlocks.SELLING_BIN.get());
        }
    }

    @SubscribeEvent
    public static void addCapabilities(RegisterCapabilitiesEvent event)
    {
        //TODO: I have no idea how this works
/*        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, SBBlockEntities.SELLING_BIN.get(),
                (container, side) ->
                {
                    if(container instanceof SellingBinBlockEntity binBlockEntity)
                    {
                        if(binBlockEntity.isCenter())
                        {
                            return new SidedInvWrapper(container, side);
                        }
                        else
                        {
                            BlockEntity blockEntity = binBlockEntity.getLevel().getBlockEntity(binBlockEntity.getCenter());
                            if(blockEntity instanceof SellingBinBlockEntity center)
                            {
                                return new SidedInvWrapper(center, side);
                            }
                        }
                    }
                    return null;
                }
        );*/
    }

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event)
    {
        PackSource packSource = new DefaultPackSource();

        event.addPackFinders(
                SellingBin.rl("built_in_datapacks/selling_bin_currency_emeralds"),
                PackType.SERVER_DATA,
                Component.literal("Selling Bin - Emeralds"),
                packSource,
                false,
                Pack.Position.TOP
        );

        event.addPackFinders(
                SellingBin.rl("built_in_datapacks/selling_bin_sellable_foods"),
                PackType.SERVER_DATA,
                Component.literal("Selling Bin - Foods"),
                packSource,
                false,
                Pack.Position.TOP
        );

        event.addPackFinders(
                SellingBin.rl("built_in_datapacks/selling_bin_sellable_enchanted_books"),
                PackType.SERVER_DATA,
                Component.literal("Selling Bin - Enchanted Books"),
                packSource,
                false,
                Pack.Position.TOP
        );

        event.addPackFinders(
                SellingBin.rl("built_in_datapacks/selling_bin_quality_foods_compat"),
                PackType.SERVER_DATA,
                Component.literal("Selling Bin - Quality Food"),
                packSource,
                false,
                Pack.Position.TOP
        );

        event.addPackFinders(
                SellingBin.rl("built_in_datapacks/selling_bin_vinery_compat"),
                PackType.SERVER_DATA,
                Component.literal("Selling Bin - Vinery Wine"),
                packSource,
                false,
                Pack.Position.TOP
        );

        event.addPackFinders(
                SellingBin.rl("built_in_datapacks/selling_bin_brewery_compat"),
                PackType.SERVER_DATA,
                Component.literal("Selling Bin - Brewery Beer"),
                packSource,
                false,
                Pack.Position.TOP
        );

        event.addPackFinders(
                SellingBin.rl("built_in_datapacks/selling_bin_currency_numismatics_coins"),
                PackType.SERVER_DATA,
                Component.literal("Selling Bin - Numismatics"),
                packSource,
                false,
                Pack.Position.TOP
        );

        event.addPackFinders(
                SellingBin.rl("built_in_datapacks/selling_bin_currency_lightmanscurrency_compat"),
                PackType.SERVER_DATA,
                Component.literal("Selling Bin - Lightmans's Currency"),
                packSource,
                false,
                Pack.Position.TOP
        );
    }


    @SubscribeEvent
    public static void registerAttributed(RegisterDataMapTypesEvent event)
    {
        event.register(SBDataMaps.SELLING_BIN_VALUE);
        event.register(SBDataMaps.SELLING_BIN_CURRENCIES);
    }

    public static class DefaultPackSource implements PackSource
    {
        @Override
        public Component decorate(Component name)
        {
            return name;
        }

        @Override
        public boolean shouldAddAutomatically()
        {
            return false;
        }
    }

}
