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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class DummyItem extends Item {
    public DummyItem() {
        super(new Item.Properties().tab(ItemGroup.TAB_COMBAT));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        Direction direction = context.getClickedFace();
        if (direction == Direction.DOWN) {
            return ActionResultType.FAIL;
        } else {
            World world = context.getLevel();
            BlockItemUseContext blockitemusecontext = new BlockItemUseContext(context);
            BlockPos blockpos = blockitemusecontext.getClickedPos();
            ItemStack itemstack = context.getItemInHand();
            Vector3d vector3d = Vector3d.atBottomCenterOf(blockpos);
            AxisAlignedBB axisalignedbb = Registration.DUMMY.get().getDimensions().makeBoundingBox(vector3d.x(), vector3d.y(), vector3d.z());
            if (world.noCollision((Entity)null, axisalignedbb, (entity) -> {
                return true;
            }) && world.getEntities((Entity)null, axisalignedbb).isEmpty()) {
                if (world instanceof ServerWorld) {
                    ServerWorld serverworld = (ServerWorld)world;
                    DummyEntity dummyEntity = Registration.DUMMY.get().create(serverworld, itemstack.getTag(), null, context.getPlayer(), blockpos, SpawnReason.SPAWN_EGG, true, true);
                    if (dummyEntity == null) {
                        return ActionResultType.FAIL;
                    }

                    serverworld.addFreshEntityWithPassengers(dummyEntity);
                    float f = (float)(Math.round(context.getRotation()/45) * 45 + 180);
                    dummyEntity.moveTo(dummyEntity.getX(), dummyEntity.getY(), dummyEntity.getZ(), f, 0.0F);
                    dummyEntity.yHeadRot = dummyEntity.yRot;
                    dummyEntity.yBodyRot = dummyEntity.yRot;
                    //world.addEntity(dummyEntity);
                    world.playSound((PlayerEntity)null, dummyEntity.getX(), dummyEntity.getY(), dummyEntity.getZ(), SoundEvents.ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
                }

                itemstack.shrink(1);
                return ActionResultType.sidedSuccess(world.isClientSide);
            } else {
                return ActionResultType.FAIL;
            }
        }
    }
}
