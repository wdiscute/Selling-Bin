package com.wdiscute.sellingbin.datagen;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.processors.*;
import com.wdiscute.sellingbin.registry.SBDataMaps;
import dev.ithundxr.createnumismatics.content.backend.Coin;
import dev.ithundxr.createnumismatics.registry.NumismaticsItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DataMapProvider;

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
            bin.add(Tags.Items.FOODS, new FoodProcessor().create(10), false);

            bin.add(Items.GOLDEN_APPLE.builtInRegistryHolder(), AbstractProcessor.createEmpty(200), false);
            bin.add(Items.ENCHANTED_GOLDEN_APPLE.builtInRegistryHolder(), AbstractProcessor.createEmpty(3700), false);
            bin.add(Items.HONEY_BOTTLE.builtInRegistryHolder(), AbstractProcessor.createEmpty(150), false);
            bin.add(Items.CAKE.builtInRegistryHolder(), AbstractProcessor.createEmpty(200), false);

            bin.add(Items.OMINOUS_BOTTLE.builtInRegistryHolder(), AbstractProcessor.createEmpty(0), false);
            bin.add(Items.OMINOUS_BOTTLE.builtInRegistryHolder(), AbstractProcessor.createEmpty(0), false);
        }

        //Quality foods built-in datapack
        if (false)
        {
            Map<ResourceLocation, Float> qualities = new HashMap<>();

            qualities.put(ResourceLocation.fromNamespaceAndPath("quality_food", "diamond"), 2f);
            qualities.put(ResourceLocation.fromNamespaceAndPath("quality_food", "gold"), 1.5f);
            qualities.put(ResourceLocation.fromNamespaceAndPath("quality_food", "iron"), 1.25f);

            bin.add(Tags.Items.FOODS, new SBDataMaps.ItemValue(10, List.of(
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

            enchants.put(getHolderEnchant(Enchantments.AQUA_AFFINITY), 40);
            enchants.put(getHolderEnchant(Enchantments.BANE_OF_ARTHROPODS), 20);
            enchants.put(getHolderEnchant(Enchantments.BINDING_CURSE), 500);
            enchants.put(getHolderEnchant(Enchantments.BLAST_PROTECTION), 30);
            enchants.put(getHolderEnchant(Enchantments.BREACH), 40);
            enchants.put(getHolderEnchant(Enchantments.CHANNELING), 300);
            enchants.put(getHolderEnchant(Enchantments.DENSITY), 80);
            enchants.put(getHolderEnchant(Enchantments.EFFICIENCY), 200);
            enchants.put(getHolderEnchant(Enchantments.FEATHER_FALLING), 100);
            enchants.put(getHolderEnchant(Enchantments.FIRE_ASPECT), 300);
            enchants.put(getHolderEnchant(Enchantments.FIRE_PROTECTION), 30);
            enchants.put(getHolderEnchant(Enchantments.FLAME), 50);
            enchants.put(getHolderEnchant(Enchantments.FORTUNE), 500);
            enchants.put(getHolderEnchant(Enchantments.FROST_WALKER), 40);
            enchants.put(getHolderEnchant(Enchantments.IMPALING), 70);
            enchants.put(getHolderEnchant(Enchantments.INFINITY), 300);
            enchants.put(getHolderEnchant(Enchantments.KNOCKBACK), 70);
            enchants.put(getHolderEnchant(Enchantments.LOOTING), 700);
            enchants.put(getHolderEnchant(Enchantments.LOYALTY), 100);
            enchants.put(getHolderEnchant(Enchantments.LUCK_OF_THE_SEA), 30);
            enchants.put(getHolderEnchant(Enchantments.LURE), 50);
            enchants.put(getHolderEnchant(Enchantments.MENDING), 500);
            enchants.put(getHolderEnchant(Enchantments.MULTISHOT), 50);
            enchants.put(getHolderEnchant(Enchantments.PIERCING), 70);
            enchants.put(getHolderEnchant(Enchantments.POWER), 70);
            enchants.put(getHolderEnchant(Enchantments.PROJECTILE_PROTECTION), 20);
            enchants.put(getHolderEnchant(Enchantments.PROTECTION), 200);
            enchants.put(getHolderEnchant(Enchantments.PUNCH), 30);
            enchants.put(getHolderEnchant(Enchantments.QUICK_CHARGE), 60);
            enchants.put(getHolderEnchant(Enchantments.RESPIRATION), 90);
            enchants.put(getHolderEnchant(Enchantments.RIPTIDE), 40);
            enchants.put(getHolderEnchant(Enchantments.SHARPNESS), 240);
            enchants.put(getHolderEnchant(Enchantments.SILK_TOUCH), 500);
            enchants.put(getHolderEnchant(Enchantments.SMITE), 240);
            enchants.put(getHolderEnchant(Enchantments.SOUL_SPEED), 130);
            enchants.put(getHolderEnchant(Enchantments.SWEEPING_EDGE), 150);
            enchants.put(getHolderEnchant(Enchantments.SWIFT_SNEAK), 700);
            enchants.put(getHolderEnchant(Enchantments.THORNS), 70);
            enchants.put(getHolderEnchant(Enchantments.UNBREAKING), 300);
            enchants.put(getHolderEnchant(Enchantments.VANISHING_CURSE), 500);
            enchants.put(getHolderEnchant(Enchantments.WIND_BURST), 50);


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


    Holder<Enchantment> getHolderEnchant(ResourceKey<Enchantment> rk)
    {
        Optional<Holder.Reference<Enchantment>> registryHolder =
                holderProvider.asGetterLookup().lookup(Registries.ENCHANTMENT).get().get(rk);

        Holder<Enchantment> delegate = registryHolder.get().getDelegate();
        return delegate;
    }
}
