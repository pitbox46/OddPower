package github.pitbox46.oddpower.setup;

import github.pitbox46.oddpower.OddPower;
import github.pitbox46.oddpower.blocks.GenericGeneratorScreen;
import github.pitbox46.oddpower.blocks.SlotGeneratorContainer;
import github.pitbox46.oddpower.blocks.SlotGeneratorScreen;
import github.pitbox46.oddpower.entities.DummyRenderer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = OddPower.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    public static void init(final FMLClientSetupEvent clientSetupEvent) {
        ScreenManager.registerFactory(Registration.DUMMY_GENERATOR_CONTAINER.get(), SlotGeneratorScreen::new);
        ScreenManager.registerFactory(Registration.EXPLOSION_GENERATOR_CONTAINER.get(), GenericGeneratorScreen::new);
        RenderingRegistry.registerEntityRenderingHandler(Registration.DUMMY.get(), DummyRenderer::new);
    }
}
