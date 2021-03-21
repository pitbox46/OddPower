package github.pitbox46.oddpower.blocks;

import com.google.common.collect.ArrayListMultimap;
import github.pitbox46.oddpower.setup.Registration;
import github.pitbox46.oddpower.tools.OddPowerEnergy;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public class ExplosionGeneratorTile extends TileEntity implements ITickableTileEntity {
    private static final Logger LOGGER = LogManager.getLogger();

    private OddPowerEnergy energyStorage = createEnergy();
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

    /**
     * This mutlimap represents an array of actions (Character) that correspond with a given tick (Long).
     * Allows for easy scheduling based on ticks.
     */
    protected ArrayListMultimap<Long, Character> tickQueue = ArrayListMultimap.create();

    private static final int MAX_TRANSFER = 1000;
    private static final int CAPACITY = 64000;
    private static final int GENERATE = 1000;

    public ExplosionGeneratorTile() {
        super(Registration.EXPLOSION_GENERATOR_TILE.get());
    }

    @Override
    public void remove() {
        super.remove();
        energy.invalidate();
    }

    @Override
    public void tick() {
        if(world.isRemote) {
            return;
        }
        sendOutPower();
    }

    private void sendOutPower() {
        AtomicInteger capacity = new AtomicInteger(energyStorage.getEnergyStored());
        if (capacity.get() > 0) {
            for (Direction direction : Direction.values()) {
                TileEntity te = world.getTileEntity(pos.offset(direction));
                if (te != null) {
                    boolean doContinue = te.getCapability(CapabilityEnergy.ENERGY, direction).map(handler -> {
                                if (handler.canReceive()) {
                                    int received = handler.receiveEnergy(Math.min(capacity.get(), MAX_TRANSFER), false);
                                    capacity.addAndGet(-received);
                                    energyStorage.consumeEnergy(received);
                                    markDirty();
                                    return capacity.get() > 0;
                                } else {
                                    return true;
                                }
                            }
                    ).orElse(true);
                    if (!doContinue) {
                        return;
                    }
                }
            }
        }
    }

    public void generatePower(){
        LOGGER.debug("{} energy created at ({}, {}, {})", Integer.toString(GENERATE), Integer.toString(getPos().getX()), Integer.toString(getPos().getY()), Integer.toString(getPos().getZ()));
        energyStorage.addEnergy(GENERATE);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        energyStorage.deserializeNBT(tag.getCompound("energy"));
        super.read(state, tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("energy", energyStorage.serializeNBT());
        return super.write(tag);
    }

    public OddPowerEnergy createEnergy() {
        return new OddPowerEnergy(CAPACITY, MAX_TRANSFER) {
            @Override
            protected void onEnergyChanged() {
                markDirty();
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return energy.cast();
        }
        return super.getCapability(cap, side);
    }
}
