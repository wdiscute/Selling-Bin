package com.wdiscute.sellingbin.bin;

import com.wdiscute.sellingbin.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.nikdo53.tinymultiblocklib.block.AbstractMultiBlock;
import net.nikdo53.tinymultiblocklib.block.IMultiBlock;
import net.nikdo53.tinymultiblocklib.block.IPreviewableMultiblock;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SellingBinBlock extends AbstractMultiBlock implements IPreviewableMultiblock, WorldlyContainerHolder
{

    public SellingBinBlock()
    {
        super(BlockBehaviour.Properties.of()
                .noOcclusion()
                .destroyTime(2)
        );
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType)
    {
        if (!state.getValue(AbstractMultiBlock.CENTER)) return null;
        return TickableBlockEntity.getTicketHelper(level);
    }

    @Override
    protected float getShadeBrightness(BlockState p_308911_, BlockGetter p_308952_, BlockPos p_308918_)
    {
        return 1.0F;
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState p_309084_, BlockGetter p_309133_, BlockPos p_309097_)
    {
        return true;
    }

    @Override
    public List<BlockPos> makeFullBlockShape(Level level, BlockPos center, BlockState blockState, @Nullable BlockEntity blockEntity, @Nullable Direction direction)
    {
        assert direction != null;
        return List.of(center, center.relative(direction.getClockWise()));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return super.getStateForPlacement(context);
    }

    @Override
    public BlockState getDefaultStateForPreviews(Direction direction)
    {
        return IPreviewableMultiblock.super.getDefaultStateForPreviews(direction);
    }

    @Override
    public RenderShape getMultiblockRenderShape(BlockState state, boolean c)
    {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable DirectionProperty getDirectionProperty()
    {
        return FACING;
    }

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
    }

    private void playSound(Level level, BlockPos bp, BlockState state, SoundEvent sound)
    {
        Vec3i vec3i = state.getValue(HorizontalDirectionalBlock.FACING).getNormal();
        double d0 = (double) bp.getX() + 0.5 + (double) vec3i.getX() / 2.0;
        double d1 = (double) bp.getY() + 0.5 + (double) vec3i.getY() / 2.0;
        double d2 = (double) bp.getZ() + 0.5 + (double) vec3i.getZ() / 2.0;
        level.playSound(null, d0, d1, d2, sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack handStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        int value = Currency.calculateValueFromSingleStack(handStack);
        if (value > 0)
        {
            WorldlyContainer container = getContainer(state, level, pos);

            if (container != null)
            {
                ItemStack inside = container.getItem(0);

                if (inside.isEmpty())
                {
                    container.setItem(0, handStack.copy());
                    player.setItemInHand(hand, ItemStack.EMPTY);

                    if(!level.isClientSide) playSound(level, pos, state, SoundEvents.ITEM_PICKUP);
                    return ItemInteractionResult.SUCCESS;
                }
                else
                {
                    int fitInsideCount = inside.getMaxStackSize();
                    fitInsideCount -= inside.getCount();

                    int amountTransferring = Math.min(handStack.getCount(), fitInsideCount);
                    if(inside.isStackable() && ItemStack.isSameItemSameComponents(handStack, inside) && fitInsideCount > 0)
                    {
                        handStack.shrink(amountTransferring);
                        inside.grow(amountTransferring);
                        if(!level.isClientSide) playSound(level, pos, state, SoundEvents.ITEM_PICKUP);
                        return ItemInteractionResult.SUCCESS;
                    }
                }

            }


        }
        return super.useItemOn(handStack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {
        BlockPos center = IMultiBlock.getCenter(level, pos);
        if (level.getBlockEntity(center) instanceof SellingBinBlockEntity sbbe && !level.isClientSide)
        {
            player.openMenu(sbbe, center);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        return ModBlockEntities.SELLING_BIN.get().create(blockPos, blockState);
    }

    @Override
    public boolean hasCustomBE()
    {
        return true;
    }

    @Override
    public WorldlyContainer getContainer(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos)
    {
        if (blockState.getValue(AbstractMultiBlock.CENTER))
            if (levelAccessor.getBlockEntity(blockPos) instanceof SellingBinBlockEntity sbbe) return sbbe;

        if (levelAccessor.getBlockEntity(blockPos) instanceof SellingBinBlockEntity notCenter)
        {
            if (levelAccessor.getBlockEntity(blockPos.offset(notCenter.getOffset().multiply(-1))) instanceof SellingBinBlockEntity center)
            {
                return center;
            }
        }
        return null;
    }
}
