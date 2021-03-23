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
    //Todo: Make sure that this doesn't actually do anything so it can be removed
    public DummyEntity(World worldIn, double posX, double posY, double posZ) {
        this(Registration.DUMMY.get(), worldIn);
        this.setPos(posX, posY, posZ);
    }

    public DummyEntity(EntityType<? extends DummyEntity> entityType, World world) {
        super(entityType, world);
        this.maxUpStep = 0.0F;
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
         return LivingEntity.createLivingAttributes()
                 .add(Attributes.MAX_HEALTH, 20.0D)
                 .add(Attributes.ATTACK_DAMAGE, 0.0D)
                 .add(Attributes.MOVEMENT_SPEED, 0.0D)
                 .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                 .add(Attributes.FOLLOW_RANGE, 0.0D);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entityIn) {
        //Do nothing on collision
    }

    @Override
    public void die(DamageSource cause) {
        TileEntity tileEntity = level.getBlockEntity(blockPosition().below());
        if(tileEntity instanceof DummyGeneratorTile){
            ((DummyGeneratorTile) tileEntity).generatePower(1000);
        }
        super.die(cause);
    }

    @Override
    public ActionResultType interactAt(PlayerEntity player, Vector3d vec, Hand hand) {
        if(!level.isClientSide() && player.isShiftKeyDown() && player.getItemInHand(hand) == ItemStack.EMPTY) {
            remove();
            player.setItemInHand(Hand.MAIN_HAND, new ItemStack(Registration.DUMMY_ITEM.get(), 1));
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}
