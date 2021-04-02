package github.pitbox46.oddpower.tiles;

import github.pitbox46.oddpower.items.UpgradeItem;
import github.pitbox46.oddpower.setup.Config;
import github.pitbox46.oddpower.setup.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.HashMap;

import static net.minecraft.state.properties.BlockStateProperties.LIT;
import static net.minecraft.state.properties.BlockStateProperties.POWERED;

public class MethaneGeneratorTile extends AbstractGeneratorTile {
    private static final Logger LOGGER = LogManager.getLogger();
    BlockPos blockPos;
    BlockPos blockPos2;
    private AxisAlignedBB boundingBox = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

    public MethaneGeneratorTile() {
        super(Registration.METHANE_GENERATOR_TILE.get());
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
        int cowNumber = this.world.getLoadedEntitiesWithinAABB(CowEntity.class, boundingBox).size();
        if(world.getBlockState(pos).get(LIT) != cowNumber > 0) {
            world.setBlockState(pos, world.getBlockState(pos).with(LIT, cowNumber > 0), 3);
        }
        generatePower((int) Math.floor(cowNumber * Config.METHANE_GENERATE.get()));
        sendOutPower();
        tickCount++;
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        boundingBox = new AxisAlignedBB(pos.getX()-3,pos.getY()-4,pos.getZ()-3,pos.getX()+3,pos.getY()-1,pos.getZ()+3);
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
