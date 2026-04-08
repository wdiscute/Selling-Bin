package com.wdiscute.sellingbin.processors;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.wdiscute.sellingbin.registry.SBDataMaps;
import com.wdiscute.sellingbin.SellingBin;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractProcessor
{
    public static final Codec<AbstractProcessor> ABSTRACT_PROCESSOR_CODEC = Identifier.CODEC
            .dispatch(AbstractProcessor::getRegistryHolderOrThrow,
                    loc ->
                    {
                        if(SellingBin.SELLING_BIN_REGISTRY.get(loc) == null)
                        {
                            LogUtils.getLogger().error("Selling Bin Processor {} is not registered! " +
                                    "Make sure it's not dependent on another mod, and that you spelt the name correctly. " +
                                    "Using empty processor instead.", loc);
                            return EmptyProcessor.CODEC;
                        }
                        return SellingBin.SELLING_BIN_REGISTRY.get(loc).getCodecOrThrow();
                    });

    public static final Codec<List<AbstractProcessor>> ABSTRACT_PROCESSOR_CODEC_LIST = ABSTRACT_PROCESSOR_CODEC.listOf();

    public abstract MapCodec<? extends AbstractProcessor> codec();

    public abstract Identifier getIdentifier();

    abstract public boolean showDescriptionOnEmi();

    abstract public List<Text> getDescription();

    public Identifier getRegistryHolderOrThrow()
    {
        var Identifier = getIdentifier();
        if (Identifier == null)
        {
            throw new IllegalStateException("Selling Bin Processor " + this + " does not have an identifier!");
        }
        return Identifier;
    }

    public MapCodec<? extends AbstractProcessor> getCodecOrThrow()
    {
        var codec = codec();
        if (codec == null)
        {
            throw new IllegalStateException("Selling Bin Processor " + this + " does not have a codec!");
        }
        return codec;
    }


    abstract public int addValue(int baseValue, int currentValue, ItemStack itemStack, BlockEntity blockEntity, PlayerEntity player);

    public boolean shouldCancelShrink(ItemStack itemStack)
    {
        return false;
    }

    public void onSellStart(ItemStack itemStack)
    {
    }

    public void onSellComplete(ItemStack itemStack)
    {
    }

    public static SBDataMaps.ItemValue createEmpty(int baseValue)
    {
        return new SBDataMaps.ItemValue(baseValue, List.of());
    }

    public static SBDataMaps.ItemValue createEmpty()
    {
        return new SBDataMaps.ItemValue(0, List.of());
    }

    public SBDataMaps.ItemValue create(int baseValue)
    {
        return new SBDataMaps.ItemValue(baseValue, List.of(this));
    }

    public boolean canSell(ItemStack itemStack, @Nullable BlockEntity blockEntity, @Nullable PlayerEntity player)
    {
        return true;
    }
}
