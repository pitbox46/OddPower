package github.pitbox46.oddpower.common;

import github.pitbox46.oddpower.entities.DummyEntity;
import github.pitbox46.oddpower.setup.Registration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class DummyGeneratorItem extends Item {
    public DummyGeneratorItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        Direction direction = context.getFace();
        if (direction == Direction.DOWN) {
            return ActionResultType.FAIL;
        } else {
            World world = context.getWorld();
            BlockItemUseContext blockitemusecontext = new BlockItemUseContext(context);
            BlockPos blockpos = blockitemusecontext.getPos();
            ItemStack itemstack = context.getItem();
            Vector3d vector3d = Vector3d.copyCenteredHorizontally(blockpos);
            AxisAlignedBB axisalignedbb = Registration.DUMMY_GENERATOR.get().getSize().func_242285_a(vector3d.getX(), vector3d.getY(), vector3d.getZ());
            if (world.hasNoCollisions((Entity)null, axisalignedbb, (entity) -> {
                return true;
            }) && world.getEntitiesWithinAABBExcludingEntity((Entity)null, axisalignedbb).isEmpty()) {
                if (world instanceof ServerWorld) {
                    ServerWorld serverworld = (ServerWorld)world;
                    DummyEntity dummyGeneratorEntity = Registration.DUMMY_GENERATOR.get().create(serverworld, itemstack.getTag(), (ITextComponent)null, context.getPlayer(), blockpos, SpawnReason.SPAWN_EGG, true, true);
                    if (dummyGeneratorEntity == null) {
                        return ActionResultType.FAIL;
                    }

                    serverworld.func_242417_l(dummyGeneratorEntity);
                    float f = (float) MathHelper.floor((MathHelper.wrapDegrees(context.getPlacementYaw() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                    dummyGeneratorEntity.setLocationAndAngles(dummyGeneratorEntity.getPosX(), dummyGeneratorEntity.getPosY(), dummyGeneratorEntity.getPosZ(), f, 0.0F);
                    world.addEntity(dummyGeneratorEntity);
                    world.playSound((PlayerEntity)null, dummyGeneratorEntity.getPosX(), dummyGeneratorEntity.getPosY(), dummyGeneratorEntity.getPosZ(), SoundEvents.ENTITY_ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
                }

                itemstack.shrink(1);
                return ActionResultType.func_233537_a_(world.isRemote);
            } else {
                return ActionResultType.FAIL;
            }
        }
    }
}
