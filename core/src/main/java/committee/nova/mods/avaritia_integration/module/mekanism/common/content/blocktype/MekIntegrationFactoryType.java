package committee.nova.mods.avaritia_integration.module.mekanism.common.content.blocktype;

import committee.nova.mods.avaritia_integration.module.mekanism.common.MekIntegrationLang;
import committee.nova.mods.avaritia_integration.module.mekanism.common.registries.MekIntegrationBlockTypes;
import committee.nova.mods.avaritia_integration.module.mekanism.common.registries.MekIntegrationBlocks;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.text.IHasTranslationKey;
import mekanism.common.MekanismLang;
import mekanism.common.registration.impl.BlockRegistryObject;

import java.util.Locale;
import java.util.function.Supplier;

@NothingNullByDefault
public enum MekIntegrationFactoryType implements IHasTranslationKey {
    NEUTRON_COLLECTING("neutron_collecting", MekIntegrationLang.NEUTRON_COLLECTING, () -> MekIntegrationBlockTypes.NEUTRON_COLLECTOR, () -> MekIntegrationBlocks.NEUTRON_COLLECTOR),
    SINGULARITY_COMPRESSING("singularity_compressing", MekIntegrationLang.NEUTRON_COMPRESSING, () -> MekIntegrationBlockTypes.SINGULARITY_COMPRESSOR, () -> MekIntegrationBlocks.SINGULARITY_COMPRESSOR);

    private final String registryNameComponent;
    private final MekanismLang langEntry;
    private final Supplier<MekIntegrationFactoryMachine<?>> baseMachine;
    private final Supplier<BlockRegistryObject<?, ?>> baseBlock;

    MekIntegrationFactoryType(String registryNameComponent, MekanismLang langEntry, Supplier<MekIntegrationFactoryMachine<?>> baseMachine, Supplier<BlockRegistryObject<?, ?>> baseBlock) {
        this.registryNameComponent = registryNameComponent;
        this.langEntry = langEntry;
        this.baseMachine = baseMachine;
        this.baseBlock = baseBlock;
    }

    public String getRegistryNameComponent() {
        return registryNameComponent;
    }

    public String getRegistryNameComponentCapitalized() {
        String name = getRegistryNameComponent();
        return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
    }

    public MekIntegrationFactoryMachine<?> getBaseMachine() {
        return baseMachine.get();
    }

    public BlockRegistryObject<?, ?> getBaseBlock() {
        return baseBlock.get();
    }

    @Override
    public String getTranslationKey() {
        return langEntry.getTranslationKey();
    }
}
