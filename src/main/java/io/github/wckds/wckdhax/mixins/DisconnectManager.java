package io.github.wckds.wckdhax.mixins;

import io.github.wckds.wckdhax.Settings;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class DisconnectManager {
    @Inject(at = @At("RETURN"), method = "disconnect")
    public void onDisconnect(CallbackInfo ci) {
        if (Settings.player != null) Settings.saveToggles();
        Settings.player = null;
        Settings.world = null;
    }
}
