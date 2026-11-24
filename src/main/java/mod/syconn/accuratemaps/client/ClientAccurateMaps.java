/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * Based on Accurate Maps by D4rkness_King / lucaargolo
 * (https://github.com/lucaargolo/accurate-maps), MPL-2.0.
 * Rewritten in Java for Minecraft 1.20+ using Stonecutter.
 */

package mod.syconn.accuratemaps.client;

//? if fabric && <26.1
//import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

import mod.syconn.accuratemaps.mixin.BrightnessInvoker;
import mod.syconn.accuratemaps.mixin.MapColorInvoker;
import mod.syconn.accuratemaps.util.AdditionalMapColor;
import mod.syconn.accuratemaps.util.AdditionalMapData;
import net.minecraft.client.Minecraft;

import net.minecraft.client.renderer.texture.DynamicTexture;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

//? <26.1 {
/*import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;
import java.nio.ByteBuffer;
*///? } else {
import com.mojang.blaze3d.platform.NativeImage;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.resources.Identifier;
//? }

import java.util.LinkedHashMap;
import java.util.Map;

public class ClientAccurateMaps {

    private static final Map<Integer, AdditionalMapData> mapData = new LinkedHashMap<>();
    private static final Map<BlockState, Integer> stateColorMap = new LinkedHashMap<>();

    public static void paintBlockColorMap(Minecraft client) {
        //? if <26.1 {
        /*var atlasId = client.getModelManager().getBlockModelShaper().getBlockModel(Blocks.STONE.defaultBlockState()).getParticleIcon().atlasLocation();
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
        *///? } else {
        var modelSet = client.getModelManager().getBlockStateModelSet();
        var resources = client.getResourceManager();
        var imageCache = new HashMap<Identifier, NativeImage>();
        var parts = new ArrayList<BlockStateModelPart>();
        var random = RandomSource.create(42L);

        for (var block : BuiltInRegistries.BLOCK) {
            for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                var model = modelSet.get(state);

                // Prefer the sprite on the top face, fall back to the particle sprite
                parts.clear();
                model.collectParts(random, parts);

                TextureAtlasSprite sprite = null;
                for (var part : parts) {
                    var quads = part.getQuads(Direction.UP);
                    if (!quads.isEmpty()) {
                        sprite = quads.get(0).materialInfo().sprite();
                        break;
                    }
                }
                if (sprite == null) sprite = model.particleMaterial().sprite();

                var contents = sprite.contents();
                var texId = contents.name().withPath(p -> "textures/" + p + ".png");

                var image = imageCache.computeIfAbsent(texId, id -> {
                    try (var in = resources.getResourceOrThrow(id).open()) {
                        return NativeImage.read(in);
                    } catch (Exception e) {
                        return null;
                    }
                });
                if (image == null) continue;

                // Animated textures are a vertical strip, so only sample the first frame
                int w = Math.min(contents.width(), image.getWidth());
                int h = Math.min(contents.height(), image.getHeight());

                long sumR = 0, sumG = 0, sumB = 0;
                int count = 0;
                for (int x = 0; x < w; x++) {
                    for (int y = 0; y < h; y++) {
//                        int px = image.getPixel(x, y);
//                        int a = (px >>> 24) & 0xFF;
//                        if (a > 0) {
//                            count++;
//                            sumR += px & 0xFF;
//                            sumG += (px >> 8) & 0xFF;
//                            sumB += (px >> 16) & 0xFF;
//                        }
                        int px = image.getPixel(x, y); // ARGB on 26.1
                        int a = (px >>> 24) & 0xFF;
                        if (a > 0) {
                            count++;
                            sumR += (px >> 16) & 0xFF;
                            sumG += (px >> 8) & 0xFF;
                            sumB += px & 0xFF;
                        }
                    }
                }
                if (count > 0) {
                    int rgb = (int) (sumB / count) | ((int) (sumG / count) << 8) | ((int) (sumR / count) << 16);
                    stateColorMap.put(state, rgb);
                }
            }
        }
        imageCache.values().forEach(i -> {
            if (i != null) i.close();
        });
        //? }
    }

    public static void updateTexture(DynamicTexture texture, MapItemSavedData data, AdditionalMapData additionalData) {
        //? if <26.1 {
        /*if (texture.getPixels() != null) {
            for (int i = 0; i < 128; ++i) {
                for (int j = 0; j < 128; ++j) {
                    int k = i + j * 128;

                    var unShifted = data.colors[k] & 255;
                    var originalColor = MapColorInvoker.invokeByIdUnsafe(unShifted >> 2);
                    var brightness = BrightnessInvoker.invokeByIdUnsafe(unShifted & 3);
                    var blockState = Block.stateById(additionalData.getStates()[k]);
                    var blockPos = BlockPos.of(additionalData.getPositions()[k]);
                    if (!blockState.isAir()) {
                        //? if fabric {
                        var provider = ColorProviderRegistry.BLOCK.get(blockState.getBlock());
                        int tintColor = provider != null ? provider.getColor(blockState, Minecraft.getInstance().level, blockPos, 0) : 0xFFFFFF;
                        //? } else if forge || neoforge {
                        /^int raw = Minecraft.getInstance().getBlockColors().getColor(blockState, Minecraft.getInstance().level, blockPos, 0);
                        int tintColor = raw != -1 ? raw : 0xFFFFFF;
                        ^///? }
                        var savedColor = stateColorMap.getOrDefault(blockState, 0);
                        var multipliedColor = getMultipliedColor(tintColor, savedColor);
                        texture.getPixels().setPixelRGBA(i, j, AdditionalMapColor.getRenderColor(multipliedColor, brightness));
                    } else texture.getPixels().setPixelRGBA(i, j, originalColor.calculateRGBColor(brightness));
                }
            }

            texture.upload();
        }
        *///? } else {
        if (texture.getPixels() != null) {
            for (int i = 0; i < 128; ++i) {
                for (int j = 0; j < 128; ++j) {
                    int k = i + j * 128;

                    var unShifted = data.colors[k] & 255;
                    var originalColor = MapColorInvoker.invokeByIdUnsafe(unShifted >> 2);
                    var brightness = BrightnessInvoker.invokeByIdUnsafe(unShifted & 3);
                    var blockState = Block.stateById(additionalData.getStates()[k]);
                    var blockPos = BlockPos.of(additionalData.getPositions()[k]);

                    int pixel;
                    if (!blockState.isAir()) {
                        var sources = Minecraft.getInstance().getBlockColors().getTintSources(blockState);
                        var tintColor = sources.isEmpty() ? 0xFFFFFF : sources.get(0).colorInWorld(blockState, Minecraft.getInstance().level, blockPos) & 0xFFFFFF;

                        if (blockState.is(Blocks.WATER)) {
                            if (tintColor == 0xFFFFFF && !sources.isEmpty()) tintColor = sources.get(0).color(blockState) & 0xFFFFFF;
                            if (tintColor == 0xFFFFFF) tintColor = 0x3F76E4;
                        }
                        var savedColor = stateColorMap.getOrDefault(blockState, 0);
                        var multipliedColor = getMultipliedColor(tintColor, savedColor);
                        pixel = AdditionalMapColor.getRenderColor(multipliedColor, brightness);
                    }
                    else pixel = originalColor.calculateARGBColor(brightness);
                    texture.getPixels().setPixel(i, j, pixel);
                }
            }
            texture.upload();
        }
        //? }
    }

    private static int getMultipliedColor(int tintColor, Integer savedColor) {
        var lightFactor = 1.2f;
        int red = Math.round(Math.min(255.0F, (((((tintColor >> 16) & 0xFF) * ((savedColor >> 16) & 0xFF)) / 255.0F) * lightFactor)));
        int green = Math.round(Math.min(255.0F, (((((tintColor >> 8) & 0xFF) * ((savedColor >> 8) & 0xFF)) / 255.0F) * lightFactor)));
        int blue = Math.round(Math.min(255.0F, ((((tintColor & 0xFF) * (savedColor & 0xFF)) / 255.0F) * lightFactor)));
        return (red << 16) | (green << 8) | blue;
    }

    public static boolean isStateColorMapEmpty() {
        return stateColorMap.isEmpty();
    }

    public static AdditionalMapData getMapData(int id) {
        return mapData.get(id);
    }

    public static void putMapData(int id, AdditionalMapData data) {
        mapData.put(id, data);
    }

    public static void clearMapData() {
        mapData.clear();
    }
}
