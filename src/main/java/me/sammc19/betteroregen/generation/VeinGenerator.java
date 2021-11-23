package me.sammc19.betteroregen.generation;

import static me.sammc19.betteroregen.BetterOreGen.LOGGER;

import me.sammc19.betteroregen.BetterOreGen;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SideShapeType;
import net.minecraft.util.math.BlockPos;
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
            for (OreVein oreVein : BetterOreGen.oreVeins) {
                if (y > oreVein.yMin && y < oreVein.yMax)
                    possibleOreVeins.add(oreVein);
            }

            if (possibleOreVeins.size() < 1)
                return false;

            OreVein selectedOreVein = possibleOreVeins.get(random.nextInt(possibleOreVeins.size()));

            place(selectedOreVein, x, y, z);
            place(selectedOreVein, x, y + 1, z);
            place(selectedOreVein, x, y + 2, z);
            place(selectedOreVein, x + 1, y, z);
            place(selectedOreVein, x + 2, y, z);
            place(selectedOreVein, x, y, z + 1);
            place(selectedOreVein, x, y, z + 2);
            return true;
        }

        return false;
    }

    private void place(OreVein oreVein, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);

        if (blockIsFloating(new OreVeinBlock(blockPos)))
            return;

        if(!blockCanBeReplacedByVein(worldAccess.getBlockState(blockPos)))
            return;

        int randInt = random.nextInt(100);
        int selectionIndex = 0;
        int weightsSum = oreVein.blockWeights.get(selectionIndex);

        while (weightsSum < randInt) {
            selectionIndex++;
            weightsSum += oreVein.blockWeights.get(selectionIndex);
        }

        Block selectedBlock = oreVein.blocks.get(selectionIndex);
        worldAccess.setBlockState(blockPos, selectedBlock.getDefaultState(), 0);
    }

    private boolean blockIsFloating(OreVeinBlock oreVeinBlock) {
        for (Direction direction : Direction.values()) {
            if (oreVeinBlock.blockState.isSideSolid(worldAccess, oreVeinBlock.blockPos, direction, SideShapeType.FULL)) {
                return false;
            }
        }
        return true;
    }

    private boolean blockCanBeReplacedByVein(BlockState blockState){
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