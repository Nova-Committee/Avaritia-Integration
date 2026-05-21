package committee.nova.mods.avaritia_integration.module.botania.render;

import committee.nova.mods.avaritia_integration.module.botania.entity.AlphaSparkEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import vazkii.botania.client.render.entity.BaseSparkRenderer;

import java.util.Objects;

/**
 * @author cnlimiter
 */
public class AlphaSparkRender extends BaseSparkRenderer<AlphaSparkEntity> {
    private final TextureAtlasSprite dispersiveIcon;
    private final TextureAtlasSprite dominantIcon;
    private final TextureAtlasSprite recessiveIcon;
    private final TextureAtlasSprite isolatedIcon;

    public AlphaSparkRender(EntityRendererProvider.Context ctx) {
        super(ctx);
        var atlas = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
        this.dispersiveIcon = Objects.requireNonNull(atlas.apply(ResourceLocation.fromNamespaceAndPath("botania", "item/spark_upgrade_rune_dispersive")));
        this.dominantIcon = Objects.requireNonNull(atlas.apply(ResourceLocation.fromNamespaceAndPath("botania", "item/spark_upgrade_rune_dominant")));
        this.recessiveIcon = Objects.requireNonNull(atlas.apply(ResourceLocation.fromNamespaceAndPath("botania", "item/spark_upgrade_rune_recessive")));
        this.isolatedIcon = Objects.requireNonNull(atlas.apply(ResourceLocation.fromNamespaceAndPath("botania", "item/spark_upgrade_rune_isolated")));
    }

    @Override
    public TextureAtlasSprite getSpinningIcon(AlphaSparkEntity entity) {
        return switch (entity.getUpgrade()) {
            case NONE -> null;
            case DISPERSIVE -> this.dispersiveIcon;
            case DOMINANT -> this.dominantIcon;
            case RECESSIVE -> this.recessiveIcon;
            case ISOLATED -> this.isolatedIcon;
        };
    }

}