package mod.syconn.accuratemaps.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedHashMap;
import java.util.Map;

public class ClientAccurateMaps { // TODO MAYBE SUPPORT AT REGISTRATION?

    private final Map<BlockState, Integer> blockStateIdMap = new LinkedHashMap<>();

    @Environment(EnvType.CLIENT)
    private final Map<Integer, Integer> idColorMap = new LinkedHashMap<>();

    public void paintBlockColorMap(Minecraft client) {
//        val atlas = client.getModelManager().getModel(Blocks.STONE.defaultState).getParticleIcon().atlasLocation();
//        RenderSystem.bindTexture(atlas.glId)
//        val width = intArrayOf(0)
//        GL11.glGetTexLevelParameteriv(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH, width)
//        val height = intArrayOf(0)
//        GL11.glGetTexLevelParameteriv(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT, height)
//        val pixels = ByteArray(width[0] * height[0] * 4)
//        val buffer:ByteBuffer = ByteBuffer.allocateDirect(pixels.size)
//        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer)
//        buffer.get(pixels)
//        Registry.BLOCK.forEach {
//            block ->
//                    block.stateManager.states.forEach {
//                state ->
//                        val blockModel = client.bakedModelManager.blockModels.getModel(state)
//                val blockSprite = blockModel.getQuads(state, Direction.UP, Random.create()).firstOrNull() ?.sprite ?:
//                blockModel.particleSprite
//                var sumIndex = 0
//                var sumR = 0
//                var sumG = 0
//                var sumB = 0
//                val minU = (blockSprite.minU * width[0]).toInt()
//                val maxU = (blockSprite.maxU * width[0]).toInt()
//                val minV = (blockSprite.minV * height[0]).toInt()
//                val maxV = (blockSprite.maxV * height[0]).toInt()
//                (minU until maxU).forEach {
//                    pixelX ->
//                            (minV until maxV).forEach {
//                        pixelY ->
//                                val index = (pixelX + pixelY * width[0]) * 4
//                        if (pixels[index + 3].toInt() and 0xFF > 0){
//                            sumIndex++
//                            sumR += pixels[index].toInt() and 0xFF
//                            sumG += pixels[index + 1].toInt() and 0xFF
//                            sumB += pixels[index + 2].toInt() and 0xFF
//                        }
//                    }
//                }
//                if (sumIndex > 0)
//                    blockColorMap[state] = (sumB / sumIndex) + ((sumG / sumIndex) << 8) + ((sumR / sumIndex) << 16);
//            }
//        }
//        renderViewsCache.clear();
    }


}
