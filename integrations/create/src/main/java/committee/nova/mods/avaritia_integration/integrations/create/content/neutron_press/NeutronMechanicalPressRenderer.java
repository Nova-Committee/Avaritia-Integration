package committee.nova.mods.avaritia_integration.integrations.create.content.neutron_press;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationPartialModels;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class NeutronMechanicalPressRenderer extends KineticBlockEntityRenderer<NeutronMechanicalPressBlockEntity> {

    public NeutronMechanicalPressRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRenderOffScreen(NeutronMechanicalPressBlockEntity be) {
        return true;
    }

    @Override
    protected void renderSafe(NeutronMechanicalPressBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        if (VisualizationManager.supportsVisualization(be.getLevel()))
            return;

        BlockState blockState = be.getBlockState();
        PressingBehaviour pressingBehaviour = be.getPressingBehaviour();
        float renderedHeadOffset =
                pressingBehaviour.getRenderedHeadOffset(partialTicks) * pressingBehaviour.mode.headOffset;

        SuperByteBuffer headRender = CachedBuffers.partialFacing(CreateIntegrationPartialModels.NEUTRON_MECHANICAL_PRESS_HEAD, blockState,
                blockState.getValue(NeutronMechanicalPressBlock.HORIZONTAL_FACING));
        headRender.translate(0, -renderedHeadOffset, 0)
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.solid()));
    }

    @Override
    protected BlockState getRenderedBlockState(NeutronMechanicalPressBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }
}
