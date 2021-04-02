package github.pitbox46.oddpower.setup;

import github.pitbox46.oddpower.OddPower;
import github.pitbox46.oddpower.blocks.*;
import github.pitbox46.oddpower.common.ForgeEventHandlers;
import github.pitbox46.oddpower.common.ModEventHandlers;
import github.pitbox46.oddpower.entities.DummyEntity;
import github.pitbox46.oddpower.gui.DummyGeneratorContainer;
import github.pitbox46.oddpower.gui.IncineratorContainer;
import github.pitbox46.oddpower.gui.SlotlessGeneratorContainer;
import github.pitbox46.oddpower.items.DummyItem;
import github.pitbox46.oddpower.items.UpgradeItem;
import github.pitbox46.oddpower.tiles.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.registry.Registry;

public class Registration {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        RegistrationHelper.ITEMS.register(modEventBus);
        RegistrationHelper.BLOCKS.register(modEventBus);
        RegistrationHelper.ENTITIES.register(modEventBus);
        RegistrationHelper.TILE_ENTITIES.register(modEventBus);
        RegistrationHelper.CONTAINERS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
        modEventBus.register(new ModEventHandlers());

        LOGGER.debug("Hello from OddPower Registration");
    }

    //Todo add tooltip for stored energy for item blocks
    //Dummy Generator
    private static final RegistrationHelper<DummyGenerator, DummyGeneratorTile, DummyGeneratorContainer> DUMMY_GENERATOR_HELPER
            = new RegistrationHelper<>("dummy_generator", DummyGenerator::new, DummyGeneratorTile::new,
            ((windowId, inv, data) -> new DummyGeneratorContainer(Registration.DUMMY_GENERATOR_CONTAINER.get(), windowId, data.readBlockPos(), inv, Registration.DUMMY_GENERATOR.get())));
    public static final RegistryObject<DummyGenerator> DUMMY_GENERATOR = DUMMY_GENERATOR_HELPER.block;
    public static final RegistryObject<TileEntityType<DummyGeneratorTile>> DUMMY_GENERATOR_TILE = DUMMY_GENERATOR_HELPER.tile;
    public static final RegistryObject<BlockItem> DUMMY_GENERATOR_ITEM = DUMMY_GENERATOR_HELPER.blockItem;
    public static final RegistryObject<ContainerType<DummyGeneratorContainer>> DUMMY_GENERATOR_CONTAINER = DUMMY_GENERATOR_HELPER.container;
    //Explosion Generator
    private static final RegistrationHelper<ExplosionGenerator, ExplosionGeneratorTile, SlotlessGeneratorContainer> EXPLOSION_HELPER
            = new RegistrationHelper<>("explosion_generator", ExplosionGenerator::new, ExplosionGeneratorTile::new,
            ((windowId, inv, data) -> new SlotlessGeneratorContainer(Registration.EXPLOSION_GENERATOR_CONTAINER.get(), windowId, data.readBlockPos(), inv, Registration.EXPLOSION_GENERATOR.get())));
    public static final RegistryObject<ExplosionGenerator> EXPLOSION_GENERATOR = EXPLOSION_HELPER.block;
    public static final RegistryObject<TileEntityType<ExplosionGeneratorTile>> EXPLOSION_GENERATOR_TILE = EXPLOSION_HELPER.tile;
    public static final RegistryObject<BlockItem> EXPLOSION_GENERATOR_ITEM = EXPLOSION_HELPER.blockItem;
    public static final RegistryObject<ContainerType<SlotlessGeneratorContainer>> EXPLOSION_GENERATOR_CONTAINER = EXPLOSION_HELPER.container;
    //Incinerator Generator
    private static final RegistrationHelper<IncineratorGenerator, IncineratorGeneratorTile, IncineratorContainer> INCINERATOR_HELPER
            = new RegistrationHelper<>("incinerator_generator", IncineratorGenerator::new, IncineratorGeneratorTile::new,
            ((windowId, inv, data) -> new IncineratorContainer(Registration.INCINERATOR_GENERATOR_CONTAINER.get(), windowId, data.readBlockPos(), inv, Registration.INCINERATOR_GENERATOR.get())));
    public static final RegistryObject<IncineratorGenerator> INCINERATOR_GENERATOR = INCINERATOR_HELPER.block;
    public static final RegistryObject<TileEntityType<IncineratorGeneratorTile>> INCINERATOR_GENERATOR_TILE = INCINERATOR_HELPER.tile;
    public static final RegistryObject<BlockItem> INCINERATOR_GENERATOR_ITEM = INCINERATOR_HELPER.blockItem;
    public static final RegistryObject<ContainerType<IncineratorContainer>> INCINERATOR_GENERATOR_CONTAINER = INCINERATOR_HELPER.container;
    //Peltier Generator
    private static final RegistrationHelper<PeltierGenerator, PeltierGeneratorTile, SlotlessGeneratorContainer> PELTIER_HELPER
            = new RegistrationHelper<>("peltier_generator", PeltierGenerator::new, PeltierGeneratorTile::new,
            ((windowId, inv, data) -> new SlotlessGeneratorContainer(Registration.PELTIER_GENERATOR_CONTAINER.get(), windowId, data.readBlockPos(), inv, Registration.PELTIER_GENERATOR.get())));
    public static final RegistryObject<PeltierGenerator> PELTIER_GENERATOR = PELTIER_HELPER.block;
    public static final RegistryObject<TileEntityType<PeltierGeneratorTile>> PELTIER_GENERATOR_TILE = PELTIER_HELPER.tile;
    public static final RegistryObject<BlockItem> PELTIER_GENERATOR_ITEM = PELTIER_HELPER.blockItem;
    public static final RegistryObject<ContainerType<SlotlessGeneratorContainer>> PELTIER_GENERATOR_CONTAINER = PELTIER_HELPER.container;
    //Gravity Generator
    private static final RegistrationHelper<GravityGenerator, GravityGeneratorTile, SlotlessGeneratorContainer> GRAVITY_HELPER
            = new RegistrationHelper<>("gravity_generator", GravityGenerator::new, GravityGeneratorTile::new,
            ((windowId, inv, data) -> new SlotlessGeneratorContainer(Registration.GRAVITY_GENERATOR_CONTAINER.get(), windowId, data.readBlockPos(), inv, Registration.GRAVITY_GENERATOR.get())));
    public static final RegistryObject<GravityGenerator> GRAVITY_GENERATOR = GRAVITY_HELPER.block;
    public static final RegistryObject<TileEntityType<GravityGeneratorTile>> GRAVITY_GENERATOR_TILE = GRAVITY_HELPER.tile;
    public static final RegistryObject<BlockItem> GRAVITY_GENERATOR_ITEM = GRAVITY_HELPER.blockItem;
    public static final RegistryObject<ContainerType<SlotlessGeneratorContainer>> GRAVITY_GENERATOR_CONTAINER = GRAVITY_HELPER.container;
    //Methane Generator
    private static final RegistrationHelper<MethaneGenerator, MethaneGeneratorTile, SlotlessGeneratorContainer> METHANE_HELPER
            = new RegistrationHelper<>("methane_generator", MethaneGenerator::new, MethaneGeneratorTile::new,
            ((windowId, inv, data) -> new SlotlessGeneratorContainer(Registration.METHANE_GENERATOR_CONTAINER.get(), windowId, data.readBlockPos(), inv, Registration.GRAVITY_GENERATOR.get())));
    public static final RegistryObject<MethaneGenerator> METHANE_GENERATOR = METHANE_HELPER.block;
    public static final RegistryObject<TileEntityType<MethaneGeneratorTile>> METHANE_GENERATOR_TILE = METHANE_HELPER.tile;
    public static final RegistryObject<BlockItem> METHANE_GENERATOR_ITEM = METHANE_HELPER.blockItem;
    public static final RegistryObject<ContainerType<SlotlessGeneratorContainer>> METHANE_GENERATOR_CONTAINER = METHANE_HELPER.container;
    //Dummy
    public static final RegistryObject<EntityType<DummyEntity>> DUMMY = RegistrationHelper.ENTITIES.register("dummy",
            () -> EntityType.Builder.create(DummyEntity::new, EntityClassification.MISC)
                    .size(0.5F, 2.0F).trackingRange(10).build("dummy"));
    public static final RegistryObject<DummyItem> DUMMY_ITEM = RegistrationHelper.ITEMS.register("dummy",
            () -> new DummyItem(new Item.Properties().group(OddPower.MOD_TAB)));
    //Upgrade Items
    public static final RegistryObject<UpgradeItem> CAPACITY_UPGRADE = RegistrationHelper.ITEMS.register("capacity_upgrade",
            () -> new UpgradeItem(new Item.Properties().group(OddPower.MOD_TAB)));
    public static final RegistryObject<UpgradeItem> PRODUCTION_UPGRADE = RegistrationHelper.ITEMS.register("production_upgrade",
            () -> new UpgradeItem(new Item.Properties().group(OddPower.MOD_TAB)));
}
