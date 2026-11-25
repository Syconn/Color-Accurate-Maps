/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * Based on Accurate Maps by D4rkness_King / lucaargolo
 * (https://github.com/lucaargolo/accurate-maps), MPL-2.0.
 * Rewritten in Java for Minecraft 1.20+ using Stonecutter.
 */

package mod.syconn.accuratemaps.platform.neo;

//? if neoforge {
import mod.syconn.accuratemaps.util.AdditionalMapData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.jetbrains.annotations.NotNull;

//? if <26.1 {
/*import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
*///? }

@Mod(NeoAccurateMaps.MOD)
public class NeoAccurateMaps {
    public static final String MOD = "accuratemaps";

    public NeoAccurateMaps(IEventBus modBus) {
        modBus.addListener(NeoAccurateMaps::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1").optional();

        registrar.playToServer(RequestMap.TYPE, RequestMap.CODEC, (payload, context) -> {
            if (!(context.player() instanceof ServerPlayer player)) return;
            var id = payload.id();
            //? if <26.1 {
            /*var accurateMapState = player.server.overworld().getDataStorage().computeIfAbsent(
                    new SavedData.Factory<>(AdditionalMapData::new, AdditionalMapData::new, DataFixTypes.SAVED_DATA_FORCED_CHUNKS),
                    "accurate_map_" + id.key());
            var nbt = accurateMapState.clientSave(new CompoundTag());
            PacketDistributor.sendToPlayer(player, new ReceiveMap(id, nbt));
            *///? } else {
            var accurateMapState = player.server.overworld().getDataStorage().computeIfAbsent(AdditionalMapData.type(id));
            var nbt = accurateMapState.clientSave(new CompoundTag());
            PacketDistributor.sendToPlayer(player, new ReceiveMap(id, nbt));
            //? }
        });

        //? if <26.1 {
        /*registrar.playToClient(ReceiveMap.TYPE, ReceiveMap.CODEC, NeoClientAccurateMaps::handleReceiveMap);
        *///? } else {
        registrar.playToClient(ReceiveMap.TYPE, ReceiveMap.CODEC);
        //? }
    }

    public record ReceiveMap(MapId id, CompoundTag nbt) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<ReceiveMap> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("accuratemaps", "receive_color_accurate_map"));
        public static final StreamCodec<RegistryFriendlyByteBuf, ReceiveMap> CODEC = StreamCodec.composite(MapId.STREAM_CODEC, ReceiveMap::id, ByteBufCodecs.COMPOUND_TAG, ReceiveMap::nbt, ReceiveMap::new);

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record RequestMap(MapId id) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<RequestMap> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("accuratemaps", "request_color_accurate_map"));
        public static final StreamCodec<RegistryFriendlyByteBuf, RequestMap> CODEC = StreamCodec.composite(MapId.STREAM_CODEC, RequestMap::id, RequestMap::new);

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
//? }