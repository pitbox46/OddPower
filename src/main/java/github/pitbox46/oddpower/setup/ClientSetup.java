package github.pitbox46.oddpower.setup;

import github.pitbox46.oddpower.OddPower;
import github.pitbox46.oddpower.entities.DummyRenderer;
import github.pitbox46.oddpower.gui.DummyGeneratorContainer;
import github.pitbox46.oddpower.gui.GeneratorScreen;
import github.pitbox46.oddpower.gui.IncineratorContainer;
import github.pitbox46.oddpower.gui.SlotlessGeneratorContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = OddPower.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void init(final FMLClientSetupEvent clientSetupEvent) {
        ScreenManager.<DummyGeneratorContainer, GeneratorScreen<DummyGeneratorContainer>>registerFactory(Registration.DUMMY_GENERATOR_CONTAINER.get(),
                (container, inv, name) -> new GeneratorScreen<>(container, inv, name, new ResourceLocation(OddPower.MOD_ID, "textures/gui/slot_generator_gui.png")));
        ScreenManager.<SlotlessGeneratorContainer, GeneratorScreen<SlotlessGeneratorContainer>>registerFactory(Registration.EXPLOSION_GENERATOR_CONTAINER.get(),
                (container, inv, name) -> new GeneratorScreen<>(container, inv, name, new ResourceLocation(OddPower.MOD_ID, "textures/gui/generator_gui.png")));
        ScreenManager.<IncineratorContainer, GeneratorScreen<IncineratorContainer>>registerFactory(Registration.INCINERATOR_GENERATOR_CONTAINER.get(),
                (container, inv, name) -> new GeneratorScreen<>(container, inv, name, new ResourceLocation(OddPower.MOD_ID, "textures/gui/slot_generator_gui.png")));
        ScreenManager.<SlotlessGeneratorContainer, GeneratorScreen<SlotlessGeneratorContainer>>registerFactory(Registration.PELTIER_GENERATOR_CONTAINER.get(),
                (container, inv, name) -> new GeneratorScreen<>(container, inv, name, new ResourceLocation(OddPower.MOD_ID, "textures/gui/generator_gui.png")));
        ScreenManager.<SlotlessGeneratorContainer, GeneratorScreen<SlotlessGeneratorContainer>>registerFactory(Registration.GRAVITY_GENERATOR_CONTAINER.get(),
                (container, inv, name) -> new GeneratorScreen<>(container, inv, name, new ResourceLocation(OddPower.MOD_ID, "textures/gui/generator_gui.png")));
        ScreenManager.<SlotlessGeneratorContainer, GeneratorScreen<SlotlessGeneratorContainer>>registerFactory(Registration.METHANE_GENERATOR_CONTAINER.get(),
                (container, inv, name) -> new GeneratorScreen<>(container, inv, name, new ResourceLocation(OddPower.MOD_ID, "textures/gui/generator_gui.png")));
        LOGGER.debug(Registration.EXPLOSION_GENERATOR_CONTAINER.get());
        RenderingRegistry.registerEntityRenderingHandler(Registration.DUMMY.get(), DummyRenderer::new);
    }
}
