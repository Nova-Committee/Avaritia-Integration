package committee.nova.mods.avaritia_integration.module.slashblade;

import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;
import committee.nova.mods.avaritia_integration.module.slashblade.registry.SlashBladeIntegrationItems;
import committee.nova.mods.avaritia_integration.module.slashblade.registry.SlashBladeIntegrationSlashArts;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@ModuleEntry(id = SlashBladeModule.MOD_ID, target = @ModMeta(SlashBladeModule.MOD_ID))
public final class SlashBladeModule implements Module {
    public static final String MOD_ID = "slashblade";

    @Override
    public void init(IEventBus registryBus) {
        SlashBladeIntegrationSlashArts.REGISTRY.register(registryBus);
        SlashBladeIntegrationItems.REGISTRY.register(registryBus);
    }

    @Override
    public void registerClientEvent(IEventBus modBus, IEventBus gameBus) {
        modBus.addListener(SlashBladeModule::afterBakeModel);
    }

    public static void afterBakeModel(ModelEvent.ModifyBakingResult event) {
        ModelResourceLocation loc2 = new ModelResourceLocation(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(SlashBladeIntegrationItems.STREDGEUNIVERSE.get())), "inventory");
        BladeModel model2 = new BladeModel(event.getModels().get(loc2), event.getModelBakery());
        event.getModels().put(loc2, model2);
    }

    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(SlashBladeIntegrationItems.STREDGEUNIVERSE.get());
    }
}
