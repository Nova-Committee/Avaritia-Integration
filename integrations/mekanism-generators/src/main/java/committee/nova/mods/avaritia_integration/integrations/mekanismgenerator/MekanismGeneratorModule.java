package committee.nova.mods.avaritia_integration.integrations.mekanismgenerator;

import committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.client.MekanismGeneratorClient;
import committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.registries.GenIntegrationBlockEntityTypes;
import committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.registries.GenIntegrationBlocks;
import committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.registries.GenIntegrationContainerTypes;
import committee.nova.mods.avaritia_integration.integrations.mekanismgenerator.common.registries.GenIntegrationItems;
import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;

import net.minecraft.core.Holder;
import net.minecraft.world.item.CreativeModeTab.ItemDisplayParameters;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;

@ModuleEntry(id = MekanismGeneratorModule.MOD_ID, target = @ModMeta(MekanismGeneratorModule.MOD_ID))
public class MekanismGeneratorModule implements Module {

    public static final String MOD_ID = "mekanismgenerators";

    @Override
    public committee.nova.mods.avaritia_integration.api.load.IntegrationRule defaultLoadRule() {
        return Module.rule(Module.dependency("mekanism"), Module.dependency(MOD_ID));
    }

    @Override
    public void init(IEventBus registryBus) {
        GenIntegrationItems.ITEMS.register(registryBus);
        GenIntegrationBlocks.BLOCKS.register(registryBus);
        GenIntegrationContainerTypes.CONTAINER_TYPES.register(registryBus);
        GenIntegrationBlockEntityTypes.TILE_ENTITY_TYPES.register(registryBus);
    }

    @Override
    public void registerClientEvent(IEventBus modBus, IEventBus gameBus) {
        MekanismGeneratorClient.register(modBus);
    }

    @Override
    public void collectCreativeTabItems(ItemDisplayParameters parameters, Output output) {
        for (Holder<Item> itemHolder : GenIntegrationItems.ITEMS.getEntries()) {
            output.accept(itemHolder.value());
        }
        for (Holder<Item> blockHolder : GenIntegrationBlocks.BLOCKS.getSecondaryEntries()) {
            output.accept(blockHolder.value());
        }
    }
}
