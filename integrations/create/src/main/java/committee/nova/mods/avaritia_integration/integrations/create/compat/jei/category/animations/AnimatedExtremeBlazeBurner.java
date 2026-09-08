package committee.nova.mods.avaritia_integration.integrations.create.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;

import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_burner.ExtremeBlazeBurnerBlock;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlocks;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationPartialModels;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationSpriteShifts;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class AnimatedExtremeBlazeBurner extends AnimatedKinetics {

    private ExtremeBlazeBurnerBlock.ExtremeHeatLevel heatLevel = ExtremeBlazeBurnerBlock.ExtremeHeatLevel.SMOULDERING;

    public AnimatedExtremeBlazeBurner withHeat(ExtremeBlazeBurnerBlock.ExtremeHeatLevel heatLevel) {
        this.heatLevel = heatLevel;
        return this;
    }

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale = 23;
        float offset = (Mth.sin(AnimationTickHolder.getRenderTime() / 16f) + 0.5f) / 16f;

        BlockState defaultState = CreateIntegrationBlocks.EXTREME_BLAZE_BURNER.getDefaultState();
        BlockState renderState = heatLevel == ExtremeBlazeBurnerBlock.ExtremeHeatLevel.STAR
                ? defaultState.setValue(ExtremeBlazeBurnerBlock.EXTREME_HEAT_LEVEL,
                        ExtremeBlazeBurnerBlock.ExtremeHeatLevel.STAR)
                : heatLevel.isAtLeast(ExtremeBlazeBurnerBlock.ExtremeHeatLevel.FADING)
                        ? defaultState.setValue(ExtremeBlazeBurnerBlock.EXTREME_HEAT_LEVEL,
                                ExtremeBlazeBurnerBlock.ExtremeHeatLevel.BLAZE)
                        : defaultState;
        blockElement(renderState).atLocal(0, 1.65, 0).scale(scale).render(graphics);

        PartialModel blaze = heatLevel == ExtremeBlazeBurnerBlock.ExtremeHeatLevel.STAR
                ? CreateIntegrationPartialModels.BLAZE_STAR
                : heatLevel.isAtLeast(ExtremeBlazeBurnerBlock.ExtremeHeatLevel.FADING)
                        ? CreateIntegrationPartialModels.BLAZE_BLAZE_ACTIVE
                        : CreateIntegrationPartialModels.EXTREME_BLAZE_INERT;
        PartialModel rods2 = heatLevel == ExtremeBlazeBurnerBlock.ExtremeHeatLevel.STAR
                ? CreateIntegrationPartialModels.BLAZE_BURNER_STAR_RODS_2
                : heatLevel.isAtLeast(ExtremeBlazeBurnerBlock.ExtremeHeatLevel.FADING)
                        ? CreateIntegrationPartialModels.BLAZE_BURNER_BLAZE_RODS_2
                        : null;
        blockElement(blaze).atLocal(1, 1.8, 1).rotate(0, 180, 0).scale(scale).render(graphics);
        if (rods2 != null) {
            blockElement(rods2).atLocal(1, 1.7 + offset, 1).rotate(0, 180, 0).scale(scale).render(graphics);
        }

        matrixStack.scale(scale, -scale, scale);
        matrixStack.translate(0, -1.8, 0);
        SpriteShiftEntry spriteShift = heatLevel == ExtremeBlazeBurnerBlock.ExtremeHeatLevel.STAR
                ? CreateIntegrationSpriteShifts.STAR_BURNER_FLAME
                : heatLevel.isAtLeast(ExtremeBlazeBurnerBlock.ExtremeHeatLevel.FADING)
                        ? CreateIntegrationSpriteShifts.BLAZE_BURNER_FLAME
                        : null;
        if (spriteShift != null) {
            float spriteWidth = spriteShift.getTarget().getU1() - spriteShift.getTarget().getU0();
            float spriteHeight = spriteShift.getTarget().getV1() - spriteShift.getTarget().getV0();
            float time = AnimationTickHolder.getRenderTime(Minecraft.getInstance().level);
            float speed = 1 / 32f + 1 / 64f * heatLevel.ordinal();
            double vScroll = speed * time;
            vScroll = vScroll - Math.floor(vScroll);
            vScroll = vScroll * spriteHeight / 2;
            double uScroll = speed * time / 2;
            uScroll = uScroll - Math.floor(uScroll);
            uScroll = uScroll * spriteWidth / 2;
            CachedBuffers.partial(AllPartialModels.BLAZE_BURNER_FLAME, Blocks.AIR.defaultBlockState())
                    .shiftUVScrolling(spriteShift, (float) uScroll, (float) vScroll)
                    .light(LightTexture.FULL_BRIGHT)
                    .renderInto(matrixStack, graphics.bufferSource().getBuffer(RenderType.cutoutMipped()));
        }
        matrixStack.popPose();
    }
}
