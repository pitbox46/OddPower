package github.pitbox46.oddpower.setup;

import github.pitbox46.oddpower.OddPower;
import github.pitbox46.oddpower.common.DummyGeneratorItem;
import github.pitbox46.oddpower.common.ForgeEventHandlers;
import github.pitbox46.oddpower.common.ModEventHandlers;
import github.pitbox46.oddpower.entities.DummyGeneratorEntity;
import github.pitbox46.oddpower.entities.DummyGeneratorRenderer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Registration {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITIES, OddPower.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OddPower.MOD_ID);

    public static void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ENTITY_TYPE.register(modEventBus);
        ITEMS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
        modEventBus.register(new ModEventHandlers());

        LOGGER.debug("Hello from OddPower Registration");
    }

    public static final RegistryObject<EntityType<DummyGeneratorEntity>> DUMMY_GENERATOR = ENTITY_TYPE.register("dummy_generator",
            () -> EntityType.Builder.<DummyGeneratorEntity>create(DummyGeneratorEntity::new, EntityClassification.MISC)
            .size(0.5F, 1.975F).trackingRange(10)
            .build("dummy_generator"));
    public static final RegistryObject<DummyGeneratorItem> DUMMY_GENERATOR_ITEM = ITEMS.register("dummy_generator",
            () -> new DummyGeneratorItem(new Item.Properties().group(ItemGroup.COMBAT)));
}
