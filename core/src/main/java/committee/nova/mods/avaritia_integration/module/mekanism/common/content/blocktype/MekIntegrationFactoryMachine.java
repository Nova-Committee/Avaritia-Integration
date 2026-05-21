package committee.nova.mods.avaritia_integration.module.mekanism.common.content.blocktype;

import committee.nova.mods.avaritia_integration.module.mekanism.common.block.attribute.AttributeMekIntegrationFactoryType;
import committee.nova.mods.avaritia_integration.module.mekanism.common.registries.MekIntegrationBlocks;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.AttributeUpgradeable;
import mekanism.common.content.blocktype.Machine;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.base.TileEntityMekanism;

import java.util.Objects;
import java.util.function.Supplier;

public class MekIntegrationFactoryMachine<TILE extends TileEntityMekanism> extends Machine<TILE> {

    public MekIntegrationFactoryMachine(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, MekanismLang description, MekIntegrationFactoryType factoryType) {
        super(tileEntityRegistrar, description);
        add(new AttributeMekIntegrationFactoryType(factoryType), new AttributeUpgradeable(() -> MekIntegrationBlocks.getMekIntegrationFactory(FactoryTier.BASIC, getMekIntegrationFactoryType())));
    }

    public MekIntegrationFactoryType getMekIntegrationFactoryType() {
        return Objects.requireNonNull(get(AttributeMekIntegrationFactoryType.class)).getMekIntegrationFactoryType();
    }

    public static class MekIntegrationMachineBuilder<MACHINE extends Machine<TILE>, TILE extends TileEntityMekanism, T extends MekIntegrationMachineBuilder<MACHINE, TILE, T>> extends BlockTileBuilder<MACHINE, TILE, T> {

        protected MekIntegrationMachineBuilder(MACHINE holder) {
            super(holder);
        }

        public static <TILE extends TileEntityMekanism> MekIntegrationMachineBuilder<Machine<TILE>, TILE, ?> createMekIntegrationMachine(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, MekanismLang description) {
            return new MekIntegrationMachineBuilder<>(new Machine<>(tileEntityRegistrar, description));
        }

        public static <TILE extends TileEntityMekanism> MekIntegrationMachineBuilder<MekIntegrationFactoryMachine<TILE>, TILE, ?> createMekIntegrationFactoryMachine(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar,
                                                                                                                                   MekanismLang description, MekIntegrationFactoryType factoryType) {
            return new MekIntegrationMachineBuilder<>(new MekIntegrationFactoryMachine<>(tileEntityRegistrar, description, factoryType));
        }
    }
}
