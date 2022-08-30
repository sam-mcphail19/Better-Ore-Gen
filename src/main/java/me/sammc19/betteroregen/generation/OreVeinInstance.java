package me.sammc19.betteroregen.generation;

import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class OreVeinInstance {
    public OreVein veinType;
    public Set<BlockPos> blocks;
    public boolean hasBeenFound;

    public OreVeinInstance(OreVein veinType) {
        this.veinType = veinType;

        blocks = new HashSet<>();
        hasBeenFound = false;
    }
}
