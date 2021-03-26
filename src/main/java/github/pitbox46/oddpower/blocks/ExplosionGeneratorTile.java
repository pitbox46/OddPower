package github.pitbox46.oddpower.blocks;

import github.pitbox46.oddpower.setup.Registration;
import net.minecraftforge.event.world.ExplosionEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExplosionGeneratorTile extends GeneratorTile {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final int COOLDOWN = 80;
    private long previousGeneration;

    public ExplosionGeneratorTile() {
        super(Registration.EXPLOSION_GENERATOR_TILE.get());
    }

    @Override
    protected int getMaxTransfer() {
        return 1000;
    }

    @Override
    protected int getCapacity() {
//        int capacityUpgrades = 0;
//        for(int i = 0; i <= 2; i++) {// Checks for Capacity Upgrades
//            if(itemHandler.getStackInSlot(i) != ItemStack.EMPTY && itemHandler.getStackInSlot(i).getItem() == Registration.CAPACITY_UPGRADE.get()) {
//                capacityUpgrades++;
//            }
//        }
        return 64000;
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
        if(world.getGameTime() - previousGeneration >= COOLDOWN) {
            int power = detonateEvent.getAffectedBlocks().size() * 20;
            generatePower(power);
            detonateEvent.getAffectedBlocks().clear();
            detonateEvent.getAffectedEntities().clear();
            previousGeneration = world.getGameTime();
            return true;
        }
        return false;
    }

    @Override
    public void generatePower(int power){
        super.generatePower(power);
    }
}
