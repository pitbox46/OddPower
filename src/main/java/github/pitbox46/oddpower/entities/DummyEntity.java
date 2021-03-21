package github.pitbox46.oddpower.entities;

import github.pitbox46.oddpower.blocks.DummyGeneratorTile;
import github.pitbox46.oddpower.setup.Registration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class DummyEntity extends MobEntity {
    public DummyEntity(World worldIn, double posX, double posY, double posZ) {
        this(Registration.DUMMY.get(), worldIn);
        this.setPosition(posX, posY, posZ);
    }

    public DummyEntity(EntityType<? extends DummyEntity> entityType, World world) {
        super(entityType, world);
        this.stepHeight = 0.0F;
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
         return LivingEntity.registerAttributes()
                 .createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
                 .createMutableAttribute(Attributes.ATTACK_DAMAGE, 0.0D)
                 .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.0D)
                 .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                 .createMutableAttribute(Attributes.FOLLOW_RANGE, 0.0D);
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
    }

    @Override
    public void onDeath(DamageSource cause) {
        TileEntity tileEntity = world.getTileEntity(getPosition().down());
        if(tileEntity instanceof DummyGeneratorTile){
            ((DummyGeneratorTile) tileEntity).generatePower();
        }
        super.onDeath(cause);
    }

    @Override
    public ActionResultType applyPlayerInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
        if(!world.isRemote() && player.isSneaking() && player.getHeldItem(hand) == ItemStack.EMPTY) {
            remove();
            player.setHeldItem(Hand.MAIN_HAND, new ItemStack(Registration.DUMMY_ITEM.get(), 1));
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}
