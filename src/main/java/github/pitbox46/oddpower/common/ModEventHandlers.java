package github.pitbox46.oddpower.common;

import github.pitbox46.oddpower.entities.DummyEntity;
import github.pitbox46.oddpower.setup.Registration;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModEventHandlers {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(Registration.DUMMY.get(), DummyEntity.registerAttributes().create());
    }
}
