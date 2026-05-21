package committee.nova.mods.avaritia_integration.integrations;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AIPneumaticCraftIntegrationMod.MOD_ID)
public final class AIPneumaticCraftIntegrationMod {

    public static final String MOD_ID = "avaritia_integration_pneumaticcraft";

    public AIPneumaticCraftIntegrationMod(IEventBus bus, ModContainer modContainer) {
        if (false) {
            ModRun.init(bus);
        }
    }
}
