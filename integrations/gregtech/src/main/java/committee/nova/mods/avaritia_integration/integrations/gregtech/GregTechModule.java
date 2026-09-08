package committee.nova.mods.avaritia_integration.integrations.gregtech;

import com.gregtechceu.gtceu.data.material.GTMaterials;
import committee.nova.mods.avaritia_integration.integrations.gregtech.registry.AIElements;
import committee.nova.mods.avaritia_integration.integrations.gregtech.registry.AIMaterials;
import committee.nova.mods.avaritia_integration.module.Module;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

public final class GregTechModule implements Module {

    private static boolean materialsRegistered;

    @Override
    public void registerEvent(IEventBus modBus, IEventBus gameBus) {
        modBus.addListener(EventPriority.HIGHEST, GregTechModule::onRegister);
    }

    private static void onRegister(RegisterEvent event) {
        if (materialsRegistered || GTMaterials.Aluminium == null) {
            return;
        }
        materialsRegistered = true;
        AIElements.init();
        AIMaterials.init();
    }
}
