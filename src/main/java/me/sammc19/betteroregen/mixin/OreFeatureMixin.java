package me.sammc19.betteroregen.mixin;

import me.sammc19.betteroregen.BetterOreGen;
import me.sammc19.betteroregen.OreRemover;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.OreFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OreFeature.class)
public abstract class OreFeatureMixin {

    @Redirect(method = "generateVeinPart()Z",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/WorldAccess;" +
                            "setBlockState(" +
                            "Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I" +
                            ")Z"))
    public boolean removeOreFeatures(WorldAccess world, BlockPos mutable, BlockState state, int flags) {
        state = BetterOreGen.oreRemover.removeOre(world, mutable, state);
        return world.setBlockState(mutable, state, flags);
    }
}
