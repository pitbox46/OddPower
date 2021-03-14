package github.pitbox46.oddpower.entities;

import github.pitbox46.oddpower.setup.Registration;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.world.World;

public class DummyGeneratorEntity extends LivingEntity {
    public DummyGeneratorEntity(World worldIn, double posX, double posY, double posZ) {
        this(Registration.DUMMY_GENERATOR.get(), worldIn);
        this.setPosition(posX, posY, posZ);
    }

    public DummyGeneratorEntity(EntityType<? extends DummyGeneratorEntity> entityType, World world) {
        super(entityType, world);
        this.stepHeight = 0.0F;
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
         return LivingEntity.registerAttributes().createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 0.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

//    @Override
//    public boolean attackEntityFrom(DamageSource source, float amount) {
//        if (!net.minecraftforge.common.ForgeHooks.onLivingAttack(this, source, amount)) return false;
//        if (this.isInvulnerableTo(source)) {
//            return false;
//        } else if (this.world.isRemote) {
//            return false;
//        } else if (this.getShouldBeDead()) {
//            return false;
//        } else if (source.isFireDamage() && this.isPotionActive(Effects.FIRE_RESISTANCE)) {
//            return false;
//        } else {
//
//            this.idleTime = 0;
//            float f = amount;
//            if ((source == DamageSource.ANVIL || source == DamageSource.FALLING_BLOCK) && !this.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty()) {
//                this.getItemStackFromSlot(EquipmentSlotType.HEAD).damageItem((int)(amount * 4.0F + this.rand.nextFloat() * amount * 2.0F), this, (p_233653_0_) -> {
//                    p_233653_0_.sendBreakAnimation(EquipmentSlotType.HEAD);
//                });
//                amount *= 0.75F;
//            }
//
//            this.limbSwingAmount = 1.5F;
//            boolean flag1 = true;
//            if ((float)this.hurtResistantTime > 10.0F) {
//                if (amount <= this.lastDamage) {
//                    return false;
//                }
//
//                this.damageEntity(source, amount - this.lastDamage);
//                this.lastDamage = amount;
//                flag1 = false;
//            } else {
//                this.lastDamage = amount;
//                this.hurtResistantTime = 20;
//                this.damageEntity(source, amount);
//                this.maxHurtTime = 10;
//                this.hurtTime = this.maxHurtTime;
//            }
//
//            this.attackedAtYaw = 0.0F;
//            Entity entity1 = source.getTrueSource();
//            if (entity1 != null) {
//                if (entity1 instanceof LivingEntity) {
//                    this.setRevengeTarget((LivingEntity)entity1);
//                }
//
//                if (entity1 instanceof PlayerEntity) {
//                    this.recentlyHit = 100;
//                    this.attackingPlayer = (PlayerEntity)entity1;
//                } else if (entity1 instanceof net.minecraft.entity.passive.TameableEntity) {
//                    net.minecraft.entity.passive.TameableEntity wolfentity = (net.minecraft.entity.passive.TameableEntity)entity1;
//                    if (wolfentity.isTamed()) {
//                        this.recentlyHit = 100;
//                        LivingEntity livingentity = wolfentity.getOwner();
//                        if (livingentity != null && livingentity.getType() == EntityType.PLAYER) {
//                            this.attackingPlayer = (PlayerEntity)livingentity;
//                        } else {
//                            this.attackingPlayer = null;
//                        }
//                    }
//                }
//            }
//
//            if (flag1) {
//                if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage()) {
//                    this.world.setEntityState(this, (byte)33);
//                } else {
//                    byte b0;
//                    if (source == DamageSource.DROWN) {
//                        b0 = 36;
//                    } else if (source.isFireDamage()) {
//                        b0 = 37;
//                    } else if (source == DamageSource.SWEET_BERRY_BUSH) {
//                        b0 = 44;
//                    } else {
//                        b0 = 2;
//                    }
//
//                    this.world.setEntityState(this, b0);
//                }
//
//                if (source != DamageSource.DROWN && (amount > 0.0F)) {
//                    this.markVelocityChanged();
//                }
//
//                if (entity1 != null) {
//                    double d1 = entity1.getPosX() - this.getPosX();
//
//                    double d0;
//                    for(d0 = entity1.getPosZ() - this.getPosZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D) {
//                        d1 = (Math.random() - Math.random()) * 0.01D;
//                    }
//
//                    this.attackedAtYaw = (float)(MathHelper.atan2(d0, d1) * (double)(180F / (float)Math.PI) - (double)this.rotationYaw);
//                    this.applyKnockback(0.4F, d1, d0);
//                } else {
//                    this.attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
//                }
//            }
//
//            //Maybe change this to set health back to full and send energy
//            if (this.getShouldBeDead()) {
//                SoundEvent soundevent = this.getDeathSound();
//                if (flag1 && soundevent != null) {
//                    this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
//                }
//
//                this.onDeath(source);
//            } else if (flag1) {
//                this.playHurtSound(source);
//            }
//
//            return true;//?
//        }
//    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return null;
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        return null;
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {

    }

    @Override
    public HandSide getPrimaryHand() {
        return null;
    }

    @Override
    public boolean isImmuneToFire() {
        return true;
    }
}
