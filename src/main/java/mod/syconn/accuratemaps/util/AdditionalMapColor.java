package mod.syconn.accuratemaps.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class AdditionalMapColor extends MapColor {

    private final int blockState;
    private int tintColor;

    public AdditionalMapColor(MapColor original, BlockState blockState) {
        super(original.id, original.col);
        this.blockState = Block.getId(blockState);
    }

    public int getBlockState() {
        return blockState;
    }
}
