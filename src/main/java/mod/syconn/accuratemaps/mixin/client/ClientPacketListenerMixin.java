package mod.syconn.accuratemaps.mixin.client;

import mod.syconn.accuratemaps.client.ClientAccurateMaps;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(at = @At("HEAD"), method = "handleMapItemData")
    private void updateMaps(ClientboundMapItemDataPacket clientboundMapItemDataPacket, CallbackInfo ci) {
        ClientAccurateMaps.onMapUpdate(clientboundMapItemDataPacket);
    }
}
