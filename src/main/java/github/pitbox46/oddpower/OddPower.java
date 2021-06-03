package github.pitbox46.oddpower;

import github.pitbox46.oddpower.items.DummyItem;
import github.pitbox46.oddpower.network.ClientProxy;
import github.pitbox46.oddpower.network.CommonProxy;
import github.pitbox46.oddpower.setup.ClientSetup;
import github.pitbox46.oddpower.setup.Config;
import github.pitbox46.oddpower.setup.Registration;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(OddPower.MOD_ID)
public class OddPower
{
    public static final String MOD_ID = "oddpower";
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    public static CommonProxy PROXY;

    public static ItemGroup MOD_TAB = new ItemGroup("oddpower") {
        public ItemStack createIcon() {
            return new ItemStack((DummyItem) Registration.DUMMY.get("spawnegg").get());
        }
    };

    public OddPower() {
        PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);

        Registration.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
