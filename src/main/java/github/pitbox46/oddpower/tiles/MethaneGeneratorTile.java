package github.pitbox46.oddpower.tiles;

import github.pitbox46.oddpower.items.UpgradeItem;
import github.pitbox46.oddpower.setup.Config;
import github.pitbox46.oddpower.setup.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

import static net.minecraft.state.properties.BlockStateProperties.LIT;

public class MethaneGeneratorTile extends AbstractGeneratorTile {
    private static final Logger LOGGER = LogManager.getLogger();
    private long previousGeneration;
    private AxisAlignedBB zeroBox = new AxisAlignedBB(0,0,0,0,0,0);
    private AxisAlignedBB boundingBox = new AxisAlignedBB(0,0,0,0,0,0);

    public MethaneGeneratorTile() {
        super((TileEntityType<?>) Registration.METHANE_GENERATOR.get("tile").get());
    }

    @Override
    protected int getMaxTransfer() {
        return Config.METHANE_TRANSFER.get();
    }

    @Override
    protected int getCapacity() {
        return Config.METHANE_MAXPOWER.get();
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

    private void generatePower() {
        if(getTickCount() - previousGeneration < 20) return;
        if(boundingBox.equals(zeroBox)) {
            boundingBox = new AxisAlignedBB(pos.getX()-2,pos.getY()-2,pos.getZ()-2,pos.getX()+3,pos.getY(),pos.getZ()+3);
        }
        int cowNumber = this.world.getLoadedEntitiesWithinAABB(CowEntity.class, boundingBox).size();
        if(world.getBlockState(pos).get(LIT) != cowNumber > 0) {
            world.setBlockState(pos, world.getBlockState(pos).with(LIT, cowNumber > 0), 3);
        }
        generatePower((int) Math.floor(cowNumber * Config.METHANE_GENERATE.get()));
        previousGeneration = getTickCount();
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        boundingBox = new AxisAlignedBB(pos.getX()-2,pos.getY()-2,pos.getZ()-2,pos.getX()+3,pos.getY(),pos.getZ()+3);
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
