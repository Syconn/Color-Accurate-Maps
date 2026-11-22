package mod.syconn.accuratemaps.platform.fabric;

//? if fabric {
/*import mod.syconn.accuratemaps.platform.fabric.AccurateMaps;
import mod.syconn.accuratemaps.util.AdditionalMapData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;

import java.util.Objects;

public class FabricClientAccurateMaps implements ClientModInitializer {

    public static void onMapUpdate(ClientboundMapItemDataPacket packet) {
        //? if >1.20.1 {
        /^if(ClientPlayNetworking.canSend(AccurateMaps.RequestMap.TYPE)) ClientPlayNetworking.send(new AccurateMaps.RequestMap(packet.mapId()));
        ^///? } else {
        if(ClientPlayNetworking.canSend(AccurateMaps.REQUEST_MAP)) {
            var buf = PacketByteBufs.create();
            buf.writeInt(packet.getMapId());
            ClientPlayNetworking.send(AccurateMaps.REQUEST_MAP, buf);
        }
        //? }
    }

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            if(stateColorMap.isEmpty()) paintBlockColorMap(client);
            mapData.clear();
        }));

        //? if 1.20.1 {
        ClientPlayNetworking.registerGlobalReceiver(AccurateMaps.RECEIVE_MAP, ((client, handler, buf, responseSender) -> {
            var id = buf.readInt();
            var nbt = Objects.requireNonNullElseGet(buf.readNbt(), CompoundTag::new);
            client.execute(() -> mapData.put(id, new AdditionalMapData(nbt)));
        }));
        //? } else
        //ClientPlayNetworking.registerGlobalReceiver(AccurateMaps.ReceiveMap.TYPE, ((payload, context) -> context.client().execute(() -> mapData.put(payload.id().id(), new AdditionalMapData(payload.nbt())))));
    }
}
*///? }