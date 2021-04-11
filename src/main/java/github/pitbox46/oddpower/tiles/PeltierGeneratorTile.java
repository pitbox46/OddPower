package github.pitbox46.oddpower.tiles;

import github.pitbox46.oddpower.items.UpgradeItem;
import github.pitbox46.oddpower.setup.Config;
import github.pitbox46.oddpower.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class PeltierGeneratorTile extends AbstractGeneratorTile {
    private static final Logger LOGGER = LogManager.getLogger();
    private long previousGeneration;
    private static final HashMap<Block, Integer> temperatureMap = Config.readTempArray();

    public PeltierGeneratorTile() {
        super((TileEntityType<?>) Registration.PELTIER_GENERATOR.get("tile").get());
    }

    @Override
    protected int getMaxTransfer() {
        return Config.PELTIER_TRANSFER.get();
    }

    @Override
    protected int getCapacity() {
        return Config.PELTIER_MAXPOWER.get();
    }

    @Override
    public void tick() {
        if(world.isRemote) {
            return;
        }
        generatePower();
        sendOutPower();
        tickCount++;
    }

    /**
     * Checks north-south sides and takes difference in values then does the same for east-west.
     * Only fires every 20 ticks.
     */
    private void generatePower() {
        if(getTickCount() - previousGeneration < 20) return;
        int northSouth = Math.abs(temperatureMap.getOrDefault(getNorthBlock(), 0) - temperatureMap.getOrDefault(getSouthBlock(), 0));
        int eastWest = Math.abs(temperatureMap.getOrDefault(getEastBlock(), 0) - temperatureMap.getOrDefault(getWestBlock(), 0));
        super.generatePower((int) Math.floor((northSouth + eastWest) * Config.PELTIER_GENERATE.get()));
        previousGeneration = getTickCount();
    }

    @Override
    public void generatePower(int power) {
        super.generatePower(power);
    }

    private Block getNorthBlock() {
        return world.getBlockState(getPos().add(0,0,-1)).getBlock();
    }
    private Block getSouthBlock() {
        return world.getBlockState(getPos().add(0,0,1)).getBlock();
    }
    private Block getEastBlock() {
        return world.getBlockState(getPos().add(1,0,0)).getBlock();
    }
    private Block getWestBlock() {
        return world.getBlockState(getPos().add(-1,0,0)).getBlock();
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
}
