package com.wdiscute.sellingbin.datagen;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.processors.*;
import com.wdiscute.sellingbin.registry.SBDataMaps;
import dev.ithundxr.createnumismatics.content.backend.Coin;
import dev.ithundxr.createnumismatics.registry.NumismaticsItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import net.nikdo53.neobackports.datagen.DataMapProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class DGSBDataMapsProvider extends DataMapProvider
{
    private HolderLookup.Provider holderProvider;

    protected DGSBDataMapsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider)
    {
        holderProvider = provider;

        var bin = this.builder(SBDataMaps.SELLING_BIN_VALUE);
        var currencies = this.builder(SBDataMaps.SELLING_BIN_CURRENCIES);


        //foods built-in datapack
        if (false)
        {
            bin.add(ItemTags.FOX_FOOD, new FoodProcessor().create(10), false);

            bin.add(Items.GOLDEN_APPLE.builtInRegistryHolder(), AbstractProcessor.createEmpty(200), false);
            bin.add(Items.ENCHANTED_GOLDEN_APPLE.builtInRegistryHolder(), AbstractProcessor.createEmpty(3700), false);
            bin.add(Items.HONEY_BOTTLE.builtInRegistryHolder(), AbstractProcessor.createEmpty(150), false);
            bin.add(Items.CAKE.builtInRegistryHolder(), AbstractProcessor.createEmpty(200), false);

        }

        //Quality foods built-in datapack
        if (false)
        {
            Map<String, Float> qualities = new HashMap<>();

            qualities.put("diamond", 2f);
            qualities.put("gold", 1.5f);
            qualities.put("iron", 1.25f);

            bin.add(ItemTags.FOX_FOOD, new SBDataMaps.ItemValue(10, List.of(
                    new FoodProcessor(),
                    new QualityFoodsProcessor(qualities)
            )), false);
        }


        //Let's do Vinery
        if (false)
        {
            Map<String, Float> ages = new HashMap<>();

            ages.put("1", 1.5f);
            ages.put("2", 3f);
            ages.put("3", 5f);

            bin.add(ItemTags.create(SellingBin.rl("vinery", "red_wine")),
                    new WineAgeProcessor(ages).create(200), false);

            bin.add(ItemTags.create(SellingBin.rl("vinery", "white_wine")),
                    new WineAgeProcessor(ages).create(200), false);
        }

        //Let's do Brewery
        if (false)
        {
            Map<String, Float> ages = new HashMap<>();

            ages.put("1", 1.5f);
            ages.put("2", 3f);
            ages.put("3", 5f);

            bin.add(ItemTags.create(SellingBin.rl("brewery", "beer")),
                    new BeerQualityProcessor(ages).create(200), false);
        }


        //enchanted books built-in datapack
        if (false)
        {

            Map<Holder<Enchantment>, Integer> enchants = new HashMap<>();
            BuiltInRegistries.ENCHANTMENT.forEach(enchantment -> {
                ResourceLocation key = BuiltInRegistries.ENCHANTMENT.getKey(enchantment);
                if (key.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE)){
                    enchants.put(getHolderEnchant(enchantment), 20);
                }
            });


            bin.add(Items.ENCHANTED_BOOK.builtInRegistryHolder(), new EnchantmentProcessor(enchants).create(50), false);
        }

        //durability
        if (false)
        {
            bin.add(Items.STONE_AXE.builtInRegistryHolder(), new DurabilityProcessor().create(200), false);
        }

        //emeralds currency built-in datapack
        if (false)
        {
            currencies.add(Items.EMERALD.builtInRegistryHolder(), 100, false);
            currencies.add(Items.EMERALD_BLOCK.builtInRegistryHolder(), 900, false);
        }

        //Create: Numismatics
        if (false)
        {
            currencies.add(SellingBin.rl("numismatics", "spur"), 10, false);
            currencies.add(SellingBin.rl("numismatics", "bevel"), 80, false);
            currencies.add(SellingBin.rl("numismatics", "sprocket"), 160, false);
            currencies.add(SellingBin.rl("numismatics", "cog"), 640, false);
            currencies.add(SellingBin.rl("numismatics", "crown"), 5120, false);
            currencies.add(SellingBin.rl("numismatics", "sun"), 40960, false);
        }

        //Lightman's Currency
        if (false)
        {
            currencies.add(SellingBin.rl("lightmanscurrency", "coin_iron"), 1, false);
            currencies.add(SellingBin.rl("lightmanscurrency", "coin_gold"), 10, false);
            currencies.add(SellingBin.rl("lightmanscurrency", "coin_emerald"), 100, false);
            currencies.add(SellingBin.rl("lightmanscurrency", "coin_diamond"), 1000, false);
            currencies.add(SellingBin.rl("lightmanscurrency", "coin_netherite"), 10000, false);
        }

    }


    Holder<Enchantment> getHolderEnchant(Enchantment rk)
    {
        ResourceKey<Enchantment> key = ForgeRegistries.ENCHANTMENTS.getResourceKey(rk).get();
        Optional<Holder.Reference<Enchantment>> registryHolder = ForgeRegistries.ENCHANTMENTS.getDelegate(key);

        return registryHolder.get();
    }
}
