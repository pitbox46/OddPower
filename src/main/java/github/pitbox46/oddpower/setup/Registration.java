package github.pitbox46.oddpower.setup;

import github.pitbox46.oddpower.OddPower;
import github.pitbox46.oddpower.blocks.DummyGenerator;
import github.pitbox46.oddpower.blocks.ExplosionGenerator;
import github.pitbox46.oddpower.blocks.IncineratorGenerator;
import github.pitbox46.oddpower.common.ForgeEventHandlers;
import github.pitbox46.oddpower.common.ModEventHandlers;
import github.pitbox46.oddpower.entities.DummyEntity;
import github.pitbox46.oddpower.gui.DummyGeneratorContainer;
import github.pitbox46.oddpower.gui.IncineratorContainer;
import github.pitbox46.oddpower.gui.SlotlessGeneratorContainer;
import github.pitbox46.oddpower.items.DummyItem;
import github.pitbox46.oddpower.items.UpgradeItem;
import github.pitbox46.oddpower.tiles.DummyGeneratorTile;
import github.pitbox46.oddpower.tiles.ExplosionGeneratorTile;
import github.pitbox46.oddpower.tiles.IncineratorGeneratorTile;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Registration {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OddPower.MOD_ID);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, OddPower.MOD_ID);
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, OddPower.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, OddPower.MOD_ID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, OddPower.MOD_ID);

    public static void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        ENTITIES.register(modEventBus);
        TILE_ENTITIES.register(modEventBus);
        CONTAINERS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
        modEventBus.register(new ModEventHandlers());

        LOGGER.debug("Hello from OddPower Registration");
    }

    //Todo add tooltip for stored energy for item blocks
    //Dummy Generator
    public static final RegistryObject<DummyGenerator> DUMMY_GENERATOR = BLOCKS.register("dummy_generator", DummyGenerator::new);
    public static final RegistryObject<TileEntityType<DummyGeneratorTile>> DUMMY_GENERATOR_TILE = TILE_ENTITIES.register("dummy_generator_tile",
            () -> TileEntityType.Builder.create(DummyGeneratorTile::new, DUMMY_GENERATOR.get()).build(null));
    public static final RegistryObject<BlockItem> DUMMY_GENERATOR_ITEM = ITEMS.register("dummy_generator",
            () -> new BlockItem(DUMMY_GENERATOR.get(), new Item.Properties().group(OddPower.MOD_TAB)));
    public static final RegistryObject<ContainerType<DummyGeneratorContainer>> DUMMY_GENERATOR_CONTAINER = CONTAINERS.register("dummy_generator",
            () -> IForgeContainerType.create((windowID, inv, data) -> new DummyGeneratorContainer(Registration.DUMMY_GENERATOR_CONTAINER.get(), windowID, data.readBlockPos(), inv, Registration.DUMMY_GENERATOR.get())));
    //Explosion Generator
    public static final RegistryObject<ExplosionGenerator> EXPLOSION_GENERATOR = BLOCKS.register("explosion_generator", ExplosionGenerator::new);
    public static final RegistryObject<TileEntityType<ExplosionGeneratorTile>> EXPLOSION_GENERATOR_TILE = TILE_ENTITIES.register("explosion_generator_tile",
            () -> TileEntityType.Builder.create(ExplosionGeneratorTile::new, EXPLOSION_GENERATOR.get()).build(null));
    public static final RegistryObject<BlockItem> EXPLOSION_GENERATOR_ITEM = ITEMS.register("explosion_generator",
            () -> new BlockItem(EXPLOSION_GENERATOR.get(), new Item.Properties().group(OddPower.MOD_TAB)));
    public static final RegistryObject<ContainerType<SlotlessGeneratorContainer>> EXPLOSION_GENERATOR_CONTAINER = CONTAINERS.register("explosion_generator",
            () -> IForgeContainerType.create((windowID, inv, data) -> new SlotlessGeneratorContainer(Registration.EXPLOSION_GENERATOR_CONTAINER.get(), windowID, data.readBlockPos(), inv, Registration.EXPLOSION_GENERATOR.get())));
    //Incinerator Generator
    public static final RegistryObject<IncineratorGenerator> INCINERATOR_GENERATOR = BLOCKS.register("incinerator_generator", IncineratorGenerator::new);
    public static final RegistryObject<TileEntityType<IncineratorGeneratorTile>> INCINERATOR_GENERATOR_TILE = TILE_ENTITIES.register("incinerator_generator_tile",
            () -> TileEntityType.Builder.create(IncineratorGeneratorTile::new, INCINERATOR_GENERATOR.get()).build(null));
    public static final RegistryObject<BlockItem> INCINERATOR_GENERATOR_ITEM = ITEMS.register("incinerator_generator",
            () -> new BlockItem(INCINERATOR_GENERATOR.get(), new Item.Properties().group(OddPower.MOD_TAB)));
    public static final RegistryObject<ContainerType<IncineratorContainer>> INCINERATOR_GENERATOR_CONTAINER = CONTAINERS.register("incinerator_generator",
            () -> IForgeContainerType.create((windowID, inv, data) -> new IncineratorContainer(Registration.INCINERATOR_GENERATOR_CONTAINER.get(), windowID, data.readBlockPos(), inv, Registration.INCINERATOR_GENERATOR.get())));
    //Dummy
    public static final RegistryObject<EntityType<DummyEntity>> DUMMY = ENTITIES.register("dummy",
            () -> EntityType.Builder.create(DummyEntity::new, EntityClassification.MISC)
                    .size(0.5F, 2.0F).trackingRange(10).build("dummy"));
    public static final RegistryObject<DummyItem> DUMMY_ITEM = ITEMS.register("dummy",
            () -> new DummyItem(new Item.Properties().group(OddPower.MOD_TAB)));
    //Upgrade Items
    public static final RegistryObject<UpgradeItem> CAPACITY_UPGRADE = ITEMS.register("capacity_upgrade",
            () -> new UpgradeItem(new Item.Properties().group(OddPower.MOD_TAB)));
    public static final RegistryObject<UpgradeItem> PRODUCTION_UPGRADE = ITEMS.register("production_upgrade",
            () -> new UpgradeItem(new Item.Properties().group(OddPower.MOD_TAB)));
}
