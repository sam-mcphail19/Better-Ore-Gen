package me.sammc19.betteroregen.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import me.sammc19.betteroregen.BetterOreGen;
import me.sammc19.betteroregen.generation.OreVein;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import static me.sammc19.betteroregen.BetterOreGen.LOGGER;


public class BetterOreGenConfig {

    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("betteroregen.json");
    public static final Set<String> oreFeaturesToRemove = new HashSet<>(
        List.of(
            "ore_gold_deltas",
            "ore_quartz_deltas",
            "ore_gold_nether",
            "ore_quartz_nether",
            "ore_coal",
            "prototype_ore_coal_upper",
            "prototype_ore_coal_lower",
            "ore_iron",
            "prototype_ore_iron_upper",
            "prototype_ore_iron_middle",
            "prototype_ore_iron_small",
            "ore_gold_extra",
            "ore_gold",
            "prototype_ore_gold",
            "ore_redstone",
            "prototype_ore_redstone",
            "prototype_ore_redstone_lower",
            "ore_diamond",
            "prototype_ore_diamond",
            "prototype_ore_diamond_large",
            "ore_lapis",
            "prototype_ore_lapis",
            "prototype_ore_lapis_buried",
            "ore_emerald",
            "prototype_ore_emerald",
            "ore_copper",
            "prototype_ore_copper"
        )
    );
    private static final int WORLD_HEIGHT = 256;
    public static String BASE_FREQUENCY_KEY = "base_frequency";
    public static String NAME_KEY = "name";
    public static String BLOCKS_KEY = "blocks";
    public static String REPLACEABLE_BLOCKS_KEY = "replaceable_blocks";
    public static String Y_MIN_KEY = "y_min";
    public static String Y_MAX_KEY = "y_max";
    public static String SIZE_KEY = "size";
    public static String FREQUENCY_KEY = "frequency";
    public static String DENSITY_KEY = "density";
    public static String DIMENSIONS_KEY = "dimensions";
    public static String BIOMES_KEY = "biomes";
    public static String VEINS_KEY = "veins";
    public static boolean isConfigLoaded = false;
    private static double baseFrequency;
    private static File config;
    private static JsonObject json;
    private static Gson gson;

    public static void init() {
        if (isConfigLoaded)
            return;

        config = new File(CONFIG_PATH.toString());
        load();

        gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

        LOGGER.info("BetterOreGen Initialized");
        isConfigLoaded = true;
    }

    public static void load() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(config));
            json = new JsonParser().parse(br).getAsJsonObject();
        } catch (FileNotFoundException e) {
            LOGGER.error("BetterOreGen Config Not Found", e);
        }

        baseFrequency = json.get(BASE_FREQUENCY_KEY).getAsDouble();

        JsonArray veins = json.get("veins").getAsJsonArray();
        for (int i = 0; i < veins.size(); i++) {
            try {
                OreVein newVein = readVein(veins.get(i).getAsJsonObject());
                BetterOreGen.oreVeins.add(newVein);
            } catch (IllegalArgumentException e) {
                LOGGER.error(e);
            }
        }
    }

    public static void save() {
        JsonObject betterOreGenConfig = new JsonObject();
        betterOreGenConfig.add(BASE_FREQUENCY_KEY, new JsonPrimitive(baseFrequency));
        betterOreGenConfig.add(VEINS_KEY, new JsonArray());
        for (OreVein oreVein : BetterOreGen.oreVeins) {
            JsonObject oreVeinJson = new JsonObject();
            oreVeinJson.add(NAME_KEY, new JsonPrimitive(oreVein.name));
            oreVeinJson.add(BLOCKS_KEY, createJsonBlocksList(oreVein));
            oreVeinJson.add(REPLACEABLE_BLOCKS_KEY, createReplaceableBlocksList(oreVein));
            oreVeinJson.add(Y_MIN_KEY, new JsonPrimitive(oreVein.yMin));
            oreVeinJson.add(Y_MAX_KEY, new JsonPrimitive(oreVein.yMax));
            oreVeinJson.add(SIZE_KEY, new JsonPrimitive(oreVein.size));
            oreVeinJson.add(FREQUENCY_KEY, new JsonPrimitive(roundDoubleToDecimalPlaces(oreVein.frequency, 3)));
            oreVeinJson.add(DENSITY_KEY, new JsonPrimitive(roundDoubleToDecimalPlaces(oreVein.density, 3)));
            oreVeinJson.add(DIMENSIONS_KEY, gson.toJsonTree(oreVein.dimensions).getAsJsonArray());
            oreVeinJson.add(BIOMES_KEY, gson.toJsonTree(oreVein.biomes).getAsJsonArray());

            betterOreGenConfig.get(VEINS_KEY).getAsJsonArray().add(oreVeinJson);
        }

        try {
            FileWriter fileWriter = new FileWriter(config);
            fileWriter.write(gson.toJson(betterOreGenConfig));
            fileWriter.close();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    private static OreVein readVein(JsonObject jsonObject) {
        OreVein oreVein = new OreVein.Builder()
            .withName(jsonObject.get(NAME_KEY).getAsString())
            .withBlocks(readBlocks(jsonObject.get(BLOCKS_KEY).getAsJsonArray()))
            .withBlockWeights(readBlockWeights(jsonObject.get(BLOCKS_KEY).getAsJsonArray()))
            .withReplaceableBlocks(readReplaceableBlocks(jsonObject.get(REPLACEABLE_BLOCKS_KEY).getAsJsonArray()))
            .withYMin(readYMin(jsonObject.get(Y_MIN_KEY).getAsInt()))
            .withYMax(readYMax(jsonObject.get(Y_MAX_KEY).getAsInt()))
            .withSize(readSize(jsonObject.get(SIZE_KEY).getAsInt()))
            .withFrequency(readFrequency(jsonObject.get(FREQUENCY_KEY).getAsDouble()))
            .withDensity(readDensity(jsonObject.get(DENSITY_KEY).getAsDouble()))
            .withDimensions(readDimensions(jsonObject.get(DIMENSIONS_KEY).getAsJsonArray()))
            .withBiomes(readBiomes(jsonObject.get(BIOMES_KEY).getAsJsonArray()))
            .build();

        if (oreVein.yMin > oreVein.yMax) {
            throw new IllegalArgumentException("Invalid y_min/y_max: (" + oreVein.yMin + ", " + oreVein.yMax + "), y_max must be greater than y_min.");
        }

        return oreVein;
    }

    private static ArrayList<Block> readBlocks(JsonArray jsonArray) {
        if (jsonArray.size() % 2 == 1) {
            throw new IllegalArgumentException("Invalid blocks array: " + jsonArray + ", blocks array must be even length.");
        }

        ArrayList<Block> blocks = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i += 2) {
            blocks.add(parseBlock(jsonArray.get(i).getAsString()));
        }
        return blocks;
    }

    private static ArrayList<Integer> readBlockWeights(JsonArray jsonArray) {
        if (jsonArray.size() % 2 == 1) {
            throw new IllegalArgumentException("Invalid blocks array: " + jsonArray + ", blocks array must be even length.");
        }

        ArrayList<Integer> blockWeights = new ArrayList<>();
        int sum = 0;

        for (int i = 1; i < jsonArray.size(); i += 2) {
            int blockWeight = jsonArray.get(i).getAsInt();
            blockWeights.add(blockWeight);
            sum += blockWeight;
        }

        if (sum != 100) {
            throw new IllegalArgumentException("Invalid blocks array: " + jsonArray + ", block weights must sum to 100.");
        }
        return blockWeights;
    }

    private static ArrayList<Block> readReplaceableBlocks(JsonArray jsonArray) {
        ArrayList<Block> replaceableBlocks = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            replaceableBlocks.add(parseBlock(jsonArray.get(i).getAsString()));
        }
        return replaceableBlocks;
    }

    /*
        "minecraft:plains",
        "DRY",
        "ARID"
     */
    private static ArrayList<Biome> readBiomes(JsonArray jsonArray) {
        ArrayList<Biome> biomes = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            //biomes.add(jsonArray.get(i).getAsString());
        }

        return biomes;
    }

    private static ArrayList<Integer> readDimensions(JsonArray jsonArray) {
        ArrayList<Integer> dimensions = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            dimensions.add(jsonArray.get(i).getAsInt());
        }

        return dimensions;
    }

    private static int readYMin(int yMin) {
        if (yMin < 0 || yMin > WORLD_HEIGHT) {
            throw new IllegalArgumentException("Invalid y_min: " + yMin + ", y_min must be between 0 and " + WORLD_HEIGHT + ".");
        }
        return yMin;
    }

    private static int readYMax(int yMax) {
        if (yMax < 0 || yMax > WORLD_HEIGHT) {
            throw new IllegalArgumentException("Invalid y_max: " + yMax + ", y_max must be between 0 and " + WORLD_HEIGHT + ".");
        }
        return yMax;
    }

    private static int readSize(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Invalid size: " + size + ", size must be positive");
        }
        return size;
    }

    private static double readFrequency(double frequency) {
        if (frequency < 0) {
            throw new IllegalArgumentException("Invalid frequency: " + frequency + ", frequency must be positive");
        }
        return roundDoubleToDecimalPlaces(frequency, 3);
    }

    private static double readDensity(double density) {
        if (density < 0 || density > 1) {
            throw new IllegalArgumentException("Invalid density: " + density + ", density must be between 0 and 1.");
        }
        return roundDoubleToDecimalPlaces(density, 3);
    }

    private static JsonArray createJsonBlocksList(OreVein oreVein) {
        JsonArray jsonArray = new JsonArray();

        for (int i = 0; i < oreVein.blocks.size(); i++) {
            jsonArray.add(Registry.BLOCK.getId(oreVein.blocks.get(i)).toString());
            jsonArray.add(oreVein.blockWeights.get(i));
        }

        return jsonArray;
    }

    private static JsonArray createReplaceableBlocksList(OreVein oreVein) {
        JsonArray jsonArray = new JsonArray();

        for (Block block : oreVein.replaceableBlocks) {
            jsonArray.add(Registry.BLOCK.getId(block).toString());
        }

        return jsonArray;
    }

    private static double roundDoubleToDecimalPlaces(double value, int decimalPlaces) {
        double multiplier = Math.pow(10, decimalPlaces);
        return (double) Math.round(value * multiplier) / multiplier;
    }

    private static Block parseBlock(String blockString) {
        Block block = Registry.BLOCK.get(new Identifier(blockString));
        if (block == Blocks.AIR && !blockString.equals("minecraft:air")) {
            throw new IllegalArgumentException("Invalid blocks array, " + blockString + " not found.");
        }
        return block;
    }

    public static double getBaseFrequency() {
        return baseFrequency;
    }
}
