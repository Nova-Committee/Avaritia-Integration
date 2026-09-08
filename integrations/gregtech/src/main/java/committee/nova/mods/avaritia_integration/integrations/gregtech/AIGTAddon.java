package committee.nova.mods.avaritia_integration.integrations.gregtech;

import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

@GTAddon(AIGregTechIntegrationMod.MOD_ID)
public final class AIGTAddon implements IGTAddon {

    private GTRegistrate registrate;

    @Override
    public GTRegistrate getRegistrate() {
        if (registrate == null) {
            registrate = GTRegistrate.create(AIGregTechIntegrationMod.MOD_ID);
        }
        return registrate;
    }

    @Override
    public void gtInitComplete() {}
}
