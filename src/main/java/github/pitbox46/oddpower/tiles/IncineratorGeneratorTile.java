package github.pitbox46.oddpower.tiles;

import github.pitbox46.oddpower.items.UpgradeItem;
import github.pitbox46.oddpower.setup.Config;
import github.pitbox46.oddpower.setup.Registration;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class IncineratorGeneratorTile extends AbstractGeneratorTile {
    private static final Logger LOGGER = LogManager.getLogger();
    private long previousGeneration;

    public IncineratorGeneratorTile() {
        super(Registration.INCINERATOR_GENERATOR_TILE.get());
    }

    @Override
    protected int getMaxTransfer() {
        return Config.INCINERATOR_TRANSFER.get();
    }

    @Override
    protected int getCapacity() {
        return Config.INCINERATOR_MAXPOWER.get();
    }

    @Override
    public void tick() {
        if(world.isRemote) {
            return;
        }
        incinerate();
        sendOutPower();
        tickCount++;
    }

    @Override
    public void generatePower(int power){
        super.generatePower(power);
    }

    public void incinerate() {
        itemHandler.getStackInSlot(3);
        if (getTickCount() - previousGeneration >= Config.INCINERATOR_COOLDOWN.get() && !itemHandler.getStackInSlot(3).isEmpty() && !(itemHandler.getStackInSlot(3).getItem() instanceof UpgradeItem)) {
            itemHandler.getStackInSlot(3).shrink(1);
            generatePower(Config.INCINERATOR_GENERATE.get());
            previousGeneration = getTickCount();
        }
    }

    @Override
    protected IItemHandler createHandler() {
        return new ItemStackHandler(4) {
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
                return (slot < 3 && stack.getItem() instanceof UpgradeItem) || (slot == 3 && !(stack.getItem() instanceof UpgradeItem));
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return isItemValid(slot, stack) ? super.insertItem(slot, stack, simulate) : stack;
            }
        };
    }
}
