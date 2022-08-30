package me.sammc19.betteroregen.mixin;

import me.sammc19.betteroregen.generation.OreVeinInstance;
import me.sammc19.betteroregen.generation.VeinGenerator;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.sammc19.betteroregen.BetterOreGen.LOGGER;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Inject(method = "tick", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;tick()V")), at = @At(value = "HEAD", ordinal = 0))
    private void clientTick(CallbackInfo callbackInfo) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null || client.player == null) {
            return;
        }

        HitResult result = client.crosshairTarget;

        if (result != null && result.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) result;
            OreVeinInstance vein = VeinGenerator.getFromVeinMap(blockHitResult.getBlockPos());

            if (vein != null && !vein.hasBeenFound) {
                client.player.sendMessage(Text.of("You found a " + vein.veinType.name + " vein at " + blockHitResult.getBlockPos().toShortString()), false);
                vein.hasBeenFound = true;
                for (BlockPos pos : vein.blocks) {
                    client.world.setBlockState(pos, Blocks.CYAN_WOOL.getDefaultState());
                }
            }
        }
    }
}
