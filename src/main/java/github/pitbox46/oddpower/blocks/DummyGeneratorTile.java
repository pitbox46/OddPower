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
    public void remove() {
        super.remove();
        energy.invalidate();
    }

    @Override
    public void tick() {
        if(world.isRemote) {
            return;
        }
        BlockState blockState = world.getBlockState(pos);
        //First condition checks to see if the method should be called on this tick
        if(tickQueue.containsEntry(world.getGameTime(), 'a') && world instanceof ServerWorld) {
            spawnNewDummy();
            tickQueue.remove(world.getGameTime(), 'a');
            world.setBlockState(pos, blockState.with(BlockStateProperties.POWERED, false),
                    Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
        }

        sendOutPower();
    }

    public void generatePower(int power){
        int remainingCapacity = energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored();
        energyStorage.addEnergy(Math.min(power, remainingCapacity));
        BlockState blockState = world.getBlockState(pos);
        world.setBlockState(pos, blockState.with(BlockStateProperties.POWERED, true),
                Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
        if(!tickQueue.containsValue('a')) { //Prevents multiple dummies from being queued at once
            tickQueue.put(world.getGameTime() + 20, 'a');
        }
        LOGGER.debug("{} energy created at ({}, {}, {})", Integer.toString(Math.min(power, remainingCapacity)), Integer.toString(getPos().getX()), Integer.toString(getPos().getY()), Integer.toString(getPos().getZ()));
    }

    protected void spawnNewDummy(){
        ServerWorld serverworld = (ServerWorld)world;
        DummyEntity dummyEntity = Registration.DUMMY.get().create(serverworld, null, null, null, getPos(), SpawnReason.DISPENSER, true, true);
        serverworld.func_242417_l(dummyEntity);
        dummyEntity.setLocationAndAngles(dummyEntity.getPosX(), dummyEntity.getPosY(), dummyEntity.getPosZ(), 0.0F, 0.0F);
        dummyEntity.rotationYawHead = dummyEntity.rotationYaw;
        dummyEntity.renderYawOffset = dummyEntity.rotationYaw;
        //world.addEntity(dummyEntity);
        world.playSound(null, dummyEntity.getPosX(), dummyEntity.getPosY(), dummyEntity.getPosZ(), SoundEvents.ENTITY_ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
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

    private OddPowerEnergy createEnergy() {
        return new OddPowerEnergy(CAPACITY, MAX_TRANSFER) {
            @Override
            protected void onEnergyChanged() {
                markDirty();
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
