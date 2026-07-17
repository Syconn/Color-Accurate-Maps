package mod.syconn.accuratemaps;

import mod.syconn.accuratemaps.util.AdditionalMapData;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.maps.MapId;
import org.jetbrains.annotations.NotNull;

//? if 1.20.1 {
/*import mod.syconn.accuratemaps.util.AdditionalMapData;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
*///? }

public class AccurateMaps implements ModInitializer {

    //? if 1.20.1 {
    /*public static ResourceLocation REQUEST_MAP = new ResourceLocation("accuratemaps", "request_color_accurate_map");
    public static ResourceLocation RECEIVE_MAP = new ResourceLocation("accuratemaps", "receive_color_accurate_map");
    *///? }

    @Override
    public void onInitialize() {
        //? if 1.20.1 {
        /*ServerPlayNetworking.registerGlobalReceiver(REQUEST_MAP, ((server, player, handler, buf, responseSender) ->{
            var id = buf.readInt();
            var accurateMapState = server.overworld().getDataStorage().computeIfAbsent(AdditionalMapData::new, AdditionalMapData::new, "accurate_map_"+id);
            var nbt = accurateMapState.save(new CompoundTag());
            var innerBuf = PacketByteBufs.create();
            innerBuf.writeInt(id);
            innerBuf.writeNbt(nbt);
            ServerPlayNetworking.send(player, RECEIVE_MAP, innerBuf);
        }));
        *///? } else {
        PayloadTypeRegistry.playC2S().register(RequestMap.TYPE, RequestMap.CODEC);
        PayloadTypeRegistry.playS2C().register(ReceiveMap.TYPE, ReceiveMap.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(RequestMap.TYPE, ((payload, context) -> {
            var id = payload.id();
            var accurateMapState = context.server().overworld().getDataStorage().computeIfAbsent(new SavedData.Factory<>(AdditionalMapData::new, AdditionalMapData::new, DataFixTypes.SAVED_DATA_FORCED_CHUNKS), "accurate_map_"+id.key());
            var nbt = accurateMapState.clientSave(new CompoundTag());
            ServerPlayNetworking.send(context.player(), new ReceiveMap(id, nbt));
        }));
        //? }
    }

    //? if >1.20.1 {
    public record ReceiveMap(MapId id, CompoundTag nbt) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<ReceiveMap> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("accuratemaps", "receive_color_accurate_map"));
        public static final StreamCodec<RegistryFriendlyByteBuf, ReceiveMap> CODEC = StreamCodec.composite(MapId.STREAM_CODEC, ReceiveMap::id, ByteBufCodecs.COMPOUND_TAG, ReceiveMap::nbt, ReceiveMap::new);

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record RequestMap(MapId id) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<RequestMap> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("accuratemaps", "request_color_accurate_map"));
        public static final StreamCodec<RegistryFriendlyByteBuf, RequestMap> CODEC = StreamCodec.composite(MapId.STREAM_CODEC, RequestMap::id, RequestMap::new);

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
    //? }
}
