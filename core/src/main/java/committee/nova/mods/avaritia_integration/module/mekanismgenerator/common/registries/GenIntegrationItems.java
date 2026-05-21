package committee.nova.mods.avaritia_integration.module.mekanismgenerator.common.registries;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import net.minecraft.world.item.Item;

public class GenIntegrationItems {

    private GenIntegrationItems() {

    }

    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(AvaritiaIntegration.MOD_ID);

    public static final ItemRegistryObject<Item> INFINITY_SOLAR_PANEL = ITEMS.register("infinity_solar_panel");
    public static final ItemRegistryObject<Item> NEUTRON_SOLAR_PANEL = ITEMS.register("neutron_solar_panel");
}
