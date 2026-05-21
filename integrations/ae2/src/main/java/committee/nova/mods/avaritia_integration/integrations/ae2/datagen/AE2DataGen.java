package committee.nova.mods.avaritia_integration.integrations.ae2.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public final class AE2DataGen {

    private AE2DataGen() {}

    public static void gatherData(GatherDataEvent event) {
        if (!event.includeServer()) {
            return;
        }
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> future = event.getLookupProvider();
        generator.addProvider(true, new AE2Recipes(output, future));
    }
}
