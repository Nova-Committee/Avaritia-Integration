package committee.nova.mods.avaritia_integration.integrations.ae2;

import committee.nova.mods.avaritia_integration.integrations.ae2.datagen.AE2DataGen;

import net.neoforged.bus.api.IEventBus;

class ModRun {

    public static void init(IEventBus bus) {
        bus.addListener(AE2DataGen::gatherData);
    }
}
