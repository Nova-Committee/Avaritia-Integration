package committee.nova.mods.avaritia_integration.module.slashblade.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.slashblade.slasharts.MeteoriteSwordSlashArts;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.slasharts.SlashArts;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SlashBladeIntegrationSlashArts {
    public static final DeferredRegister<SlashArts> REGISTRY = DeferredRegister.create(SlashArts.REGISTRY_KEY, AvaritiaIntegration.MOD_ID);

    public static final RegistryObject<SlashArts> METEORITE_SWORD = register("meteorite_sword", () -> new MeteoriteSwordSlashArts((e) -> ComboStateRegistry.NONE.getId()));

    private static <T extends SlashArts> RegistryObject<T> register(String id, Supplier<T> obj) {
        return REGISTRY.register(id, obj);
    }
}
