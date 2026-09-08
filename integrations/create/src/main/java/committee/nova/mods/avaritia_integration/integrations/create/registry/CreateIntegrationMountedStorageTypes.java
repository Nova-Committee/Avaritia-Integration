package committee.nova.mods.avaritia_integration.integrations.create.registry;

import java.util.function.Supplier;

import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;

import committee.nova.mods.avaritia_integration.integrations.create.CreateModule;
import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_depot.ExtremeDepotMountedStorageType;

public class CreateIntegrationMountedStorageTypes {

    private static final CreateRegistrate REGISTRATE = CreateModule.REGISTRATE;

    public static final RegistryEntry<MountedItemStorageType<?>, ExtremeDepotMountedStorageType> EXTREME_DEPOT = simpleItem(
            "extreme_depot", ExtremeDepotMountedStorageType::new);

    private static <T extends MountedItemStorageType<?>> RegistryEntry<MountedItemStorageType<?>, T> simpleItem(
            String name, Supplier<T> supplier) {
        return REGISTRATE.mountedItemStorage(name, supplier).register();
    }

    public static void register() {
    }
}
