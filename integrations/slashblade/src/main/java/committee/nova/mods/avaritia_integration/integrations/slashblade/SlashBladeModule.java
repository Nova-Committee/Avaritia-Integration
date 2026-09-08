package committee.nova.mods.avaritia_integration.integrations.slashblade;

import committee.nova.mods.avaritia_integration.integrations.slashblade.client.SlashBladeClient;
import committee.nova.mods.avaritia_integration.integrations.slashblade.item.StredgeuniverseItem;
import committee.nova.mods.avaritia_integration.integrations.slashblade.registry.SlashBladeIntegrationItems;
import committee.nova.mods.avaritia_integration.integrations.slashblade.registry.SlashBladeIntegrationSlashArts;
import committee.nova.mods.avaritia_integration.module.Module;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;

public final class SlashBladeModule implements Module {

    public static final String MOD_ID = "slashblade";

    @Override
    public void init(IEventBus registryBus) {
        SlashBladeIntegrationSlashArts.REGISTRY.register(registryBus);
        SlashBladeIntegrationItems.REGISTRY.register(registryBus);
    }

    @Override
    public void registerClientEvent(IEventBus modBus, IEventBus gameBus) {
        SlashBladeClient.register(modBus);
    }

    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters,
            CreativeModeTab.Output output) {
        ItemStack stack = SlashBladeIntegrationItems.STREDGEUNIVERSE.get().getDefaultInstance();
        if (SlashBladeIntegrationItems.STREDGEUNIVERSE.get() instanceof StredgeuniverseItem item) {
            item.setupBlade(stack, parameters.holders());
        }
        output.accept(stack);
    }
}
