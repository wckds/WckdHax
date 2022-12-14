package io.github.wckds.wckdhax.mixins;

import io.github.wckds.wckdhax.Settings;
import io.github.wckds.wckdhax.renderhax.FreeCam;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class FreecamManager {
    @Inject(at = @At("HEAD"), method = "pushOutOfBlocks", cancellable = true)
    void pushOutOfBlocks(CallbackInfo ci) {
        if (FreeCam.INSTANCE.enabled) ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "sendMovementPackets", cancellable = true)
    void sendMovementPackets(CallbackInfo ci) {
        if (FreeCam.INSTANCE.enabled) ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "tick")
    void tick(CallbackInfo ci) {
        if (FreeCam.INSTANCE.enabled) {
            Settings.player.setBoundingBox(new Box(Settings.player.getPos(), Settings.player.getPos()));
            Settings.player.getAbilities().flying = true;
        }
    }
}
