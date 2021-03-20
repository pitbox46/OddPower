package github.pitbox46.oddpower.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ExplosionGenerator extends Block {
    public ExplosionGenerator() {
        super(Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.0f));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ExplosionGeneratorTile();
    }

    //Todo perhaps make these battery generators rather than stand alone generators
    // or figure out a way to nullify the explosion if the generator is there. (Explosion event is likely cancellable)
    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        if(world.getTileEntity(pos) instanceof ExplosionGeneratorTile) {
            ((ExplosionGeneratorTile) world.getTileEntity(pos)).createEnergy();
        }
    }
}
