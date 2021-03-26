package github.pitbox46.oddpower.setup;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

public class Config {
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_POWER = "power";
    public static final String SUBCATEGORY_DUMMY = "dummy_generator";
    public static final String SUBCATEGORY_EXPLOSION = "explosion_generator";

    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.IntValue DUMMY_MAXPOWER;
    public static ForgeConfigSpec.IntValue DUMMY_GENERATE;
    public static ForgeConfigSpec.IntValue DUMMY_TRANSFER;
    public static ForgeConfigSpec.IntValue EXPLOSION_MAXPOWER;
    public static ForgeConfigSpec.IntValue EXPLOSION_GENERATE;
    public static ForgeConfigSpec.IntValue EXPLOSION_TRANSFER;
    public static ForgeConfigSpec.IntValue EXPLOSION_COOLDOWN;

    static {

        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        CLIENT_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);
        CLIENT_BUILDER.pop();

        SERVER_BUILDER.comment("Power Settings").push(CATEGORY_POWER);

        setupDummyGeneratorConfig(SERVER_BUILDER, CLIENT_BUILDER);
        setupExplosionGeneratorConfig(SERVER_BUILDER, CLIENT_BUILDER);

        SERVER_BUILDER.pop();


        SERVER_CONFIG = SERVER_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupDummyGeneratorConfig(ForgeConfigSpec.Builder SERVER_BUILDER, ForgeConfigSpec.Builder CLIENT_BUILDER) {
        SERVER_BUILDER.comment("Dummy Generator Settings").push(SUBCATEGORY_DUMMY);

        DUMMY_MAXPOWER = SERVER_BUILDER.comment("Base capacity")
                .defineInRange("maxPower", 64000, 0, Integer.MAX_VALUE);
        DUMMY_GENERATE = SERVER_BUILDER.comment("Power generation per kill")
                .defineInRange("generate", 2000, 0, Integer.MAX_VALUE);
        DUMMY_TRANSFER = SERVER_BUILDER.comment("Power transfer per tick")
                .defineInRange("transfer", 100, 0, Integer.MAX_VALUE);
        SERVER_BUILDER.pop();
    }

    private static void setupExplosionGeneratorConfig(ForgeConfigSpec.Builder SERVER_BUILDER, ForgeConfigSpec.Builder CLIENT_BUILDER) {
        SERVER_BUILDER.comment("Explosion Generator Settings").push(SUBCATEGORY_EXPLOSION);

        EXPLOSION_MAXPOWER = SERVER_BUILDER.comment("Base capacity")
                .defineInRange("maxPower", 64000, 0, Integer.MAX_VALUE);
        EXPLOSION_GENERATE = SERVER_BUILDER.comment("Power generation per block affected in explosion")
                .defineInRange("generate", 20, 0, Integer.MAX_VALUE);
        EXPLOSION_TRANSFER = SERVER_BUILDER.comment("Power transfer per tick")
                .defineInRange("transfer", 100, 0, Integer.MAX_VALUE);
        EXPLOSION_COOLDOWN = SERVER_BUILDER.comment("Cooldown in ticks")
                .defineInRange("cooldown", 80, 0, Integer.MAX_VALUE);
        SERVER_BUILDER.pop();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {
    }

}
