package mod.syconn.accuratemaps.platform.forge.networking;

import mod.syconn.accuratemaps.platform.forge.ForgeAccurateMaps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

//? if forge {
public class ForgeNetwork {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(ForgeAccurateMaps.MOD),
            () -> PROTOCOL_VERSION, NetworkRegistry.acceptMissingOr(PROTOCOL_VERSION), NetworkRegistry.acceptMissingOr(PROTOCOL_VERSION));

    public static void register() {
        int id = 0;

        CHANNEL.messageBuilder(RequestMapPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(RequestMapPacket::encode)
                .decoder(RequestMapPacket::decode)
                .consumerMainThread(RequestMapPacket::handle)
                .add();

        CHANNEL.messageBuilder(ReceiveMapPacket.class, id, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ReceiveMapPacket::encode)
                .decoder(ReceiveMapPacket::decode)
                .consumerMainThread(ReceiveMapPacket::handle)
                .add();
    }

    public static void sendToServer(Object msg) {
        CHANNEL.sendToServer(msg);
    }

    public static void sendToPlayer(Object msg, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }
}
//? }