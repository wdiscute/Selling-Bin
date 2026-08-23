package com.wdiscute.sellingbin.datagen;

import com.wdiscute.sellingbin.SellingBin;
import com.wdiscute.sellingbin.registry.SBBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DGSBBlocksTagsProvider extends BlockTagsProvider
{

    public DGSBBlocksTagsProvider(PackOutput output,
                                  CompletableFuture<HolderLookup.Provider> lookupProvider,
                                  @Nullable ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, SellingBin.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(SBBlocks.SELLING_BIN_BLOCK.get());
    }
}
