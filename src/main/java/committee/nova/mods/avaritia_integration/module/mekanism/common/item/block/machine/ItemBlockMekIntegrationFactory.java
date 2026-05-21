package committee.nova.mods.avaritia_integration.module.mekanism.common.item.block.machine;

import committee.nova.mods.avaritia_integration.module.mekanism.common.block.attribute.AttributeMekIntegrationFactoryType;
import committee.nova.mods.avaritia_integration.module.mekanism.common.block.prefab.BlockMekIntegrationFactoryMachine.BlockMekIntegrationFactory;
import mekanism.api.text.EnumColor;
import mekanism.common.MekanismLang;
import mekanism.common.attachments.component.AttachedEjector;
import mekanism.common.attachments.component.AttachedSideConfig;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registries.MekanismDataComponents;
import mekanism.common.tier.FactoryTier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemBlockMekIntegrationFactory extends ItemBlockTooltip<BlockMekIntegrationFactory<?>> {

    private static AttachedSideConfig getSideConfig(BlockMekIntegrationFactory<?> block) {
        return switch (Attribute.getOrThrow(block.builtInRegistryHolder(), AttributeMekIntegrationFactoryType.class).getMekIntegrationFactoryType()) {
            case NEUTRON_COLLECTING -> AttachedSideConfig.ELECTRIC_MACHINE;
            case SINGULARITY_COMPRESSING -> AttachedSideConfig.ADVANCED_MACHINE;
        };
    }

    public ItemBlockMekIntegrationFactory(BlockMekIntegrationFactory<?> block, Properties properties) {
        super(block, true, properties
                .component(MekanismDataComponents.SORTING, false)
                .component(MekanismDataComponents.EJECTOR, AttachedEjector.DEFAULT)
                .component(MekanismDataComponents.SIDE_CONFIG, getSideConfig(block))
        );
    }

    public FactoryTier getTier() {
        return Attribute.getTier(getBlock(), FactoryTier.class);
    }

    @Override
    protected void addTypeDetails(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        //Should always be present but validate it just in case
        AttributeMekIntegrationFactoryType factoryType = Attribute.get(getBlock(), AttributeMekIntegrationFactoryType.class);
        if (factoryType != null) {
            tooltip.add(MekanismLang.FACTORY_TYPE.translateColored(EnumColor.INDIGO, EnumColor.GRAY, factoryType.getMekIntegrationFactoryType()));
        }
        super.addTypeDetails(stack, context, tooltip, flag);
    }
}
