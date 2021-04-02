package github.pitbox46.oddpower.blocks;

import github.pitbox46.oddpower.gui.IncineratorContainer;
import github.pitbox46.oddpower.setup.Registration;
import github.pitbox46.oddpower.tiles.IncineratorGeneratorTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

import static net.minecraft.state.properties.BlockStateProperties.*;

public class IncineratorGenerator extends Block {
    public IncineratorGenerator() {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.0f)
                .setLightLevel((state) -> state.get(LIT) ? 13 : 0));
        setDefaultState(getStateContainer().getBaseState().with(LIT, false));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new IncineratorGeneratorTile();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LIT).add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState blockState = super.getStateForPlacement(context);
        assert blockState != null;
        return blockState.with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(tileEntity instanceof IncineratorGeneratorTile) {
                INamedContainerProvider containerProvider = new INamedContainerProvider() {
                    @Override
                    public ITextComponent getDisplayName() {
                        return new TranslationTextComponent("screen.oddpower.incinerator_generator");
                    }

                    @Nullable
                    @Override
                    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                        return new IncineratorContainer(Registration.INCINERATOR_GENERATOR_CONTAINER.get(), i, pos, playerInventory, Registration.INCINERATOR_GENERATOR.get());
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