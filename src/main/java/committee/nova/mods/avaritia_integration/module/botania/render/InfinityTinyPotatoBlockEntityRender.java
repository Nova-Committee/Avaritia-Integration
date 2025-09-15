package committee.nova.mods.avaritia_integration.module.botania.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.botania.item.block.entity.InfinityTinyPotatoBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import vazkii.botania.common.helper.VecHelper;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

public class InfinityTinyPotatoBlockEntityRender implements BlockEntityRenderer<InfinityTinyPotatoBlockEntity> {
    public static final String DEFAULT = "default";
    private static final Pattern ESCAPED = Pattern.compile("[^a-z0-9/._-]");
    private final BlockRenderDispatcher blockRenderDispatcher;

    public InfinityTinyPotatoBlockEntityRender(BlockEntityRendererProvider.Context ctx) {
        this.blockRenderDispatcher = ctx.getBlockRenderDispatcher();
    }

    private static ResourceLocation taterLocation(String name) {
        return new ResourceLocation(AvaritiaIntegration.MOD_ID, "textures/block/infinity_potato");
    }

    @Override
    public void render(@Nonnull InfinityTinyPotatoBlockEntity potato, float partialTicks, PoseStack ms, @Nonnull MultiBufferSource buffers, int light, int overlay) {
        ms.pushPose();

        // 基础数据
        BlockState blockState = potato.getBlockState();
        BakedModel model = blockRenderDispatcher.getBlockModel(blockState);
        RenderType layer = Sheets.translucentCullBlockSheet();

        // 移动到中心
        ms.translate(0.5F, 0F, 0.5F);

        // 朝向
        float rotY = switch (blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case SOUTH -> 180F;
            case EAST -> 90F;
            case WEST -> 270F;
            default -> 0F; // NORTH
        };
        ms.mulPose(VecHelper.rotateY(rotY));

        // 跳动动画
        float jump = potato.jumpTicks;
        if (jump > 0) jump -= partialTicks;
        float up = (float) Math.abs(Math.sin(jump / 10 * Math.PI)) * 0.2F;
        float rotZ = (float) Math.sin(jump / 10 * Math.PI) * 2;
        float wiggle = (float) Math.sin(jump / 10 * Math.PI) * 0.05F;

        ms.translate(wiggle, up, 0F);
        ms.mulPose(VecHelper.rotateZ(rotZ));

        // 渲染方块模型
        ms.pushPose();
        ms.translate(-0.5F, 0, -0.5F);
        VertexConsumer buffer = buffers.getBuffer(layer);
        renderModel(ms, buffer, light, overlay, model);
        ms.popPose();

        // 渲染名字
        renderName(potato, ms, buffers, light);

        ms.popPose();
    }

    private void renderName(InfinityTinyPotatoBlockEntity potato, PoseStack ms, MultiBufferSource buffers, int light) {
        Minecraft mc = Minecraft.getInstance();
        HitResult pos = mc.hitResult;
        String name = potato.name.getString();

        if (Minecraft.renderNames() && !name.isEmpty() && pos != null && pos.getType() == Type.BLOCK
                && potato.getBlockPos().equals(((BlockHitResult) pos).getBlockPos())) {

            ms.pushPose();
            ms.translate(0.0F, -0.6F, 0.0F);
            ms.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
            float f1 = 0.02666667F;
            ms.scale(-f1, -f1, f1);

            int halfWidth = mc.font.width(name) / 2;
            float opacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int opacityRGB = (int) (opacity * 255.0F) << 24;

            mc.font.drawInBatch(potato.name, -halfWidth, 0.0F, 553648127,
                    false, ms.last().pose(), buffers, DisplayMode.SEE_THROUGH, opacityRGB, light);
            mc.font.drawInBatch(potato.name, -halfWidth, 0.0F, -1,
                    false, ms.last().pose(), buffers, DisplayMode.NORMAL, 0, light);

            ms.popPose();
        }
    }

    private void renderModel(PoseStack ms, VertexConsumer buffer, int light, int overlay, BakedModel model) {
        this.blockRenderDispatcher.getModelRenderer().renderModel(ms.last(), buffer, null, model,
                1.0F, 1.0F, 1.0F, light, overlay);
    }
}
