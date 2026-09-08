package committee.nova.mods.avaritia_integration.integrations.mysticalagriculture.plugin;

import committee.nova.mods.avaritia_integration.integrations.mysticalagriculture.AIMysticalAgricultureIntegrationMod;
import committee.nova.mods.avaritia_integration.integrations.mysticalagriculture.registry.MysticalAgricultureIntegrationBlocks;
import committee.nova.mods.avaritia_integration.integrations.mysticalagriculture.registry.MysticalAgricultureIntegrationCrops;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;

import com.blakebr0.mysticalagriculture.api.IMysticalAgriculturePlugin;
import com.blakebr0.mysticalagriculture.api.MysticalAgriculturePlugin;
import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.blakebr0.mysticalagriculture.api.registry.ICropRegistry;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

@MysticalAgriculturePlugin
public final class MysticalAgricultureIntegrationPlugin implements IMysticalAgriculturePlugin {

    private static final boolean DEBUG = !FMLEnvironment.production;

    @Override
    public void onRegisterCrops(ICropRegistry registry) {
        if (!ModList.get().isLoaded(AIMysticalAgricultureIntegrationMod.AGRADDITIONS_MOD_ID)) {
            return;
        }
        registry.register(withRequiredMods(MysticalAgricultureIntegrationCrops.BLAZE_CUBE,
                AIMysticalAgricultureIntegrationMod.AGRADDITIONS_MOD_ID));
        registry.register(withRequiredMods(MysticalAgricultureIntegrationCrops.CRYSTAL_MATRIX,
                AIMysticalAgricultureIntegrationMod.AGRADDITIONS_MOD_ID));
        registry.register(withRequiredMods(MysticalAgricultureIntegrationCrops.INFINITY,
                AIMysticalAgricultureIntegrationMod.AGRADDITIONS_MOD_ID));
    }

    @Override
    public void onPostRegisterCrops(ICropRegistry registry) {
        if (!ModList.get().isLoaded(AIMysticalAgricultureIntegrationMod.AGRADDITIONS_MOD_ID)) {
            return;
        }
        MysticalAgricultureIntegrationCrops.INFINITY
                .setCruxBlock(MysticalAgricultureIntegrationBlocks.INFINITY_CRUX);
        MysticalAgricultureIntegrationCrops.CRYSTAL_MATRIX
                .setCruxBlock(MysticalAgricultureIntegrationBlocks.CRYSTAL_MATRIX_CRUX);
    }

    private static Crop withRequiredMods(Crop crop, String... mods) {
        if (DEBUG) {
            return crop;
        }
        Stream<String> stream = Arrays.stream(mods);
        ModList list = ModList.get();
        Objects.requireNonNull(list);
        boolean enabled = stream.anyMatch(list::isLoaded);
        return crop.setEnabled(enabled);
    }
}
