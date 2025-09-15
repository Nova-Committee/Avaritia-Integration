package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.api.event.InitEvent;
import committee.nova.mods.avaritia_integration.api.module.AbModule;
import committee.nova.mods.avaritia_integration.api.module.InModule;
import committee.nova.mods.avaritia_integration.common.item.StredgeuniverseItem;
import committee.nova.mods.avaritia_integration.util.MeteoriteSwordSlashArts;
import mods.flammpfeil.slashblade.client.renderer.model.BladeModel;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

import static committee.nova.mods.avaritia_integration.init.registry.Registries.item;

/**
 * @author: cnlimiter
 */
@InModule(value = "slashblade_reg", dependencies = "slashblade")
@InModule.Subscriber(modBus = true)
public class SlashbladeReg extends AbModule {
    public static final DeferredRegister<SlashArts> REGISTRY = DeferredRegister.create(SlashArts.REGISTRY_KEY, AvaritiaIntegration.MOD_ID);
    public static final RegistryObject<Item> stredgeuniverse = item("stredgeuniverse", StredgeuniverseItem::new);

    public static final RegistryObject<SlashArts> meteoritesword = REGISTRY.register("meteorite_sword",
            () -> new MeteoriteSwordSlashArts((e) -> ComboStateRegistry.NONE.getId()));

    @Override
    protected void preInit() {
        REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public void Baked(final ModelEvent.ModifyBakingResult event) {
        ModelResourceLocation loc2 = new ModelResourceLocation(
                Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(SlashbladeReg.stredgeuniverse.get())), "inventory");
        BladeModel model2 = new BladeModel(event.getModels().get(loc2), event.getModelBakery());
        event.getModels().put(loc2, model2);
    }
}
