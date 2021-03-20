package github.pitbox46.oddpower.common;

import github.pitbox46.oddpower.entities.DummyEntity;
import github.pitbox46.oddpower.setup.Registration;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
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

import javax.annotation.Nullable;
import java.util.List;

public class DummyItem extends Item {
    public DummyItem() {
        super(new Item.Properties().group(ItemGroup.COMBAT));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
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
            AxisAlignedBB axisalignedbb = Registration.DUMMY.get().getSize().func_242285_a(vector3d.getX(), vector3d.getY(), vector3d.getZ());
            if (world.hasNoCollisions((Entity)null, axisalignedbb, (entity) -> {
                return true;
            }) && world.getEntitiesWithinAABBExcludingEntity((Entity)null, axisalignedbb).isEmpty()) {
                if (world instanceof ServerWorld) {
                    ServerWorld serverworld = (ServerWorld)world;
                    DummyEntity dummyEntity = Registration.DUMMY.get().create(serverworld, itemstack.getTag(), null, context.getPlayer(), blockpos, SpawnReason.SPAWN_EGG, true, true);
                    if (dummyEntity == null) {
                        return ActionResultType.FAIL;
                    }

                    serverworld.func_242417_l(dummyEntity);
                    float f = (float)(Math.round(context.getPlacementYaw()/45) * 45 + 180);
                    dummyEntity.setLocationAndAngles(dummyEntity.getPosX(), dummyEntity.getPosY(), dummyEntity.getPosZ(), f, 0.0F);
                    dummyEntity.rotationYawHead = dummyEntity.rotationYaw;
                    dummyEntity.renderYawOffset = dummyEntity.rotationYaw;
                    world.addEntity(dummyEntity);
                    world.playSound((PlayerEntity)null, dummyEntity.getPosX(), dummyEntity.getPosY(), dummyEntity.getPosZ(), SoundEvents.ENTITY_ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
                }

                itemstack.shrink(1);
                return ActionResultType.func_233537_a_(world.isRemote);
            } else {
                return ActionResultType.FAIL;
            }
        }
    }
}
