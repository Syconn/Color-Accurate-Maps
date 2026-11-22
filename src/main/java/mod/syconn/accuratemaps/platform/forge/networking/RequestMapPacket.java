package mod.syconn.accuratemaps.platform.forge.networking;

import mod.syconn.accuratemaps.util.AdditionalMapData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RequestMapPacket {
    private final int id;

    public RequestMapPacket(int id) {
        this.id = id;
    }

    public static void encode(RequestMapPacket pkt, FriendlyByteBuf buf) {
        buf.writeInt(pkt.id);
    }

    public static RequestMapPacket decode(FriendlyByteBuf buf) {
        return new RequestMapPacket(buf.readInt());
    }

    public static void handle(RequestMapPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        var player = ctx.get().getSender();
        if (player != null) {
            var accurateMapState = player.server.overworld().getDataStorage().computeIfAbsent(AdditionalMapData::new, AdditionalMapData::new, "accurate_map_" + pkt.id);
            var nbt = accurateMapState.save(new CompoundTag());
            ForgeNetwork.sendToPlayer(new ReceiveMapPacket(pkt.id, nbt), player);
        }
        ctx.get().setPacketHandled(true);
    }
}