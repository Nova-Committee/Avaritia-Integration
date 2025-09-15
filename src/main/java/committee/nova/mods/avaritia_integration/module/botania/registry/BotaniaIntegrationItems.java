package committee.nova.mods.avaritia_integration.module.botania.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.Shared;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class BotaniaIntegrationItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, AvaritiaIntegration.MOD_ID);

    public static <T extends Item> RegistryObject<T> register(String id, Supplier<T> obj) {
        RegistryObject<T> r = REGISTRY.register(id, obj);
        Shared.appendItemToGroup(r);
        return r;
    }
}
