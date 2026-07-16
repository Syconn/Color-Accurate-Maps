package mod.syconn.accuratemaps.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class AdditionalMapColor extends MapColor {

    private final int blockState;
    private final BlockPos pos;

    public AdditionalMapColor(MapColor original, BlockState blockState, BlockPos pos) {
        super(original.id, original.col);
        this.blockState = Block.getId(blockState);
        this.pos = pos;
    }

    public int getBlockState() {
        return blockState;
    }

    public BlockPos getPos() {
        return pos;
    }

    public static int getRenderColor(int color, Brightness brightness) {
        int i = brightness.modifier;
        int j = (color >> 16 & 255) * i / 255;
        int k = (color >> 8 & 255) * i / 255;
        int l = (color & 255) * i / 255;
        return -16777216 | l << 16 | k << 8 | j;
    }
}
