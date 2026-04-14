package com.wdiscute.sellingbin.registry;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.bin.SellingBinMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.nikdo53.neobackports.registry.DeferredHolder;
import net.nikdo53.neobackports.registry.DeferredRegisterTyped;

import java.util.function.Supplier;

public interface SBMenuTypes
{
    DeferredRegisterTyped<MenuType<?>> MENUS =
            DeferredRegisterTyped.create(Registries.MENU, SellingBin.MOD_ID);

    Supplier<MenuType<SellingBinMenu>> SELLING_BIN_MENU =
            registerMenuType("selling_bin_menu", SellingBinMenu::new);

    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name,
                                                                                                               IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
