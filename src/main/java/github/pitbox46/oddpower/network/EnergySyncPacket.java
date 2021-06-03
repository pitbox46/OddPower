package github.pitbox46.oddpower.network;

import net.minecraft.util.math.BlockPos;

public class EnergySyncPacket {
    public final BlockPos blockPos;
    public final int energy;
    public EnergySyncPacket(BlockPos blockPos, int energy) {
        this.blockPos = blockPos;
        this.energy = energy;
    }
}
