package committee.nova.mods.avaritia_integration.integrations.enderio.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public final class EnderIODataGen {

    private EnderIODataGen() {}

    public static void gatherData(GatherDataEvent event) {
        if (!event.includeServer()) {
            return;
        }
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> future = event.getLookupProvider();
        generator.addProvider(true, new EnderIORecipes(output, future));
    }
}
