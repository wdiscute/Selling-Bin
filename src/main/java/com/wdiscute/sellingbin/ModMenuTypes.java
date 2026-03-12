package com.wdiscute.sellingbin;

import com.wdiscute.sellingbin.bin.SellingBinMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public interface ModMenuTypes
{
    DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, SellingBin.MOD_ID);

    Supplier<MenuType<SellingBinMenu>> SELLING_BIN_MENU =
            registerMenuType("selling_bin_menu", SellingBinMenu::new);

    private static <T extends AbstractContainerMenu>DeferredHolder<MenuType<?>, MenuType<T>> registerMenuType(String name,
                                                                                                              IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
