package mod.syconn.accuratemaps.platform.forge.networking;

//? if forge {
/*import mod.syconn.accuratemaps.platform.forge.ForgeAccurateMaps;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ForgeNetwork {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new Identifier(ForgeAccurateMaps.MOD),
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

    public static void sendToAllPlayers(Object msg) {
        CHANNEL.sendToAllPlayers(msg);
    }

    public static void sendToPlayer(Object msg, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }
}
*///? }