package io.github.frqnny.darkenchanting.block;

import io.github.frqnny.darkenchanting.DarkEnchanting;
import io.github.frqnny.darkenchanting.blockentity.DarkEnchanterBlockEntity;
import io.github.frqnny.darkenchanting.util.BookEnum;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class DarkEnchanterBlock extends BlockWithEntity {
    public static final Identifier ID = new Identifier(DarkEnchanting.MODID, "dark_enchanter");
    public static final EnumProperty<BookEnum> BOOK_TYPE = EnumProperty.of("book_type", BookEnum.class);
    private static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

    public DarkEnchanterBlock(Settings s) {
        super(s);
        this.setDefaultState(this.getStateManager().getDefaultState().with(BOOK_TYPE, BookEnum.DEFAULT));
    }


    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DarkEnchanterBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        if (!world.isClient) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof DarkEnchanterBlockEntity) {
                player.openHandledScreen((NamedScreenHandlerFactory) be);
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public BlockRenderType getRenderType(BlockState blockState_1) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean hasSidedTransparency(BlockState blockState_1) {
        return true;
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState blockState_1, World world_1, BlockPos blockPos_1, Random random_1) {
        super.randomDisplayTick(blockState_1, world_1, blockPos_1, random_1);

        for (int int_1 = -2; int_1 <= 2; ++int_1) {
            for (int int_2 = -2; int_2 <= 2; ++int_2) {
                if (int_1 > -2 && int_1 < 2 && int_2 == -1) {
                    int_2 = 2;
                }

                if (random_1.nextInt(16) == 0) {
                    for (int int_3 = 0; int_3 <= 1; ++int_3) {
                        BlockPos blockPos_2 = blockPos_1.add(int_1, int_3, int_2);
                        if (world_1.getBlockState(blockPos_2).getBlock() == Blocks.BOOKSHELF) {
                            if (!world_1.isAir(blockPos_1.add(int_1 / 2, 0, int_2 / 2))) {
                                break;
                            }

                            world_1.addParticle(ParticleTypes.ENCHANT, (double) blockPos_1.getX() + 0.5D, (double) blockPos_1.getY() + 2.0D, (double) blockPos_1.getZ() + 0.5D, (double) ((float) int_1 + random_1.nextFloat()) - 0.5D, (float) int_3 - random_1.nextFloat() - 1.0F, (double) ((float) int_2 + random_1.nextFloat()) - 0.5D);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(BOOK_TYPE);
    }
}

