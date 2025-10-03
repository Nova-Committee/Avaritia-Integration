package committee.nova.mods.avaritia_integration.module.ae2.item;


import appeng.core.definitions.AEItems;
import committee.nova.mods.avaritia_integration.module.ae2.registry.AE2IntegrationItems;

public class InfiniteCellItem extends AEUniversalCellItem
{
    public InfiniteCellItem(Properties pProperties, double idleDrain)
    {
        super(pProperties, AE2IntegrationItems.INFINITY_ME_STORAGE_COMPONENT.get(),
                AEItems.ITEM_CELL_HOUSING.asItem(),
                idleDrain, -1, -1);
    }
}
