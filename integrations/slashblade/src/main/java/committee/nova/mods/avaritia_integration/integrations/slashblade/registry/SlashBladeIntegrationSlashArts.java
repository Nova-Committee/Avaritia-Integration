package committee.nova.mods.avaritia_integration.integrations.slashblade.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.integrations.slashblade.slasharts.MeteoriteSwordSlashArts;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.slasharts.SlashArts;

import java.util.function.Supplier;

public final class SlashBladeIntegrationSlashArts {

    public static final DeferredRegister<SlashArts> REGISTRY = DeferredRegister.create(SlashArts.REGISTRY_KEY,
            AvaritiaIntegration.MOD_ID);

    public static final DeferredHolder<SlashArts, SlashArts> METEORITE_SWORD = register("meteorite_sword",
            () -> new MeteoriteSwordSlashArts(entity -> ComboStateRegistry.NONE.getId()));

    private SlashBladeIntegrationSlashArts() {}

    private static <T extends SlashArts> DeferredHolder<SlashArts, T> register(String id, Supplier<T> obj) {
        return REGISTRY.register(id, obj);
    }
}
