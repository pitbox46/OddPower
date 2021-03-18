package github.pitbox46.oddpower.setup;

import github.pitbox46.oddpower.OddPower;
import github.pitbox46.oddpower.blocks.DummyGenerator;
import github.pitbox46.oddpower.common.DummyItem;
import github.pitbox46.oddpower.common.ForgeEventHandlers;
import github.pitbox46.oddpower.common.ModEventHandlers;
import github.pitbox46.oddpower.entities.DummyEntity;
import github.pitbox46.oddpower.blocks.DummyGeneratorTile;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
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

    public static final RegistryObject<DummyGenerator> DUMMY_GENERATOR = BLOCKS.register("dummy_generator", DummyGenerator::new);
    public static final RegistryObject<TileEntityType<DummyGeneratorTile>> DUMMY_GENERATOR_TILE = TILE_ENTITIES.register("dummy_generator_tile",
            () -> TileEntityType.Builder.create(DummyGeneratorTile::new, DUMMY_GENERATOR.get()).build(null));
    public static final RegistryObject<BlockItem> DUMMY_GENERATOR_ITEM = ITEMS.register("dummy_generator",
            () -> new BlockItem(DUMMY_GENERATOR.get(), new Item.Properties().group(ItemGroup.COMBAT)));

    public static final RegistryObject<EntityType<DummyEntity>> DUMMY = ENTITIES.register("dummy",
            () -> EntityType.Builder.<DummyEntity>create(DummyEntity::new, EntityClassification.MISC)
                    .size(0.5F, 1.975F).trackingRange(10).build("dummy"));
    public static final RegistryObject<DummyItem> DUMMY_ITEM = ITEMS.register("dummy", DummyItem::new);
}
