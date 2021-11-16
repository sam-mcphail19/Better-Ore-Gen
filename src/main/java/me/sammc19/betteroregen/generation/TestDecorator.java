package me.sammc19.betteroregen.generation;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorContext;

import java.util.Random;
import java.util.stream.Stream;

public class TestDecorator extends Decorator<ChanceDecoratorConfig> {

    public TestDecorator() {
        super(ChanceDecoratorConfig.CODEC);
    }

    @Override
    public Stream<BlockPos> getPositions(DecoratorContext context, Random random, ChanceDecoratorConfig config, BlockPos pos) {
        if (random.nextFloat() < 1.0F / (float) config.chance) {
            int randomX = random.nextInt(16) + pos.getX();
            int randomZ = random.nextInt(16) + pos.getZ();
            int topY = context.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, randomX, randomZ);
            return Stream.of(new BlockPos(randomX, topY, randomZ));
        } else {
            return Stream.empty();
        }
    }

}
