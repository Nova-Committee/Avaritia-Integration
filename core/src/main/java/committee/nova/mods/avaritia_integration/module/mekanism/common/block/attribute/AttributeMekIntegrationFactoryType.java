package committee.nova.mods.avaritia_integration.module.mekanism.common.block.attribute;

import committee.nova.mods.avaritia_integration.module.mekanism.common.content.blocktype.MekIntegrationFactoryType;
import mekanism.common.block.attribute.Attribute;
import org.jetbrains.annotations.NotNull;

public class AttributeMekIntegrationFactoryType implements Attribute {

    private final MekIntegrationFactoryType type;

    public AttributeMekIntegrationFactoryType(MekIntegrationFactoryType type) {
        this.type = type;
    }

    @NotNull
    public MekIntegrationFactoryType getMekIntegrationFactoryType() {
        return type;
    }
}
