package committee.nova.mods.avaritia_integration.module.ae2.client;

import appeng.api.client.StorageCellModels;
import committee.nova.mods.avaritia_integration.module.ae2.item.AEUniversalCellItem;
import committee.nova.mods.avaritia_integration.module.ae2.registry.AE2IntegrationItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;

public class AE2StorageModels
{
    private static final ResourceLocation MODEL_CELL_CREATIVE = new ResourceLocation("ae2:block/drive/cells/creative_cell");

    public static void registerStorageModels()
    {
        StorageCellModels.registerModel(AE2IntegrationItems.INFINITY_ME_STORAGE_CELL.get(), MODEL_CELL_CREATIVE);
        StorageCellModels.registerModel(AE2IntegrationItems.INFINITY_ME_STORAGE_CELL_BIG.get(), MODEL_CELL_CREATIVE);
    }

    public static void registerItemColors(RegisterColorHandlersEvent.Item event)
    {
        event.register(AEUniversalCellItem::getColor,
                AE2IntegrationItems.INFINITY_ME_STORAGE_CELL.get(),
                AE2IntegrationItems.INFINITY_ME_STORAGE_CELL_BIG.get()
        );
    }
}
