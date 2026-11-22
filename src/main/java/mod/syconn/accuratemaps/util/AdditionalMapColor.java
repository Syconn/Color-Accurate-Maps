/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * Based on Accurate Maps by D4rkness_King / lucaargolo
 * (https://github.com/lucaargolo/accurate-maps), MPL-2.0.
 * Rewritten in Java for Minecraft 1.20+ using Stonecutter.
 */

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
