package github.pitbox46.oddpower.tiles;

import github.pitbox46.oddpower.items.UpgradeItem;
import github.pitbox46.oddpower.setup.Config;
import github.pitbox46.oddpower.setup.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class GravityGeneratorTile extends AbstractGeneratorTile {
    private static final Logger LOGGER = LogManager.getLogger();
    private long previousGeneration;

    public GravityGeneratorTile() {
        super((TileEntityType<?>) Registration.GRAVITY_GENERATOR.get("tile").get());
    }

    @Override
    protected int getMaxTransfer() {
        return Config.GRAVITY_TRANSFER.get();
    }

    @Override
    protected int getCapacity() {
        return Config.GRAVITY_MAXPOWER.get();
    }

    protected IItemHandler createHandler() {
        return new ItemStackHandler(3) {
            @Override
            protected void onContentsChanged(int slot) {
                int capacityUpgrades = 0;
                for(int i = 0; i <= 2; i++) {// Checks for Capacity Upgrades
                    if(itemHandler.getStackInSlot(i) != ItemStack.EMPTY && itemHandler.getStackInSlot(i).getItem() == Registration.CAPACITY_UPGRADE.get()) {
                        capacityUpgrades++;
                    }
                }
                energyStorage.setMaxEnergyStored(getCapacity() + getCapacity() * capacityUpgrades);
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem() instanceof UpgradeItem;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return isItemValid(slot, stack) ? super.insertItem(slot, stack, simulate) : stack;
            }
        };
    }

    /**
     * Reads the previous distance fallen (taken from {@link github.pitbox46.oddpower.blocks.GravityGenerator})
     * and generates power based on it. Does not rewrite the value because it should not be used again.
     */

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        float previous_distance = tag.getFloat("previous_distance");
        if(previous_distance != 0) generatePower((int) Math.floor(Math.pow(previous_distance, 2) * Config.GRAVITY_GENERATE.get()));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        return super.write(tag);
    }
}
