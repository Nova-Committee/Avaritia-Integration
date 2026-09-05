package committee.nova.mods.avaritia_integration.integrations.botania.botania.render;

import committee.nova.mods.avaritia_integration.integrations.botania.botania.entity.AlphaSparkEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;
import vazkii.botania.client.render.entity.BaseSparkRenderer;
import vazkii.botania.common.component.BotaniaDataComponents;

/**
 * @author cnlimiter
 */
public class AlphaSparkRender extends BaseSparkRenderer<AlphaSparkEntity> {

    public AlphaSparkRender(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Nullable
    @Override
    public TextureAtlasSprite getSpinningIcon(AlphaSparkEntity entity) {
        ItemStack upgrade = entity.getUpgrade();
        if (upgrade.isEmpty()) {
            return null;
        }
        ResourceLocation icon = upgrade.get(BotaniaDataComponents.AUGMENT_ICON);
        if (icon == null) {
            return null;
        }
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(icon);
    }
}
