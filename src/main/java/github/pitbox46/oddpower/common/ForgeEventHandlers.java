package github.pitbox46.oddpower.common;

import github.pitbox46.oddpower.entities.DummyEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ForgeEventHandlers {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent entityJoinEvent){
        Entity entity = entityJoinEvent.getEntity();
        if (entity instanceof MonsterEntity) {
            ((MonsterEntity) entity).targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(((MonsterEntity) entity), DummyEntity.class, true));
        }
    }
}
