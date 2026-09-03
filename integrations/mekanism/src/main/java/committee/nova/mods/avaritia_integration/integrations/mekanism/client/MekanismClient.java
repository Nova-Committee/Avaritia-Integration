package committee.nova.mods.avaritia_integration.integrations.mekanism.client;

import committee.nova.mods.avaritia_integration.integrations.mekanism.client.gui.machine.GuiMIFactory;
import committee.nova.mods.avaritia_integration.integrations.mekanism.client.gui.machine.GuiNeutronCollector;
import committee.nova.mods.avaritia_integration.integrations.mekanism.client.gui.machine.GuiSingularityCompressor;
import committee.nova.mods.avaritia_integration.integrations.mekanism.common.registries.MekIntegrationContainerTypes;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import mekanism.client.ClientRegistrationUtil;

public final class MekanismClient {

    private MekanismClient() {}

    public static void register(IEventBus modBus) {
        modBus.addListener(EventPriority.LOW, MekanismClient::registerScreen);
    }

    private static void registerScreen(RegisterMenuScreensEvent event) {
        ClientRegistrationUtil.registerScreen(event, MekIntegrationContainerTypes.NEUTRON_COLLECTOR,
                GuiNeutronCollector::new);
        ClientRegistrationUtil.registerScreen(event, MekIntegrationContainerTypes.SINGULARITY_COMPRESSOR,
                GuiSingularityCompressor::new);
        ClientRegistrationUtil.registerScreen(event, MekIntegrationContainerTypes.FACTORY, GuiMIFactory::new);
    }
}
