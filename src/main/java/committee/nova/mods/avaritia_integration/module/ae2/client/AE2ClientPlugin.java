package committee.nova.mods.avaritia_integration.module.ae2.client;

import net.minecraftforge.eventbus.api.IEventBus;

public class AE2ClientPlugin
{
    public static void register()
    {
        AE2StorageModels.registerStorageModels();
    }

    public static void registerStorageLED(IEventBus modEventBus)
    {
        modEventBus.addListener(AE2StorageModels::registerItemColors);
    }
}
