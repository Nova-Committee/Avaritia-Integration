package committee.nova.mods.avaritia_integration.module.mekanism.common.content.blocktype;

import committee.nova.mods.avaritia_integration.module.mekanism.common.block.attribute.AttributeMekIntegrationFactoryType;
import committee.nova.mods.avaritia_integration.module.mekanism.common.registries.MekIntegrationBlocks;
import committee.nova.mods.avaritia_integration.module.mekanism.common.registries.MekIntegrationContainerTypes;
import committee.nova.mods.avaritia_integration.module.mekanism.common.tile.factory.TileEntityMIFactory;
import committee.nova.mods.avaritia_integration.module.mekanism.common.util.MekIntegrationEnumUtils;
import committee.nova.mods.avaritia_integration.module.mekanism.common.util.MekIntegrationUtils;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.*;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.lib.math.Pos3D;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tier.FactoryTier;
import mekanism.api.math.MathUtils;
import mekanism.common.util.EnumUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

import static mekanism.common.content.blocktype.BlockShapes.ENRICHING_FACTORY;

public class MekIntegrationFactory<TILE extends TileEntityMIFactory<?>> extends MekIntegrationFactoryMachine<TILE> {

    private final MekIntegrationFactoryMachine<?> origMachine;

    public MekIntegrationFactory(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, Supplier<ContainerTypeRegistryObject<? extends MekanismContainer>> containerRegistrar,
                                 MekIntegrationFactoryMachine<?> origMachine, FactoryTier tier) {
        super(tileEntityRegistrar, MekanismLang.DESCRIPTION_FACTORY, origMachine.getMekIntegrationFactoryType());
        this.origMachine = origMachine;
        setMachineData(tier);
        add(new AttributeGui(containerRegistrar, null), new AttributeTier<>(tier));

        if (tier.ordinal() < MekIntegrationUtils.getFactoryTier().length - 1) {
            add(new AttributeUpgradeable(() -> MekIntegrationBlocks.getMekIntegrationFactory(EnumUtils.FACTORY_TIERS[tier.ordinal() + 1], origMachine.getMekIntegrationFactoryType())));
        }
    }

    private void setMachineData(FactoryTier tier) {
        setFrom(origMachine, AttributeSound.class, AttributeMekIntegrationFactoryType.class, AttributeUpgradeSupport.class);
        AttributeEnergy origEnergy = origMachine.get(AttributeEnergy.class);
        add(new AttributeEnergy(origEnergy::getUsage, () -> MathUtils.multiplyClamped(Math.max(origEnergy.getConfigStorage() / 2, origEnergy.getUsage()), tier.processes)));
    }

    public static class MekIntegrationFactoryBuilder<FACTORY extends MekIntegrationFactory<TILE>, TILE extends TileEntityMIFactory<?>, T extends MekIntegrationMachineBuilder<FACTORY, TILE, T>>
            extends BlockTileBuilder<FACTORY, TILE, T> {

        protected MekIntegrationFactoryBuilder(FACTORY holder) {
            super(holder);
        }

        @SuppressWarnings("unchecked")
        public static <TILE extends TileEntityMIFactory<?>> MekIntegrationFactoryBuilder<MekIntegrationFactory<TILE>, TILE, ?> createMekIntegrationFactory(Supplier<?> tileEntityRegistrar, MekIntegrationFactoryType type,
                                                                                                                                                           FactoryTier tier) {
            // this is dirty but unfortunately necessary for things to play right
            MekIntegrationFactoryBuilder<MekIntegrationFactory<TILE>, TILE, ?> builder = new MekIntegrationFactoryBuilder<>(new MekIntegrationFactory<>((Supplier<TileEntityTypeRegistryObject<TILE>>) tileEntityRegistrar,
                    () -> MekIntegrationContainerTypes.FACTORY, type.getBaseMachine(), tier));
            //Note, we can't just return the builder here as then it gets all confused about object types, so we just
            // assign the value here, and then return the builder itself as it is the same object
            builder.withComputerSupport(tier, type.getRegistryNameComponentCapitalized() + "Factory");
            builder.withCustomShape(getShape(type));
            builder.with(switch (type) {
                case NEUTRON_COLLECTING ->
                        AttributeSideConfig.create(TransmissionType.ITEM, TransmissionType.CHEMICAL, TransmissionType.ENERGY);
                case SINGULARITY_COMPRESSING -> AttributeSideConfig.ELECTRIC_MACHINE;
            });
            builder.replace(new AttributeParticleFX().addDense(ParticleTypes.SMOKE, 5, rand -> new Pos3D(
                    rand.nextFloat() * 0.7F - 0.3F,
                    rand.nextFloat() * 0.1F + 0.7F,
                    rand.nextFloat() * 0.7F - 0.3F
            )));
            return builder;
        }

        private static VoxelShape[] getShape(MekIntegrationFactoryType type) {
            return switch (type) {
                case NEUTRON_COLLECTING, SINGULARITY_COMPRESSING -> ENRICHING_FACTORY;
            };
        }
    }
}
