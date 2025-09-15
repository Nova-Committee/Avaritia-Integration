package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.api.module.AbModule;
import committee.nova.mods.avaritia_integration.api.module.InModule;
import committee.nova.mods.avaritia_integration.util.MeteoriteSwordSlashArts;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author: cnlimiter
 */
@InModule(value = "slashblade_reg", dependencies = "slashblade")
@InModule.Subscriber(modBus = true)
public class SlashbladeReg extends AbModule {
    public static final DeferredRegister<SlashArts> REGISTRY = DeferredRegister.create(SlashArts.REGISTRY_KEY, AvaritiaIntegration.MOD_ID);

    public static final RegistryObject<SlashArts> meteoritesword = REGISTRY.register("meteorite_sword",
            () -> new MeteoriteSwordSlashArts((e) -> ComboStateRegistry.NONE.getId()));

    @Override
    protected void register() {
        REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
