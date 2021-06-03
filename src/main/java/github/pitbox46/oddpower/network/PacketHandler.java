package github.pitbox46.oddpower.network;

import github.pitbox46.oddpower.OddPower;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "3.2.1";
    public static SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("fishingoverhaul", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);
    private static int ID = 0;

    public static void init() {
        CHANNEL.registerMessage(
                ID++,
                EnergySyncPacket.class,
                (msg, pb) -> {
                    pb.writeBlockPos(msg.blockPos);
                    pb.writeInt(msg.energy);
                },
                pb -> new EnergySyncPacket(pb.readBlockPos(), pb.readInt()),
                (msg, ctx) -> {
                    ctx.get().enqueueWork(() -> OddPower.PROXY.handleEnergySync(ctx.get(), msg.blockPos, msg.energy));
                    ctx.get().setPacketHandled(true);
                });
    }
}