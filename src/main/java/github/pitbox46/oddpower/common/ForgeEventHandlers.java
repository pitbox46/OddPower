package github.pitbox46.oddpower.common;

import github.pitbox46.oddpower.entities.DummyEntity;
import github.pitbox46.oddpower.tiles.ExplosionGeneratorTile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
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
     * If there is an Explosion Generator nearby, call ExplosionGeneratorTile#onExplosion
     */
    @SubscribeEvent
    public void onExplosionEvent(ExplosionEvent.Detonate detonateEvent){
        for(BlockPos pos: detonateEvent.getAffectedBlocks()){
            if(detonateEvent.getWorld().getTileEntity(pos) instanceof ExplosionGeneratorTile &&
                    ((ExplosionGeneratorTile) detonateEvent.getWorld().getTileEntity(pos)).onExplosion(detonateEvent)) {
                break;
            }
        }
    }
}
