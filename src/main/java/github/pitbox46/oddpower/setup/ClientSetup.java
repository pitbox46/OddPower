package github.pitbox46.oddpower.setup;

import github.pitbox46.oddpower.OddPower;
import github.pitbox46.oddpower.blocks.MethaneGenerator;
import github.pitbox46.oddpower.entities.DummyEntity;
import github.pitbox46.oddpower.entities.DummyRenderer;
import github.pitbox46.oddpower.gui.DummyGeneratorContainer;
import github.pitbox46.oddpower.gui.GeneratorScreen;
import github.pitbox46.oddpower.gui.IncineratorContainer;
import github.pitbox46.oddpower.gui.SlotlessGeneratorContainer;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
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
        ScreenManager.<DummyGeneratorContainer, GeneratorScreen<DummyGeneratorContainer>>registerFactory((ContainerType<DummyGeneratorContainer>) Registration.DUMMY_GENERATOR.get("container").get(),
                (container, inv, name) -> new GeneratorScreen<>(container, inv, name, new ResourceLocation(OddPower.MOD_ID, "textures/gui/slot_generator_gui.png")));
        ScreenManager.<SlotlessGeneratorContainer, GeneratorScreen<SlotlessGeneratorContainer>>registerFactory((ContainerType<SlotlessGeneratorContainer>) Registration.EXPLOSION_GENERATOR.get("container").get(),
                (container, inv, name) -> new GeneratorScreen<>(container, inv, name, new ResourceLocation(OddPower.MOD_ID, "textures/gui/generator_gui.png")));
        ScreenManager.<IncineratorContainer, GeneratorScreen<IncineratorContainer>>registerFactory((ContainerType<IncineratorContainer>) Registration.INCINERATOR_GENERATOR.get("container").get(),
                (container, inv, name) -> new GeneratorScreen<>(container, inv, name, new ResourceLocation(OddPower.MOD_ID, "textures/gui/slot_generator_gui.png")));
        ScreenManager.<SlotlessGeneratorContainer, GeneratorScreen<SlotlessGeneratorContainer>>registerFactory((ContainerType<SlotlessGeneratorContainer>) Registration.PELTIER_GENERATOR.get("container").get(),
                (container, inv, name) -> new GeneratorScreen<>(container, inv, name, new ResourceLocation(OddPower.MOD_ID, "textures/gui/generator_gui.png")));
        ScreenManager.<SlotlessGeneratorContainer, GeneratorScreen<SlotlessGeneratorContainer>>registerFactory((ContainerType<SlotlessGeneratorContainer>) Registration.GRAVITY_GENERATOR.get("container").get(),
                (container, inv, name) -> new GeneratorScreen<>(container, inv, name, new ResourceLocation(OddPower.MOD_ID, "textures/gui/generator_gui.png")));
        ScreenManager.<SlotlessGeneratorContainer, GeneratorScreen<SlotlessGeneratorContainer>>registerFactory((ContainerType<SlotlessGeneratorContainer>) Registration.METHANE_GENERATOR.get("container").get(),
                (container, inv, name) -> new GeneratorScreen<>(container, inv, name, new ResourceLocation(OddPower.MOD_ID, "textures/gui/generator_gui.png")));
        RenderingRegistry.registerEntityRenderingHandler((EntityType<? extends DummyEntity>) Registration.DUMMY.get("entity").get(), DummyRenderer::new);
        RenderTypeLookup.setRenderLayer((Block) Registration.METHANE_GENERATOR.get("block").get(), RenderType.getTranslucent());
    }
}
