package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.util.MeteoriteSwordSlashArts;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModSlashArts {
    public static final DeferredRegister<SlashArts> REGISTRY = DeferredRegister.create(SlashArts.REGISTRY_KEY, AvaritiaIntegration.MOD_ID);

    public static final RegistryObject<SlashArts> meteoritesword = REGISTRY.register("meteorite_sword",
            () -> new MeteoriteSwordSlashArts((e) -> ComboStateRegistry.NONE.getId()));
}
