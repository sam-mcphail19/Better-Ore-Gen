package me.sammc19.betteroregen.mixin;

import static me.sammc19.betteroregen.BetterOreGen.LOGGER;

import me.sammc19.betteroregen.BetterOreGen;
import me.sammc19.betteroregen.generation.TestFeature;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.OreFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OreFeature.class)
public abstract class OreFeatureMixin {
    /*
    @Redirect(method = "generateVeinPart()Z",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/WorldAccess;" +
                            "setBlockState(" +
                            "Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I" +
                            ")Z"))
    public boolean removeOreFeatures(WorldAccess world, BlockPos mutable, BlockState state, int flags) {
        //LOGGER.info();
        state = BetterOreGen.oreRemover.removeOre(world, mutable, state);
        return world.setBlockState(mutable, state, flags);
    }
    */

    /*
        This removes all ore features including granite, diorite, dirt, etc.
        and also any ore feature i add lol
     */

    @Inject(method="generate()Z", at=@At("HEAD"), cancellable=true)
    public void removeOreFeatures(CallbackInfoReturnable<Boolean> cir){
        //cir.setReturnValue(false);
    }

}
