package committee.nova.mods.avaritia_integration.init.registry;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class BaseFluidType extends FluidType {
    private final String texture;
    private final Vector3f fogColor;

    public BaseFluidType(final String texture, final Vector3f fogColor, final FluidType.Properties properties) {
        super(properties);
        this.texture = texture;
        this.fogColor = fogColor;
    }

    @Override
    @SuppressWarnings("removal")
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public int getTintColor() {
                return 0xFFFFFFFF;
            }

            @Override
            public ResourceLocation getStillTexture() {
                return AvaritiaIntegration.rl("block/" + texture + "_still");
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return AvaritiaIntegration.rl("block/" + texture + "_flow");
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return getStillTexture();
            }

            @Override
            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return fogColor;
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                RenderSystem.setShaderFogStart(1f);
                RenderSystem.setShaderFogEnd(6f);
            }
        });
    }
}
