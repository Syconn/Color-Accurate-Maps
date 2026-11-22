/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * Based on Accurate Maps by D4rkness_King / lucaargolo
 * (https://github.com/lucaargolo/accurate-maps), MPL-2.0.
 * Rewritten in Java for Minecraft 1.20+ using Stonecutter.
 */

package mod.syconn.accuratemaps.mixin.client;

import mod.syconn.accuratemaps.client.ClientAccurateMaps;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    //? if 1.20.1 {
    @Inject(at = @At("RETURN"), method = "reloadResourcePacks(Z)Ljava/util/concurrent/CompletableFuture;", cancellable = true)
    private void reloadBlockStateMap(boolean bl, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        cir.setReturnValue(cir.getReturnValue().thenRun(() -> ClientAccurateMaps.paintBlockColorMap((Minecraft) (Object) this)));
    }
    //? } else {
    /*@Inject(at = @At("RETURN"), method = "reloadResourcePacks()Ljava/util/concurrent/CompletableFuture;", cancellable = true)
    private void reloadBlockStateMap(CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        cir.setReturnValue(cir.getReturnValue().thenRun(() -> ClientAccurateMaps.paintBlockColorMap((Minecraft) (Object) this)));
    }
    *///? }
}
