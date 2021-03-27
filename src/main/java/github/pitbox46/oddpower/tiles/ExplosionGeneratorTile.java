package github.pitbox46.oddpower.tiles;

import github.pitbox46.oddpower.items.UpgradeItem;
import github.pitbox46.oddpower.setup.Config;
import github.pitbox46.oddpower.setup.Registration;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class ExplosionGeneratorTile extends AbstractGeneratorTile {
    private static final Logger LOGGER = LogManager.getLogger();
    private long previousGeneration;

    public ExplosionGeneratorTile() {
        super(Registration.EXPLOSION_GENERATOR_TILE.get());
    }

    @Override
    protected int getMaxTransfer() {
        return Config.EXPLOSION_TRANSFER.get();
    }

    @Override
    protected int getCapacity() {
        return Config.EXPLOSION_MAXPOWER.get();
    }

    @Override
    public void remove() {
        super.remove();
        energy.invalidate();
    }

    @Override
    public void tick() {
        super.tick();
    }

    /**
     * Called on explosion event. Nullifies damage and generates energy if the generator hasn't been active for COOLDOWN ticks
     *
     * @return Boolean based on if the generator can generate power
     */
    public boolean onExplosion(ExplosionEvent.Detonate detonateEvent) {
        if(getTickCount() - previousGeneration < Config.EXPLOSION_COOLDOWN.get()) return false;
        int power = detonateEvent.getAffectedBlocks().size() * Config.EXPLOSION_GENERATE.get();
        generatePower(power);
        detonateEvent.getAffectedBlocks().clear();
        detonateEvent.getAffectedEntities().clear();
        previousGeneration = getTickCount();
        return true;
    }

    @Override
    public void generatePower(int power){
        super.generatePower(power);
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
