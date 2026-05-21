package committee.nova.mods.avaritia_integration.module.mekanism.common.content.blocktype;

import mekanism.common.util.EnumUtils;
import mekanism.common.util.VoxelShapeUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MekIntegrationBlockShapes {

    private MekIntegrationBlockShapes() {
    }

    private static VoxelShape box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return Block.box(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static final VoxelShape[] SOLAR_GENERATOR = new VoxelShape[EnumUtils.HORIZONTAL_DIRECTIONS.length];

    static {
        VoxelShapeUtils.setShape(VoxelShapeUtils.combine(
                box(1, 7, 1, 15, 8, 15),
                box(0.5, 8.5, 0.5, 15.5, 9.5, 15.5),
                box(3, 2, 3, 13, 3, 13),
                box(1.9, 0.05, 1.9, 14.1, 2.25, 14.1),
                box(1, 8, 0, 15, 10, 1),
                box(15, 8, 0, 16, 10, 16),
                box(0, 8, 0, 1, 10, 16),
                box(1, 8, 15, 15, 10, 16),
                box(1, 0, 10, 6, 2, 15),
                box(10, 0, 10, 15, 2, 15),
                box(10, 0, 1, 15, 2, 6),
                box(10, 0, 0, 16, 1, 6),
                box(0, 0, 0, 6, 1, 6),
                box(0, 0, 10, 6, 1, 16),
                box(10, 0, 10, 16, 1, 16),
                box(1, 0, 1, 6, 2, 6),
                box(2, 2, 2, 4, 7, 4),
                box(12, 2, 2, 14, 7, 4),
                box(12, 2, 12, 14, 7, 14),
                box(2, 2, 12, 4, 7, 14),
                box(4, 3, 4, 12, 7, 12),
                box(5, 3, 5, 11, 7, 11)
        ), SOLAR_GENERATOR);
    }
}
