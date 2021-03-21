package github.pitbox46.oddpower.common;

import github.pitbox46.oddpower.blocks.ExplosionGeneratorTile;
import github.pitbox46.oddpower.entities.DummyEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ForgeEventHandlers {
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Adds goal for monster entites to target dummies. Does not work for slimes
     */
    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent entityJoinEvent){
        Entity entity = entityJoinEvent.getEntity();
        if (entity instanceof MonsterEntity) {
            ((MonsterEntity) entity).targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(((MonsterEntity) entity), DummyEntity.class, true));
        }
    }

    /**
     * If there is an explosion generator nearby, the explosion effect is nullified and energy is created based on big the explosion is
     */
    @SubscribeEvent
    public void onExplosionEvent(ExplosionEvent.Detonate detonateEvent){
        for(BlockPos pos: detonateEvent.getAffectedBlocks()){
            if(detonateEvent.getWorld().getTileEntity(pos) instanceof ExplosionGeneratorTile){
                if(((ExplosionGeneratorTile) detonateEvent.getWorld().getTileEntity(pos)).onExplosion(detonateEvent)){
                    break;
                }
            }
        }
    }
}
