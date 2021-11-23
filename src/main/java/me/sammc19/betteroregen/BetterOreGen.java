package me.sammc19.betteroregen;

import me.sammc19.betteroregen.config.BetterOreGenConfig;
import me.sammc19.betteroregen.generation.OreVein;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;


public class BetterOreGen implements ModInitializer {

    public static final String namespace = "betteroregen";

    public static final Logger LOGGER = LogManager.getLogger();

    public static ArrayList<OreVein> oreVeins = new ArrayList<>();

    @Override
    public void onInitialize() {
        BetterOreGenConfig.initializeConfig();
    }
}
