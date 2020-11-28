package me.sammc19.betteroregen;

import net.fabricmc.api.ModInitializer;

public class BetterOreGen implements ModInitializer {

    public static final String namespace = "betteroregen";
    public static OreRemover oreRemover;

    @Override
    public void onInitialize() {
        oreRemover = new OreRemover();
    }
}
