package com.wdiscute.sellingbin.bin;



import com.wdiscute.sellingbin.registry.SBBlockEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.nikdo53.tinymultiblocklib.block.AbstractMultiBlock;
import net.nikdo53.tinymultiblocklib.block.IMultiBlock;
import net.nikdo53.tinymultiblocklib.block.IPreviewableMultiblock;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SellingBinBlock extends AbstractMultiBlock implements IPreviewableMultiblock, InventoryProvider
{

    public SellingBinBlock()
    {
        super(AbstractBlock.Settings.create()
                .nonOpaque()
                .hardness(2)
        );
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World level, BlockState state, BlockEntityType<T> blockEntityType)
    {
        if (!state.get(AbstractMultiBlock.CENTER)) return null;
        return TickableBlockEntity.getTicketHelper(level);
    }

    @Override
    protected float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos)
    {
        return 1.0f;
    }

    @Override
    protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos)
    {
        return true;
    }

    @Override
    public List<BlockPos> makeFullBlockShape(World level, BlockPos center, BlockState state, BlockEntity blockEntity, Direction direction)
    {
        assert direction != null;
        return List.of(center, center.offset(direction.rotateYClockwise()));
    }

    @Override
    public BlockState getDefaultStateForPreviews(Direction direction)
    {
        return IPreviewableMultiblock.super.getDefaultStateForPreviews(direction);
    }

    @Override
    public BlockRenderType getMultiblockRenderShape(BlockState state, boolean c)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public @Nullable DirectionProperty getDirectionProperty()
    {
        return FACING;
    }

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
    {
        super.appendProperties(builder);
    }


    private void playSound(World level, BlockPos bp, BlockState state, SoundEvent sound)
    {
        Vec3i vec3i = state.get(HorizontalFacingBlock.FACING).getVector();
        double d0 = (double) bp.getX() + 0.5 + (double) vec3i.getX() / 2.0;
        double d1 = (double) bp.getY() + 0.5 + (double) vec3i.getY() / 2.0;
        double d2 = (double) bp.getZ() + 0.5 + (double) vec3i.getZ() / 2.0;
        level.playSound(null, d0, d1, d2, sound, SoundCategory.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack handStack, BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
    {
        int value = Currency.calculateValueFromSingleStack(handStack, level.getBlockEntity(pos));
        if (value > 0)
        {
            SidedInventory container = getInventory(state, level, pos);

            if (container != null)
            {
                ItemStack inside = container.getStack(0);

                if (inside.isEmpty())
                {
                    container.setStack(0, handStack.copy());
                    player.setStackInHand(hand, ItemStack.EMPTY);

                    if(!level.isClient) playSound(level, pos, state, SoundEvents.ENTITY_ITEM_PICKUP);
                    return ItemActionResult.SUCCESS;
                }
                else
                {
                    int fitInsideCount = inside.getMaxCount();
                    fitInsideCount -= inside.getCount();

                    int amountTransferring = Math.min(handStack.getCount(), fitInsideCount);
                    if(inside.isStackable() && ItemStack.areEqual(handStack, inside) && fitInsideCount > 0)
                    {
                        handStack.decrement(amountTransferring);
                        inside.increment(amountTransferring);
                        if(!level.isClient) playSound(level, pos, state, SoundEvents.ENTITY_ITEM_PICKUP);
                        return ItemActionResult.SUCCESS;
                    }
                }

            }


        }
        return super.onUseWithItem(handStack, state, level, pos, player, hand, hit);
    }


    @Override
    protected ActionResult onUse(BlockState state, World level, BlockPos pos, PlayerEntity player, BlockHitResult hit)
    {
        BlockPos center = IMultiBlock.getCenter(level, pos);
        if (level.getBlockEntity(center) instanceof SellingBinBlockEntity sbbe && !level.isClient)
        {
            player.openHandledScreen(sbbe, center);
        }
        return ActionResult.SUCCESS;
    }


    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state)
    {
        return SBBlockEntities.SELLING_BIN.get().create(blockPos, blockState);
    }

    @Override
    public boolean hasCustomBE()
    {
        return true;
    }


    @Override
    public SidedInventory getInventory(BlockState blockState, WorldAccess levelAccessor, BlockPos blockPos)
    {
        if (blockState.get(AbstractMultiBlock.CENTER))
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
