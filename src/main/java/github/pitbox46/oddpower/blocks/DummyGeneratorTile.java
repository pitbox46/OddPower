package github.pitbox46.oddpower.blocks;

import com.google.common.collect.ArrayListMultimap;
import github.pitbox46.oddpower.entities.DummyEntity;
import github.pitbox46.oddpower.setup.Registration;
import github.pitbox46.oddpower.tools.OddPowerEnergy;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public class DummyGeneratorTile extends TileEntity implements ITickableTileEntity {
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

    public DummyGeneratorTile() {
        super(Registration.DUMMY_GENERATOR_TILE.get());
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
        BlockState blockState = level.getBlockState(worldPosition);
        //First condition checks to see if the method should be called on this tick
        if(tickQueue.containsEntry(level.getGameTime(), 'a') && level instanceof ServerWorld) {
            spawnNewDummy();
            tickQueue.remove(level.getGameTime(), 'a');
            level.setBlock(worldPosition, blockState.setValue(BlockStateProperties.POWERED, false),
                    Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
        }

        sendOutPower();
    }

    public void generatePower(int power){
        int remainingCapacity = energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored();
        energyStorage.addEnergy(Math.min(power, remainingCapacity));
        BlockState blockState = level.getBlockState(worldPosition);
        level.setBlock(worldPosition, blockState.setValue(BlockStateProperties.POWERED, true),
                Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
        if(!tickQueue.containsValue('a')) { //Prevents multiple dummies from being queued at once
            tickQueue.put(level.getGameTime() + 20, 'a');
        }
        LOGGER.debug("{} energy created at ({}, {}, {})", Integer.toString(Math.min(power, remainingCapacity)), Integer.toString(getBlockPos().getX()), Integer.toString(getBlockPos().getY()), Integer.toString(getBlockPos().getZ()));
    }

    protected void spawnNewDummy(){
        ServerWorld serverworld = (ServerWorld)level;
        DummyEntity dummyEntity = Registration.DUMMY.get().create(serverworld, null, null, null, getBlockPos(), SpawnReason.DISPENSER, true, true);
        serverworld.addFreshEntityWithPassengers(dummyEntity);
        dummyEntity.moveTo(dummyEntity.getX(), dummyEntity.getY(), dummyEntity.getZ(), 0.0F, 0.0F);
        dummyEntity.yHeadRot = dummyEntity.yRot;
        dummyEntity.yBodyRot = dummyEntity.yRot;
        //world.addEntity(dummyEntity);
        level.playSound(null, dummyEntity.getX(), dummyEntity.getY(), dummyEntity.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
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

    private OddPowerEnergy createEnergy() {
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
