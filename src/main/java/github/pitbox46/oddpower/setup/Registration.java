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
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Registration {
    private static final Logger LOGGER = LogManager.getLogger();

    static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OddPower.MOD_ID);
    static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, OddPower.MOD_ID);
    static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, OddPower.MOD_ID);
    static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, OddPower.MOD_ID);
    static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, OddPower.MOD_ID);

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

    /* Generators Todo add tooltip for stored energy for item blocks */
    public static final Map<String, RegistryObject<?>> DUMMY_GENERATOR = registerGenerator("dummy_generator", DummyGenerator::new, DummyGeneratorTile::new,
            ((windowId, inv, data) -> new DummyGeneratorContainer((ContainerType<?>) Registration.DUMMY_GENERATOR.get("container").get(), windowId, data.readBlockPos(), inv, (Block) Registration.DUMMY_GENERATOR.get("block").get())));
    public static final Map<String, RegistryObject<?>> EXPLOSION_GENERATOR = registerGenerator("explosion_generator", ExplosionGenerator::new, ExplosionGeneratorTile::new,
            ((windowId, inv, data) -> new SlotlessGeneratorContainer((ContainerType<?>) Registration.EXPLOSION_GENERATOR.get("container").get(), windowId, data.readBlockPos(), inv, (Block) Registration.EXPLOSION_GENERATOR.get("block").get())));
    public static final Map<String, RegistryObject<?>> INCINERATOR_GENERATOR = registerGenerator("incinerator_generator", IncineratorGenerator::new, IncineratorGeneratorTile::new,
            ((windowId, inv, data) -> new IncineratorContainer((ContainerType<?>) Registration.INCINERATOR_GENERATOR.get("container").get(), windowId, data.readBlockPos(), inv, (Block) Registration.INCINERATOR_GENERATOR.get("block").get())));
    public static final Map<String, RegistryObject<?>> PELTIER_GENERATOR = registerGenerator("peltier_generator", PeltierGenerator::new, PeltierGeneratorTile::new,
            ((windowId, inv, data) -> new SlotlessGeneratorContainer((ContainerType<?>) Registration.PELTIER_GENERATOR.get("container").get(), windowId, data.readBlockPos(), inv, (Block) Registration.PELTIER_GENERATOR.get("block").get())));
    public static final Map<String, RegistryObject<?>> GRAVITY_GENERATOR = registerGenerator("gravity_generator", GravityGenerator::new, GravityGeneratorTile::new,
            ((windowId, inv, data) -> new SlotlessGeneratorContainer((ContainerType<?>) Registration.GRAVITY_GENERATOR.get("container").get(), windowId, data.readBlockPos(), inv, (Block) Registration.GRAVITY_GENERATOR.get("block").get())));
    public static final Map<String, RegistryObject<?>> METHANE_GENERATOR = registerGenerator("methane_generator", MethaneGenerator::new, MethaneGeneratorTile::new,
            ((windowId, inv, data) -> new SlotlessGeneratorContainer((ContainerType<?>) Registration.METHANE_GENERATOR.get("container").get(), windowId, data.readBlockPos(), inv, (Block) Registration.METHANE_GENERATOR.get("block").get())));

    /* Entities */
    public static final Map<String, RegistryObject<?>> DUMMY = new HashMap<>();
    static {
        DUMMY.put("entity", ENTITIES.register("dummy", () -> EntityType.Builder.create(DummyEntity::new, EntityClassification.MISC)
                .size(0.5F, 2.0F).trackingRange(10).build("dummy")));
        DUMMY.put("spawnegg", ITEMS.register("dummy", () -> new DummyItem(new Item.Properties().group(OddPower.MOD_TAB))));
    }

    /* Items */
    public static final RegistryObject<UpgradeItem> CAPACITY_UPGRADE = ITEMS.register("capacity_upgrade",
            () -> new UpgradeItem(new Item.Properties().group(OddPower.MOD_TAB)));
    public static final RegistryObject<UpgradeItem> PRODUCTION_UPGRADE = ITEMS.register("production_upgrade",
            () -> new UpgradeItem(new Item.Properties().group(OddPower.MOD_TAB)));

    public static <B extends Block, T extends TileEntity, C extends Container> Map<String, RegistryObject<?>> registerGenerator(String name, Supplier<B> blockSupplier, Supplier<T> tileSupplier, IContainerFactory<C> containerSupplier) {
        Map<String, RegistryObject<?>> map = new HashMap<>();
        map.put("block", BLOCKS.register(name, blockSupplier));
        map.put("tile", TILE_ENTITIES.register(name+"_tile", () -> TileEntityType.Builder.create(tileSupplier, (Block) map.get("block").get()).build(null)));
        map.put("blockItem", ITEMS.register(name, () -> new BlockItem((Block) map.get("block").get(), new Item.Properties().group(OddPower.MOD_TAB))));
        map.put("container", CONTAINERS.register(name, () -> IForgeContainerType.create(containerSupplier)));
        return map;
    }
}
