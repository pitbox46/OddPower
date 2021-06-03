package github.pitbox46.oddpower.network;

import github.pitbox46.oddpower.tiles.AbstractGeneratorTile;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkEvent;

public class ClientProxy extends CommonProxy {
    public ClientProxy() {
        super();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void handleEnergySync(NetworkEvent.Context ctx, BlockPos tileEntityPos, int energy) {
        TileEntity tile = Minecraft.getInstance().world.getTileEntity(tileEntityPos);
        if(tile instanceof AbstractGeneratorTile) {
            ((AbstractGeneratorTile) tile).recieveSyncEnergy(energy);
        }
    }
}
