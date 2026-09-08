package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_basin;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import committee.nova.mods.avaritia_integration.integrations.create.foundation.data.CreateIntegrationAssetLookup;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class ExtremeBasinGenerator extends SpecialBlockStateGen {
    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return horizontalAngle(state.getValue(ExtremeBasinBlock.FACING));
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        if (state.getValue(ExtremeBasinBlock.FACING).getAxis().isVertical())
            return CreateIntegrationAssetLookup.partialBaseModel(ctx, prov);
        return CreateIntegrationAssetLookup.partialBaseModel(ctx, prov, "directional");
    }

}
