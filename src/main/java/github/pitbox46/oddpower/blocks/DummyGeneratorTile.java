package github.pitbox46.oddpower.blocks;

import github.pitbox46.oddpower.entities.DummyEntity;
import github.pitbox46.oddpower.setup.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnReason;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DummyGeneratorTile extends GeneratorTile {
    private static final Logger LOGGER = LogManager.getLogger();

    public DummyGeneratorTile() {
        super(Registration.DUMMY_GENERATOR_TILE.get());
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

    @Override
    public void generatePower(int power){
        super.generatePower(power);

        BlockState blockState = world.getBlockState(pos);
        world.setBlockState(pos, blockState.with(BlockStateProperties.POWERED, true),
                Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.BLOCK_UPDATE);
        if(!tickQueue.containsValue('a')) { //Prevents multiple dummies from being queued at once
            tickQueue.put(world.getGameTime() + 20, 'a');
        }
    }

    protected void spawnNewDummy(){
        ServerWorld serverworld = (ServerWorld)world;
        DummyEntity dummyEntity = Registration.DUMMY.get().create(serverworld, null, null, null, getPos(), SpawnReason.DISPENSER, true, true);
        serverworld.func_242417_l(dummyEntity);
        dummyEntity.setLocationAndAngles(dummyEntity.getPosX(), dummyEntity.getPosY(), dummyEntity.getPosZ(), 0.0F, 0.0F);
        dummyEntity.rotationYawHead = dummyEntity.rotationYaw;
        dummyEntity.renderYawOffset = dummyEntity.rotationYaw;
        world.playSound(null, dummyEntity.getPosX(), dummyEntity.getPosY(), dummyEntity.getPosZ(), SoundEvents.ENTITY_ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
    }
}
