package committee.nova.mods.avaritia_integration.integrations.slashblade;

import committee.nova.mods.avaritia_integration.api.load.IntegrationDataPackRegistrar;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRule;
import committee.nova.mods.avaritia_integration.api.load.IntegrationRuntime;
import committee.nova.mods.avaritia_integration.module.ModModule;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(AISlashBladeIntegrationMod.MOD_ID)
public final class AISlashBladeIntegrationMod implements ModModule {

    public static final String MOD_ID = "avaritia_integration_slashblade";
    public static final String DEPENDENCY_MOD_ID = "slashblade";

    public AISlashBladeIntegrationMod(IEventBus bus, ModContainer modContainer) {
        IntegrationDataPackRegistrar.register(bus, MOD_ID, "Avaritia Integration SlashBlade Data");
        if (IntegrationRuntime.shouldLoad(MOD_ID, this)) {
            IntegrationRuntime.load(MOD_ID, bus, new SlashBladeModule());
        }
    }

    @Override
    public IntegrationRule defaultLoadRule() {
        return ModModule.rule(ModModule.dependency(DEPENDENCY_MOD_ID));
    }
}
