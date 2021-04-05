package github.pitbox46.oddpower.blocks;

import github.pitbox46.oddpower.gui.SlotlessGeneratorContainer;
import github.pitbox46.oddpower.setup.Registration;
import github.pitbox46.oddpower.tiles.MethaneGeneratorTile;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.LIT;

public class MethaneGenerator extends BreakableBlock {
    public static final VoxelShape BASE = Block.makeCuboidShape(0,0,0,16,5,16);
    public static final VoxelShape MID = Block.makeCuboidShape(1,5,1,15,8,15);
    public static final VoxelShape TOP = Block.makeCuboidShape(0,8,0,16,16,16);
    public static final VoxelShape COMBINED_SHAPE = VoxelShapes.or(BASE, MID, TOP);

    public MethaneGenerator() {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.0f)
                .setLightLevel((state) -> state.get(LIT) ? 13 : 0).setBlocksVision((a,b,c) -> false));
        setDefaultState(getStateContainer().getBaseState().with(LIT, false));
    }

    @Override
    public VoxelShape getRayTraceShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return COMBINED_SHAPE;
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return COMBINED_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return COMBINED_SHAPE;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return COMBINED_SHAPE;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MethaneGeneratorTile();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof MethaneGeneratorTile) {
                INamedContainerProvider containerProvider = new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new TranslationTextComponent("screen.oddpower.methane_generator");
                    }

                    @Nullable
                    @Override
                    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                        return new SlotlessGeneratorContainer(Registration.METHANE_GENERATOR_CONTAINER.get(), i, pos, playerInventory, Registration.METHANE_GENERATOR.get());
                    }
                };
                NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, tileEntity.getPos());
            } else {
                throw new IllegalStateException("Named container provider is missing");
            }
        }
        return ActionResultType.SUCCESS;
    }
}
