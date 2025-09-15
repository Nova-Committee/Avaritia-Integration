package committee.nova.mods.avaritia_integration.module.botania.item.block;

import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.block.mana.ManaPoolBlock;


public class InfinityManaPoolBlock extends ManaPoolBlock {
    public InfinityManaPoolBlock(Variant v, Properties builder) {
        super(v, builder);
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}