package me.sammc19.betteroregen.generation;

import static me.sammc19.betteroregen.BetterOreGen.LOGGER;

import me.sammc19.betteroregen.BetterOreGen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SideShapeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;
import java.util.Random;

public class VeinGenerator {

    private StructureWorldAccess worldAccess;
    private static final Random random = new Random();

    public boolean generate(FeatureContext<OreFeatureConfig> context) {
        BlockPos blockPos = context.getOrigin();
        this.worldAccess = context.getWorld();
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();

        if (y <= worldAccess.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, x, z)) {
            ArrayList<OreVein> possibleOreVeins = new ArrayList<>();
            double frequencySum = 0;
            for (OreVein oreVein : BetterOreGen.oreVeins) {
                if (y > oreVein.yMin && y < oreVein.yMax) {
                    possibleOreVeins.add(oreVein);
                    frequencySum += oreVein.frequency;
                }
            }

            if (possibleOreVeins.size() < 1)
                return false;

            double rand = Math.random() * frequencySum;
            int selectionIndex = 0;
            frequencySum = possibleOreVeins.get(selectionIndex).frequency;
            while (frequencySum < rand) {
                selectionIndex++;
                frequencySum += possibleOreVeins.get(selectionIndex).frequency;
            }

            OreVein selectedOreVein = possibleOreVeins.get(selectionIndex);

            int placed = 0;
            int size = selectedOreVein.size / 2;
            for (int i = x - size; i < x + size; i++) {
                for (int j = y - size; j < y + size; j++) {
                    for (int k = z - size; k < z + size; k++) {
                        if (place(selectedOreVein, i, j, k)) {
                            placed++;
                        }
                    }
                }
            }
            return placed > 0;
        }

        return false;
    }

    private boolean place(OreVein oreVein, int x, int y, int z) {
        ChunkPos chunkPos = worldAccess.getChunk(new BlockPos(x, y, z)).getPos();
        if (!worldAccess.isChunkLoaded(chunkPos.x, chunkPos.z))
            return false;

        BlockPos blockPos = new BlockPos(x, y, z);

        if (blockIsFloating(new OreVeinBlock(blockPos))) {
            return false;
        }

        if (!blockCanBeReplacedByVein(worldAccess.getBlockState(blockPos))) {
            return false;
        }

        if (oreVein.density < Math.random()) {
            return false;
        }

        int randInt = random.nextInt(100);
        int selectionIndex = 0;
        int weightsSum = oreVein.blockWeights.get(selectionIndex);

        while (weightsSum < randInt) {
            selectionIndex++;
            weightsSum += oreVein.blockWeights.get(selectionIndex);
        }

        Block selectedBlock = oreVein.blocks.get(selectionIndex);
        worldAccess.setBlockState(blockPos, selectedBlock.getDefaultState(), 0);
        return true;
    }

    private boolean blockIsFloating(OreVeinBlock oreVeinBlock) {
        for (Direction direction : Direction.values()) {
            if (oreVeinBlock.blockState.isSideSolid(worldAccess, oreVeinBlock.blockPos, direction, SideShapeType.FULL)) {
                return false;
            }
        }
        return true;
    }

    private boolean blockCanBeReplacedByVein(BlockState blockState) {
        return blockState.isOf(Blocks.STONE);
    }

    private class OreVeinBlock {
        public BlockState blockState;
        public BlockPos blockPos;

        public OreVeinBlock(BlockPos blockPos) {
            this.blockPos = blockPos;
            this.blockState = worldAccess.getBlockState(blockPos);
        }
    }
}