package committee.nova.mods.avaritia_integration.module.mekanism.common.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.mekanism.common.config.MekIntegrationEnergy;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.item.ItemEnergized;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class MekIntegrationItems {

    private MekIntegrationItems() {

    }

    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(AvaritiaIntegration.MOD_ID);

    public static final ItemRegistryObject<ItemEnergized> INFINITY_ENERGY_TABLET = ITEMS.register("infinity_energy_tablet", () -> new ItemEnergized(() -> MekIntegrationEnergy.INFINITY_TABLET_RATE, () -> MekIntegrationEnergy.INFINITY_TABLET_MAX, BasicEnergyContainer.alwaysTrue, BasicEnergyContainer.alwaysTrue, new Item.Properties().rarity(Rarity.EPIC)));
    public static final ItemRegistryObject<ItemEnergized> NEUTRON_ENERGY_TABLET = ITEMS.register("neutron_energy_tablet", () -> new ItemEnergized(() -> MekIntegrationEnergy.NEUTRON_TABLET_RATE, () -> MekIntegrationEnergy.NEUTRON_TABLET_MAX, BasicEnergyContainer.alwaysTrue, BasicEnergyContainer.alwaysTrue, new Item.Properties().rarity(Rarity.RARE)));
    public static final ItemRegistryObject<Item> INFINITY_CONTROL_CIRCUIT = ITEMS.register("infinity_control_circuit", Rarity.EPIC);
    public static final ItemRegistryObject<Item> NEUTRON_CONTROL_CIRCUIT = ITEMS.register("neutron_control_circuit", Rarity.EPIC);
    public static final ItemRegistryObject<Item> ALLOY_INFINITY = ITEMS.register("alloy_infinity", Rarity.EPIC);
    public static final ItemRegistryObject<Item> ALLOY_NEUTRON = ITEMS.register("alloy_neutron", Rarity.EPIC);
    public static final ItemRegistryObject<Item> ENRICHED_INFINITY = ITEMS.register("enriched_infinity", Rarity.EPIC);
    public static final ItemRegistryObject<Item> ENRICHED_NEUTRON = ITEMS.register("enriched_neutron", Rarity.EPIC);
}
