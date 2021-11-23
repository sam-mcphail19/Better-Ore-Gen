package me.sammc19.betteroregen.config;

import static me.sammc19.betteroregen.BetterOreGen.LOGGER;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

import me.sammc19.betteroregen.BetterOreGen;
import me.sammc19.betteroregen.generation.OreVein;
import net.fabricmc.loader.api.FabricLoader;


public class BetterOreGenConfig {
    public static boolean isConfigLoaded = false;
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("betteroregen.json");

    private static File config;

    public static JsonObject json;

    public static void initializeConfig(){
        if (isConfigLoaded)
            return;

        config = new File(CONFIG_PATH.toString());
        loadModConfig();
        readVeins();

        LOGGER.info("BetterOreGen Initialized");
        isConfigLoaded = true;
    }

    private static void loadModConfig() {
        try{
            BufferedReader br = new BufferedReader(new FileReader(config));
            json = new JsonParser().parse(br).getAsJsonObject();
        } catch(FileNotFoundException e) {
            LOGGER.error("BetterOreConfig Not Found", e);
        }
    }

    public static void saveModConfig() {

    }

    private static void readVeins(){
        JsonArray veins = json.get("veins").getAsJsonArray();
        for(int i=0; i<veins.size(); i++) {
            try {
                OreVein newVein = OreVeinReader.readVein(veins.get(i).getAsJsonObject());
                BetterOreGen.oreVeins.add(newVein);
            } catch(IllegalArgumentException e){
                LOGGER.error(e);
            }
        }
    }
}
