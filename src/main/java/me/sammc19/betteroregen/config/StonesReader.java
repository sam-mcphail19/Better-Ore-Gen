package me.sammc19.betteroregen.config;

import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class StonesReader {

    public static Block readStone(JsonElement jsonElement){
        String stoneIdentifer = jsonElement.getAsString();
        Block block = Registry.BLOCK.get(new Identifier(stoneIdentifer));
        if(block == Blocks.AIR){
            throw new IllegalArgumentException("Invalid stone. Ore: \"" + stoneIdentifer + "\" not found.");
        }
        return block;
    }
}
