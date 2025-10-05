package committee.nova.mods.avaritia_integration.module.ae2;

import appeng.api.storage.StorageCells;
import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEItems;
import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;
import committee.nova.mods.avaritia_integration.module.ae2.client.AE2ClientPlugin;
import committee.nova.mods.avaritia_integration.module.ae2.me.AEUniversalCellHandler;
import committee.nova.mods.avaritia_integration.module.ae2.registry.AE2IntegrationItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;

@ModuleEntry(id = AE2Module.MOD_ID, target = @ModMeta(AE2Module.MOD_ID))
public final class AE2Module implements Module {
    public static final String MOD_ID = "ae2";

    @Override
    public void init(IEventBus registryBus) {
        AE2IntegrationItems.REGISTRY.register(registryBus);
    }

    @Override
    public void registerClientEvent(IEventBus modBus, IEventBus gameBus)
    {
        Module.super.registerClientEvent(modBus, gameBus);
        AE2ClientPlugin.registerStorageLED(modBus);
    }

    @Override
    public void process()
    {
        Module.super.process();
        StorageCells.addCellHandler(AEUniversalCellHandler.INSTANCE);

        Upgrades.add(AEItems.FUZZY_CARD, AE2IntegrationItems.INFINITY_ME_STORAGE_CELL.get(), 1);
        Upgrades.add(AEItems.VOID_CARD, AE2IntegrationItems.INFINITY_ME_STORAGE_CELL.get(), 1);
        Upgrades.add(AEItems.INVERTER_CARD, AE2IntegrationItems.INFINITY_ME_STORAGE_CELL.get(), 1);
        Upgrades.add(AEItems.EQUAL_DISTRIBUTION_CARD, AE2IntegrationItems.INFINITY_ME_STORAGE_CELL.get(), 1);
    }

    @Override
    public void processClient()
    {
        AE2ClientPlugin.register();
    }

    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output)
    {
        output.accept(AE2IntegrationItems.INFINITY_ME_STORAGE_COMPONENT.get());
        output.accept(AE2IntegrationItems.INFINITY_ME_STORAGE_CELL.get());
    }
}
