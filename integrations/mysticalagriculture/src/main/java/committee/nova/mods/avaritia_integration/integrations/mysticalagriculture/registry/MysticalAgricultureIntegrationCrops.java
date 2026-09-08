package committee.nova.mods.avaritia_integration.integrations.mysticalagriculture.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;

import com.blakebr0.mysticalagradditions.lib.ModCropTiers;
import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.blakebr0.mysticalagriculture.api.crop.CropTier;
import com.blakebr0.mysticalagriculture.api.crop.CropType;
import com.blakebr0.mysticalagriculture.api.lib.LazyIngredient;

public final class MysticalAgricultureIntegrationCrops {

    public static final Crop BLAZE_CUBE = new Crop(AvaritiaIntegration.rl("blaze_cube"), CropTier.FIVE,
            CropType.RESOURCE, LazyIngredient.tag("c:storage_blocks/blaze_cube"));

    public static final Crop CRYSTAL_MATRIX = new Crop(AvaritiaIntegration.rl("crystal_matrix"), ModCropTiers.SIX,
            CropType.RESOURCE, LazyIngredient.tag("c:storage_blocks/crystal_matrix"));

    public static final Crop INFINITY = new Crop(AvaritiaIntegration.rl("infinity"), ModCropTiers.SIX,
            CropType.RESOURCE, LazyIngredient.tag("c:ingots/infinity"));
}
