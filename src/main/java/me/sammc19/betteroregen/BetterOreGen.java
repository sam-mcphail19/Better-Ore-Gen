package me.sammc19.betteroregen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.sammc19.betteroregen.config.BetterOreGenConfig;
import me.sammc19.betteroregen.generation.OreRemover;
import me.sammc19.betteroregen.generation.OreVein;
import me.sammc19.betteroregen.generation.VeinGenerator;
import net.fabricmc.api.ModInitializer;

import net.minecraft.block.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;


public class BetterOreGen implements ModInitializer {

    public static final String namespace = "betteroregen";

    public static final Logger LOGGER = LogManager.getLogger();
    public static final Gson GSON = new GsonBuilder().create();

    public static OreRemover oreRemover;
    public static VeinGenerator veinGenerator;

    public static ArrayList<BlockState> ores = new ArrayList<>();
    public static ArrayList<BlockState> stones = new ArrayList<>();
    public static ArrayList<OreVein> oreVeins = new ArrayList<>();

    @Override
    public void onInitialize() {
        BetterOreGenConfig.initializeConfig();
        oreRemover = new OreRemover();
        veinGenerator = new VeinGenerator();
        veinGenerator.generateVein(null, null, null, null);
    }
}
