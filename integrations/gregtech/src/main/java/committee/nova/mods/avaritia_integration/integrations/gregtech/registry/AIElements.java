package committee.nova.mods.avaritia_integration.integrations.gregtech.registry;

import com.gregtechceu.gtceu.api.material.Element;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;

public final class AIElements {

    public static final Element Neutron = new Element(0, 999, -1, null, "neutron", "Nu", false);
    public static final Element Infinity = new Element(999, 999, -1, null, "infinity", "∞", false);

    private AIElements() {}

    public static void init() {
        GTRegistries.register(GTRegistries.ELEMENTS, AvaritiaIntegration.rl("neutron"), Neutron);
        GTRegistries.register(GTRegistries.ELEMENTS, AvaritiaIntegration.rl("infinity"), Infinity);
    }
}
