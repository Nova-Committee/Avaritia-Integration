package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_basin;

import com.simibubi.create.content.processing.basin.BasinBlock;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;

import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlockEntityTypes;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class ExtremeBasinBlock extends BasinBlock {
    public ExtremeBasinBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends com.simibubi.create.content.processing.basin.BasinBlockEntity> getBlockEntityType() {
        return CreateIntegrationBlockEntityTypes.EXTREME_BASIN.get();
    }

    public static class Generator extends SpecialBlockStateGen {
        @Override
        protected int getXRotation(BlockState state) {
            return 0;
        }

        @Override
        protected int getYRotation(BlockState state) {
            return horizontalAngle(state.getValue(FACING));
        }

        @Override
        public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                BlockState state) {
            return AssetLookup.partialBaseModel(ctx, prov, "block");
        }
    }
}
