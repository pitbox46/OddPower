package github.pitbox46.oddpower.setup;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.arguments.BlockStateParser;
import net.minecraft.command.arguments.ItemParser;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.CallbackI;

import java.util.*;
import java.util.function.Predicate;

public class Config {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_POWER = "power";
    public static final String SUBCATEGORY_DUMMY = "dummy_generator";
    public static final String SUBCATEGORY_EXPLOSION = "explosion_generator";
    public static final String SUBCATEGORY_INCINERATOR = "incinerator";
    public static final String SUBCATEGORY_PELTIER = "peltier_generator";
    public static final String SUBCATEGORY_BLOCK_TEMPERATURES = "block_temperatures";

    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.IntValue DUMMY_MAXPOWER;
    public static ForgeConfigSpec.IntValue DUMMY_GENERATE;
    public static ForgeConfigSpec.IntValue DUMMY_TRANSFER;
    public static ForgeConfigSpec.IntValue EXPLOSION_MAXPOWER;
    public static ForgeConfigSpec.IntValue EXPLOSION_GENERATE;
    public static ForgeConfigSpec.IntValue EXPLOSION_TRANSFER;
    public static ForgeConfigSpec.IntValue EXPLOSION_COOLDOWN;
    public static ForgeConfigSpec.IntValue INCINERATOR_MAXPOWER;
    public static ForgeConfigSpec.IntValue INCINERATOR_GENERATE;
    public static ForgeConfigSpec.IntValue INCINERATOR_TRANSFER;
    public static ForgeConfigSpec.IntValue INCINERATOR_COOLDOWN;
    public static ForgeConfigSpec.IntValue PELTIER_MAXPOWER;
    public static ForgeConfigSpec.DoubleValue PELTIER_GENERATE;
    public static ForgeConfigSpec.IntValue PELTIER_TRANSFER;
    public static ForgeConfigSpec.IntValue GRAVITY_MAXPOWER;
    public static ForgeConfigSpec.DoubleValue GRAVITY_GENERATE;
    public static ForgeConfigSpec.IntValue GRAVITY_TRANSFER;

    private static ForgeConfigSpec.ConfigValue<ArrayList<String>> TEMP_VALUES;
    private static final ArrayList<String> DEFAULT_TEMP_VALUES = new ArrayList<>();
    @SuppressWarnings("unchecked")
    private static final Predicate<Object> TEMP_VALUES_VALIDATOR = (array) -> {
        if(array instanceof ArrayList<?>) {
            for (String s : (ArrayList<String>) array) {
                try {
                    Integer.parseInt(s.split("=")[1]);
                } catch (NumberFormatException e) {
                    return false;
                }
            }
            return true;
        }
        return false;
    };

    static {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        CLIENT_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);
        CLIENT_BUILDER.pop();

        SERVER_BUILDER.comment("Power Settings").push(CATEGORY_POWER);

        setupDummyGeneratorConfig(SERVER_BUILDER);
        setupExplosionGeneratorConfig(SERVER_BUILDER);
        setupInceneratorConfig(SERVER_BUILDER);
        setupPeltierGeneratorConfig(SERVER_BUILDER);
        setupPeltierBlocksConfig(SERVER_BUILDER);
        setupGravityGeneratorConfig(SERVER_BUILDER);

        SERVER_BUILDER.pop();


        SERVER_CONFIG = SERVER_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupDummyGeneratorConfig(ForgeConfigSpec.Builder SERVER_BUILDER) {
        SERVER_BUILDER.comment("Dummy Generator Settings").push(SUBCATEGORY_DUMMY);

        DUMMY_MAXPOWER = SERVER_BUILDER.comment("Base capacity")
                .defineInRange("maxPower", 64000, 0, Integer.MAX_VALUE);
        DUMMY_GENERATE = SERVER_BUILDER.comment("Power generation per kill")
                .defineInRange("generate", 2000, 0, Integer.MAX_VALUE);
        DUMMY_TRANSFER = SERVER_BUILDER.comment("Power transfer per tick")
                .defineInRange("transfer", 1000, 0, Integer.MAX_VALUE);
        SERVER_BUILDER.pop();
    }

    private static void setupExplosionGeneratorConfig(ForgeConfigSpec.Builder SERVER_BUILDER) {
        SERVER_BUILDER.comment("Explosion Generator Settings").push(SUBCATEGORY_EXPLOSION);

        EXPLOSION_MAXPOWER = SERVER_BUILDER.comment("Base capacity")
                .defineInRange("maxPower", 64000, 0, Integer.MAX_VALUE);
        EXPLOSION_GENERATE = SERVER_BUILDER.comment("Power generation per block affected in explosion")
                .defineInRange("generate", 20, 0, Integer.MAX_VALUE);
        EXPLOSION_TRANSFER = SERVER_BUILDER.comment("Power transfer per tick")
                .defineInRange("transfer", 1000, 0, Integer.MAX_VALUE);
        EXPLOSION_COOLDOWN = SERVER_BUILDER.comment("Cooldown in ticks")
                .defineInRange("cooldown", 80, 0, Integer.MAX_VALUE);
        SERVER_BUILDER.pop();
    }

    private static void setupInceneratorConfig(ForgeConfigSpec.Builder SERVER_BUILDER) {
        SERVER_BUILDER.comment("Incinerator Settings").push(SUBCATEGORY_INCINERATOR);

        INCINERATOR_MAXPOWER = SERVER_BUILDER.comment("Base capacity")
                .defineInRange("maxPower", 64000, 0, Integer.MAX_VALUE);
        INCINERATOR_GENERATE = SERVER_BUILDER.comment("Power generation per item destroyed")
                .defineInRange("generate", 400, 0, Integer.MAX_VALUE);
        INCINERATOR_TRANSFER = SERVER_BUILDER.comment("Power transfer per tick")
                .defineInRange("transfer", 1000, 0, Integer.MAX_VALUE);
        INCINERATOR_COOLDOWN = SERVER_BUILDER.comment("Time to incinerate items in ticks")
                .defineInRange("cooldown", 20, 1, Integer.MAX_VALUE);
        SERVER_BUILDER.pop();
    }

    private static void setupPeltierGeneratorConfig(ForgeConfigSpec.Builder SERVER_BUILDER) {
        SERVER_BUILDER.comment("Peltier Generator Settings").push(SUBCATEGORY_PELTIER);

        PELTIER_MAXPOWER = SERVER_BUILDER.comment("Base capacity")
                .defineInRange("maxPower", 64000, 0, Integer.MAX_VALUE);
        PELTIER_GENERATE = SERVER_BUILDER.comment("Power generation multiplier. Can be decimal")
                .defineInRange("generate", 1, 0, Double.MAX_VALUE);
        PELTIER_TRANSFER = SERVER_BUILDER.comment("Power transfer per tick")
                .defineInRange("transfer", 1000, 0, Integer.MAX_VALUE);
        SERVER_BUILDER.pop();
    }

    private static void setupPeltierBlocksConfig(ForgeConfigSpec.Builder SERVER_BUILDER) {
        SERVER_BUILDER.comment("Block Temperature Values for Peltier Generator. Write in any block that you would like").push(SUBCATEGORY_BLOCK_TEMPERATURES);

        DEFAULT_TEMP_VALUES.add("minecraft:lava=320");
        DEFAULT_TEMP_VALUES.add("minecraft:flowing_lava=160");
        DEFAULT_TEMP_VALUES.add("minecraft:fire=20");
        DEFAULT_TEMP_VALUES.add("minecraft:water=-10");
        DEFAULT_TEMP_VALUES.add("minecraft:flowing_water=-20");
        DEFAULT_TEMP_VALUES.add("minecraft:ice=-40");
        DEFAULT_TEMP_VALUES.add("minecraft:packed_ice=-160");
        DEFAULT_TEMP_VALUES.add("minecraft:blue_ice=-640");

        TEMP_VALUES = SERVER_BUILDER.comment("Temperature values. You may add your own entries").define("temp_values", DEFAULT_TEMP_VALUES, TEMP_VALUES_VALIDATOR);
    }

    private static void setupGravityGeneratorConfig(ForgeConfigSpec.Builder SERVER_BUILDER) {
        SERVER_BUILDER.comment("Gravity Generator Settings").push(SUBCATEGORY_EXPLOSION);

        GRAVITY_MAXPOWER = SERVER_BUILDER.comment("Base capacity")
                .defineInRange("maxPower", 64000, 0, Integer.MAX_VALUE);
        GRAVITY_GENERATE = SERVER_BUILDER.comment("Power generation multiplier. Can be decimal")
                .defineInRange("generate", 10, 0, Double.MAX_VALUE);
        GRAVITY_TRANSFER = SERVER_BUILDER.comment("Power transfer per tick")
                .defineInRange("transfer", 1000, 0, Integer.MAX_VALUE);
        SERVER_BUILDER.pop();
    }

    public static HashMap<Block, Integer> readTempArray() {
        HashMap<Block, Integer> map = new HashMap<>();
        for(String s: TEMP_VALUES.get()) {
            String[] sArray = s.split("=");
            StringReader reader = new StringReader(sArray[0]);
            BlockStateParser parser = new BlockStateParser(reader, false);
            try{
                map.put(parser.parse(true).getState().getBlock(), Integer.parseInt(sArray[1]));
            } catch (NullPointerException | CommandSyntaxException e) {
                LOGGER.warn(sArray[0] + " not recognized");
            }
        }
        return map;
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {
    }

}