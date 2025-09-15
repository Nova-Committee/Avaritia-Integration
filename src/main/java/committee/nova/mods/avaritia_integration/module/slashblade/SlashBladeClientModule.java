package committee.nova.mods.avaritia_integration.module.slashblade;

import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;
import committee.nova.mods.avaritia_integration.module.slashblade.registry.SlashBladeIntegrationItems;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@ModuleEntry(id = SlashBladeModule.MOD_ID + "_client", target = @ModMeta(SlashBladeModule.MOD_ID))
public final class SlashBladeClientModule implements Module {
    @Override
    public void registerEvent(IEventBus modBus, IEventBus gameBus) {
        modBus.register(SlashBladeClientModule.class);
    }

    @SubscribeEvent
    public static void Baked(final ModelEvent.ModifyBakingResult event) {
        ModelResourceLocation loc2 = new ModelResourceLocation(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(SlashBladeIntegrationItems.STREDGEUNIVERSE.get())), "inventory");
        BladeModel model2 = new BladeModel(event.getModels().get(loc2), event.getModelBakery());
        event.getModels().put(loc2, model2);
    }
}
