package github.pitbox46.oddpower.setup;

import github.pitbox46.oddpower.OddPower;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class RegistrationHelper<B extends Block, T extends TileEntity, C extends Container> {
    public RegistryObject<B> block;
    public RegistryObject<TileEntityType<T>> tile;
    public RegistryObject<BlockItem> blockItem;
    public RegistryObject<ContainerType<C>> container;

    static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, OddPower.MOD_ID);
    static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, OddPower.MOD_ID);
    static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, OddPower.MOD_ID);
    static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, OddPower.MOD_ID);
    static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, OddPower.MOD_ID);

    RegistrationHelper(String name, Supplier<B> blockSupplier, Supplier<T> tileSupplier, IContainerFactory<C> containerSupplier) {
        block = BLOCKS.register(name, blockSupplier);
        tile = TILE_ENTITIES.register(name + "_tile", () -> TileEntityType.Builder.create(tileSupplier, block.get()).build(null));
        blockItem = ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(OddPower.MOD_TAB)));
        container = CONTAINERS.register(name, () -> IForgeContainerType.create(containerSupplier));
    }
}
