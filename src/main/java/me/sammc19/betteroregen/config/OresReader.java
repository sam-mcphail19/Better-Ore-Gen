package me.sammc19.betteroregen.config;

import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class OresReader {

    public static Block readOre(JsonElement jsonElement){
        String oreIdentifer = jsonElement.getAsString();
        Block block = Registry.BLOCK.get(new Identifier(oreIdentifer));
        if(block == Blocks.AIR){
            throw new IllegalArgumentException("Invalid ore. Ore: \"" + oreIdentifer + "\" not found.");
        }
        return block;
    }

}
