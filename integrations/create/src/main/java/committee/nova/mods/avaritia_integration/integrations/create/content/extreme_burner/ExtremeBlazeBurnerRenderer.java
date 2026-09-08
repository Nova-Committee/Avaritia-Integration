package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_burner;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationPartialModels;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationSpriteShifts;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ExtremeBlazeBurnerRenderer extends SafeBlockEntityRenderer<ExtremeBlazeBurnerBlockEntity> {
    public ExtremeBlazeBurnerRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(ExtremeBlazeBurnerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        ExtremeBlazeBurnerBlock.ExtremeHeatLevel heatLevel = be.getHeatLevelForRender();

        Level level = be.getLevel();
        BlockState blockState = be.getBlockState();
        float animation = be.headAnimation.getValue(partialTicks) * .175f;
        float horizontalAngle = AngleHelper.rad(be.headAngle.getValue(partialTicks));
        boolean canDrawFlame = heatLevel.isAtLeast(ExtremeBlazeBurnerBlock.ExtremeHeatLevel.FADING);
        boolean drawGoggles = be.goggles;
        PartialModel drawHat = be.hat ? AllPartialModels.TRAIN_HAT : be.stockKeeper ? AllPartialModels.LOGISTICS_HAT : be.hasStraw ? CreateIntegrationPartialModels.CCA_LIQUID_HAT : null;
        int hashCode = be.hashCode();

        renderShared(ms, null, bufferSource, level, blockState, heatLevel, animation, horizontalAngle, canDrawFlame, drawGoggles, drawHat, hashCode);
    }

    public static void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource bufferSource, LerpedFloat headAngle, boolean conductor) {
        BlockState state = context.state;
        ExtremeBlazeBurnerBlock.ExtremeHeatLevel heatLevel = ExtremeBlazeBurnerBlock.getExtremeHeatLevelOf(state);

        if (!heatLevel.isAtLeast(ExtremeBlazeBurnerBlock.ExtremeHeatLevel.FADING))
            heatLevel = ExtremeBlazeBurnerBlock.ExtremeHeatLevel.FADING;

        Level level = context.world;
        float horizontalAngle = AngleHelper.rad(headAngle.getValue(AnimationTickHolder.getPartialTicks(level)));
        boolean drawGoggles = context.blockEntityData.contains("Goggles");
        boolean drawHat = conductor || context.blockEntityData.contains("TrainHat");
        int hashCode = context.hashCode();

        renderShared(matrices.getViewProjection(), matrices.getModel(), bufferSource, level, state, heatLevel, 0, horizontalAngle, false, drawGoggles, drawHat ? AllPartialModels.TRAIN_HAT : null, hashCode);
    }

    public static void renderShared(PoseStack ms, @Nullable PoseStack modelTransform, MultiBufferSource bufferSource,
                                    Level level, BlockState blockState, ExtremeBlazeBurnerBlock.ExtremeHeatLevel heatLevel, float animation, float horizontalAngle,
                                    boolean canDrawFlame, boolean drawGoggles, PartialModel drawHat, int hashCode) {

        boolean blockAbove = animation > 0.125f;
        float time = AnimationTickHolder.getRenderTime(level);
        float renderTick = time + (hashCode % 13) * 16f;
        float offsetMult = heatLevel.isAtLeast(ExtremeBlazeBurnerBlock.ExtremeHeatLevel.FADING) ? 64 : 16;
        float offset = Mth.sin((float) ((renderTick / 16f) % (2 * Math.PI))) / offsetMult;
        float offset1 = Mth.sin((float) ((renderTick / 16f + Math.PI) % (2 * Math.PI))) / offsetMult;
        float offset2 = Mth.sin((float) ((renderTick / 16f + Math.PI / 2) % (2 * Math.PI))) / offsetMult;
        float headY = offset - (animation * .75f);

        VertexConsumer solid = bufferSource.getBuffer(RenderType.solid());
        VertexConsumer cutout = bufferSource.getBuffer(RenderType.cutoutMipped());

        ms.pushPose();

        if (canDrawFlame && blockAbove) {
            SpriteShiftEntry spriteShift =
                    heatLevel == ExtremeBlazeBurnerBlock.ExtremeHeatLevel.STAR ? CreateIntegrationSpriteShifts.STAR_BURNER_FLAME : CreateIntegrationSpriteShifts.BLAZE_BURNER_FLAME;

            float spriteWidth = spriteShift.getTarget()
                    .getU1()
                    - spriteShift.getTarget()
                    .getU0();

            float spriteHeight = spriteShift.getTarget()
                    .getV1()
                    - spriteShift.getTarget()
                    .getV0();

            float speed = 1 / 32f + 1 / 64f * heatLevel.ordinal();

            double vScroll = speed * time;
            vScroll = vScroll - Math.floor(vScroll);
            vScroll = vScroll * spriteHeight / 2;

            double uScroll = speed * time / 2;
            uScroll = uScroll - Math.floor(uScroll);
            uScroll = uScroll * spriteWidth / 2;

            SuperByteBuffer flameBuffer = CachedBuffers.partial(AllPartialModels.BLAZE_BURNER_FLAME, blockState);
            if (modelTransform != null)
                flameBuffer.transform(modelTransform);
            flameBuffer.shiftUVScrolling(spriteShift, (float) uScroll, (float) vScroll);
            draw(flameBuffer, horizontalAngle, ms, cutout);
        }

        var blazeModel = getBlazeModel(heatLevel, blockAbove);

        SuperByteBuffer blazeBuffer = CachedBuffers.partial(blazeModel, blockState);
        if (modelTransform != null)
            blazeBuffer.transform(modelTransform);
        blazeBuffer.translate(0, headY, 0);
        draw(blazeBuffer, horizontalAngle, ms, solid);

        if (drawGoggles) {
            PartialModel gogglesModel = blazeModel == AllPartialModels.BLAZE_INERT
                    ? AllPartialModels.BLAZE_GOGGLES_SMALL : AllPartialModels.BLAZE_GOGGLES;

            SuperByteBuffer gogglesBuffer = CachedBuffers.partial(gogglesModel, blockState);
            if (modelTransform != null)
                gogglesBuffer.transform(modelTransform);
            gogglesBuffer.translate(0, headY + 8 / 16f, 0);
            draw(gogglesBuffer, horizontalAngle, ms, solid);
        }

        if (drawHat != null) {
            SuperByteBuffer hatBuffer = CachedBuffers.partial(drawHat, blockState);
            if (modelTransform != null)
                hatBuffer.transform(modelTransform);
            hatBuffer.translate(0, headY, 0);
            if (blazeModel == AllPartialModels.BLAZE_INERT) {
                hatBuffer.translateY(0.5f)
                        .center()
                        .scale(0.75f)
                        .uncenter();
            } else {
                hatBuffer.translateY(0.75f);
            }
            hatBuffer
                    .rotateCentered(horizontalAngle + Mth.PI, Direction.UP)
                    .translate(0.5f, 0, 0.5f)
                    .light(LightTexture.FULL_BRIGHT)
                    .renderInto(ms, cutout);
        }

        if (heatLevel.isAtLeast(ExtremeBlazeBurnerBlock.ExtremeHeatLevel.FADING)) {
            PartialModel rodsModel = heatLevel == ExtremeBlazeBurnerBlock.ExtremeHeatLevel.STAR ? CreateIntegrationPartialModels.BLAZE_BURNER_STAR_RODS
                    : CreateIntegrationPartialModels.BLAZE_BURNER_BLAZE_RODS;
            PartialModel rodsModel2 = heatLevel == ExtremeBlazeBurnerBlock.ExtremeHeatLevel.STAR ? CreateIntegrationPartialModels.BLAZE_BURNER_STAR_RODS_2
                    : CreateIntegrationPartialModels.BLAZE_BURNER_BLAZE_RODS_2;

            SuperByteBuffer rodsBuffer = CachedBuffers.partial(rodsModel, blockState);
            if (modelTransform != null)
                rodsBuffer.transform(modelTransform);
            rodsBuffer.translate(0, offset1 + animation + .125f, 0)
                    .light(LightTexture.FULL_BRIGHT)
                    .renderInto(ms, solid);

            SuperByteBuffer rodsBuffer2 = CachedBuffers.partial(rodsModel2, blockState);
            if (modelTransform != null)
                rodsBuffer2.transform(modelTransform);
            rodsBuffer2.translate(0, offset2 + animation - 3 / 16f, 0)
                    .light(LightTexture.FULL_BRIGHT)
                    .renderInto(ms, solid);
        }

        ms.popPose();
    }

    public static PartialModel getBlazeModel(ExtremeBlazeBurnerBlock.ExtremeHeatLevel heatLevel, boolean blockAbove) {
        if (heatLevel.isAtLeast(ExtremeBlazeBurnerBlock.ExtremeHeatLevel.STAR)) {
            return blockAbove ? CreateIntegrationPartialModels.BLAZE_STAR_ACTIVE : CreateIntegrationPartialModels.BLAZE_STAR;
        } else if (heatLevel.isAtLeast(ExtremeBlazeBurnerBlock.ExtremeHeatLevel.FADING)) {
            return blockAbove && heatLevel.isAtLeast(ExtremeBlazeBurnerBlock.ExtremeHeatLevel.BLAZE) ? CreateIntegrationPartialModels.BLAZE_BLAZE_ACTIVE
                    : CreateIntegrationPartialModels.BLAZE_BLAZE;
        } else {
            return CreateIntegrationPartialModels.EXTREME_BLAZE_INERT;
        }
    }

    private static void draw(SuperByteBuffer buffer, float horizontalAngle, PoseStack ms, VertexConsumer vc) {
        buffer.rotateCentered(horizontalAngle, Direction.UP)
                .light(LightTexture.FULL_BRIGHT)
                .renderInto(ms, vc);
    }
}
