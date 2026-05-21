package committee.nova.mods.avaritia_integration.module.mekanism.common.registries;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.common.registration.impl.ChemicalDeferredRegister;
import mekanism.common.registration.impl.DeferredChemical;

public class MekIntegrationChemicals {

    private MekIntegrationChemicals() {}

    public static final ChemicalDeferredRegister CHEMICALS = new ChemicalDeferredRegister(AvaritiaIntegration.MOD_ID);

    public static final DeferredChemical<Chemical> INFINITY = CHEMICALS.registerInfuse("infinity", 0x5A4630);
    public static final DeferredChemical<Chemical> NEUTRON = CHEMICALS.registerInfuse("neutron", 0x7C8588);

    public static ChemicalStack infinity(long amount) {
        return INFINITY.asStack(amount);
    }

    public static ChemicalStack neutron(long amount) {
        return NEUTRON.asStack(amount);
    }
}
