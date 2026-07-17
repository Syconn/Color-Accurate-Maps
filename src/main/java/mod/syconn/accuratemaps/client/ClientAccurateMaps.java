package mod.syconn.accuratemaps.client;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.syconn.accuratemaps.AccurateMaps;
import mod.syconn.accuratemaps.mixin.BrightnessInvoker;
import mod.syconn.accuratemaps.mixin.MapColorInvoker;
import mod.syconn.accuratemaps.util.AdditionalMapColor;
import mod.syconn.accuratemaps.util.AdditionalMapData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClientAccurateMaps implements ClientModInitializer {

    private static final Map<Integer, AdditionalMapData> mapData = new LinkedHashMap<>();
    private static final Map<BlockState, Integer> stateColorMap = new LinkedHashMap<>();

    public static void paintBlockColorMap(Minecraft client) {
        var atlasId = client.getModelManager().getBlockModelShaper().getBlockModel(Blocks.STONE.defaultBlockState()).getParticleIcon().atlasLocation();
        var atlas = client.getTextureManager().getTexture(atlasId);

        RenderSystem.bindTexture(atlas.getId());
        var width = new int[1];
        var height = new int[1];
        GL11.glGetTexLevelParameteriv(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH, width);
        GL11.glGetTexLevelParameteriv(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT, height);
        var pixels = new byte[width[0] * height[0] * 4];
        var buffer = ByteBuffer.allocateDirect(pixels.length);
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        buffer.get(pixels);

        for (var block : BuiltInRegistries.BLOCK) {
            for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                var model = client.getBlockRenderer().getBlockModel(state);
                var quads = model.getQuads(state, Direction.UP, RandomSource.create());
                var sprite = quads.isEmpty() ? model.getParticleIcon() : quads.get(0).getSprite();

                var sumIndex = 0;
                int sumR = 0, sumG = 0, sumB = 0;
                int minU = (int) (sprite.getU0() * width[0]), maxU = (int) (sprite.getU1() * width[0]);
                int minV = (int) (sprite.getV0() * height[0]), maxV = (int) (sprite.getV1() * height[0]);

                for (int pixelX = minU; pixelX < maxU; pixelX++) {
                    for (int pixelY = minV; pixelY < maxV; pixelY++) {
                        var index = (pixelX + pixelY * width[0]) * 4;
                        if ((pixels[index + 3] & 0xFF) > 0) {
                            sumIndex++;
                            sumR += pixels[index] & 0xFF;
                            sumG += pixels[index + 1] & 0xFF;
                            sumB += pixels[index + 2] & 0xFF;
                        }
                    }
                }

                if (sumIndex > 0) {
                    int rgb = (sumB / sumIndex) | ((sumG / sumIndex) << 8) | ((sumR / sumIndex) << 16);
                    stateColorMap.put(state, rgb);
                }
            }
        }
    }

    public static void updateTexture(DynamicTexture texture, MapItemSavedData data, AdditionalMapData additionalData) {
        if (texture.getPixels() != null) {
            for(int i = 0; i < 128; ++i) {
                for(int j = 0; j < 128; ++j) {
                    int k = i + j * 128;

                    var unShifted = data.colors[k] & 255;
                    var originalColor = MapColorInvoker.invokeByIdUnsafe(unShifted >> 2);
                    var brightness = BrightnessInvoker.invokeByIdUnsafe(unShifted & 3);
                    var blockState = Block.stateById(additionalData.getStates()[k]);
                    var blockPos = BlockPos.of(additionalData.getPositions()[k]);
                    if(!blockState.isAir()) {
                        var provider = ColorProviderRegistry.BLOCK.get(blockState.getBlock());
                        int tintColor = provider != null ? provider.getColor(blockState, Minecraft.getInstance().level, blockPos, 0) : 0xFFFFFF;
                        var savedColor = stateColorMap.getOrDefault(blockState, 0);
                        var multipliedColor = getMultipliedColor(tintColor, savedColor);
                        texture.getPixels().setPixelRGBA(i, j, AdditionalMapColor.getRenderColor(multipliedColor, brightness));
                    } else{
                        texture.getPixels().setPixelRGBA(i, j, originalColor.calculateRGBColor(brightness));
                    }
                }
            }

            texture.upload();
        }
    }

    private static int getMultipliedColor(int tintColor, Integer savedColor) {
        var lightFactor = 1.2f;
        int red = Math.round(Math.min(255.0F, (((((tintColor >> 16) & 0xFF) * ((savedColor >> 16) & 0xFF)) / 255.0F) * lightFactor)));
        int green = Math.round(Math.min(255.0F, (((((tintColor >> 8) & 0xFF) * ((savedColor >> 8) & 0xFF)) / 255.0F) * lightFactor)));
        int blue = Math.round(Math.min(255.0F, ((((tintColor & 0xFF) * (savedColor & 0xFF)) / 255.0F) * lightFactor)));
        return (red << 16) | (green << 8) | blue;
    }

    public static void onMapUpdate(ClientboundMapItemDataPacket packet) {
        if(ClientPlayNetworking.canSend(AccurateMaps.RequestMap.TYPE)) ClientPlayNetworking.send(new AccurateMaps.RequestMap(packet.mapId()));
    }

    public static AdditionalMapData getMapData(int id) {
        return mapData.get(id);
    }

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            if(stateColorMap.isEmpty()) paintBlockColorMap(client);
            mapData.clear();
        }));

        //? if 1.20.1 {
        /*ClientPlayNetworking.registerGlobalReceiver(AccurateMaps.RECEIVE_MAP, ((client, handler, buf, responseSender) -> {
            var id = buf.readInt();
            var nbt = Objects.requireNonNullElseGet(buf.readNbt(), CompoundTag::new);
            client.execute(() -> mapData.put(id, new AdditionalMapData(nbt)));
        }));
        *///? } else
        ClientPlayNetworking.registerGlobalReceiver(AccurateMaps.ReceiveMap.TYPE, ((payload, context) -> context.client().execute(() -> mapData.put(payload.id().id(), new AdditionalMapData(payload.nbt())))));
    }
}
