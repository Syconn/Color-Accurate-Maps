package mod.syconn.accuratemaps.mixin.client;

//? if fabric
//import mod.syconn.accuratemaps.platform.fabric.FabricClientAccurateMaps;
//? if forge
//import mod.syconn.accuratemaps.platform.forge.ForgeClientAccurateMaps;
//? if neoforge
import mod.syconn.accuratemaps.platform.neo.NeoClientAccurateMaps;
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
        //? if fabric
        //FabricClientAccurateMaps.onMapUpdate(clientboundMapItemDataPacket);
        //? if forge
        //ForgeClientAccurateMaps.onMapUpdate(clientboundMapItemDataPacket);
        //? if neoforge
        NeoClientAccurateMaps.onMapUpdate(clientboundMapItemDataPacket);
    }
}
