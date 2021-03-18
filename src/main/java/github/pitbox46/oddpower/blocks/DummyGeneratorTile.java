package github.pitbox46.oddpower.blocks;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import github.pitbox46.oddpower.entities.DummyEntity;
import github.pitbox46.oddpower.setup.Registration;
import github.pitbox46.oddpower.tools.OddPowerEnergy;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public class DummyGeneratorTile extends TileEntity implements ITickableTileEntity {
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
        //First condition checks to see if the method should be called on this tick
        if(tickQueue.containsEntry(world.getGameTime(), 'a') && world instanceof ServerWorld) {
            spawnNewDummy();
            tickQueue.remove(world.getGameTime(), 'a');
        }
        sendOutPower();
    }

    public void dummyDies(){
        energyStorage.addEnergy(1000);
        if(!tickQueue.containsValue('a')) { //Prevents mutltiple dummies from being queued at once
            tickQueue.put(world.getGameTime() + 20, 'a');
        }
    }

    protected void spawnNewDummy(){
        ServerWorld serverworld = (ServerWorld)world;
        DummyEntity dummyGeneratorEntity = Registration.DUMMY.get().create(serverworld, null, null, null, getPos(), SpawnReason.DISPENSER, true, true);
        serverworld.func_242417_l(dummyGeneratorEntity);
        dummyGeneratorEntity.setLocationAndAngles(dummyGeneratorEntity.getPosX(), dummyGeneratorEntity.getPosY(), dummyGeneratorEntity.getPosZ(), 0.0F, 0.0F);
        world.addEntity(dummyGeneratorEntity);
        world.playSound(null, dummyGeneratorEntity.getPosX(), dummyGeneratorEntity.getPosY(), dummyGeneratorEntity.getPosZ(), SoundEvents.ENTITY_ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
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
