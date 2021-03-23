package github.pitbox46.oddpower.blocks;

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
import net.minecraftforge.event.world.ExplosionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public class ExplosionGeneratorTile extends TileEntity implements ITickableTileEntity {
    private static final Logger LOGGER = LogManager.getLogger();

    private OddPowerEnergy energyStorage = createEnergy();
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

    private static final int MAX_TRANSFER = 1000;
    private static final int CAPACITY = 64000;
    private static final int COOLDOWN = 80;
    private long previousGeneration;

    public ExplosionGeneratorTile() {
        super(Registration.EXPLOSION_GENERATOR_TILE.get());
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        energy.invalidate();
    }

    @Override
    public void tick() {
        if(level.isClientSide) {
            return;
        }
        sendOutPower();
    }

    private void sendOutPower() {
        AtomicInteger capacity = new AtomicInteger(energyStorage.getEnergyStored());
        if (capacity.get() > 0) {
            for (Direction direction : Direction.values()) {
                TileEntity te = level.getBlockEntity(worldPosition.relative(direction));
                if (te != null) {
                    boolean doContinue = te.getCapability(CapabilityEnergy.ENERGY, direction).map(handler -> {
                                if (handler.canReceive()) {
                                    int received = handler.receiveEnergy(Math.min(capacity.get(), MAX_TRANSFER), false);
                                    capacity.addAndGet(-received);
                                    energyStorage.consumeEnergy(received);
                                    setChanged();
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

    /**
     * Called on explosion event. Nullifies damage and generates energy if the generator hasn't been active for COOLDOWN ticks
     *
     * @return Boolean based on if the generator can generate power
     */
    public boolean onExplosion(ExplosionEvent.Detonate detonateEvent) {
        if(level.getGameTime() - previousGeneration >= COOLDOWN) {
            int power = detonateEvent.getAffectedBlocks().size() * 20;
            generatePower(power);
            detonateEvent.getAffectedBlocks().clear();
            detonateEvent.getAffectedEntities().clear();
            previousGeneration = level.getGameTime();
            return true;
        }
        return false;
    }

    public void generatePower(int power){
        int remainingCapacity = energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored();
        energyStorage.addEnergy(Math.min(power, remainingCapacity));
        LOGGER.debug("{} energy created at ({}, {}, {})", Integer.toString(Math.min(power, remainingCapacity)), Integer.toString(getBlockPos().getX()), Integer.toString(getBlockPos().getY()), Integer.toString(getBlockPos().getZ()));
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        energyStorage.deserializeNBT(tag.getCompound("energy"));
        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.put("energy", energyStorage.serializeNBT());
        return super.save(tag);
    }

    public OddPowerEnergy createEnergy() {
        return new OddPowerEnergy(CAPACITY, MAX_TRANSFER) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }

            @Override
            public boolean canReceive() {
                return false;
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
