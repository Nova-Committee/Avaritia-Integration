package committee.nova.mods.avaritia_integration.module.slashblade.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.slashblade.item.StredgeuniverseItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static committee.nova.mods.avaritia_integration.init.registry.Registries.item;

@SuppressWarnings("unused")
public final class SlashBladeIntegrationItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, AvaritiaIntegration.MOD_ID);

    public static final RegistryObject<Item> STREDGEUNIVERSE = item("stredgeuniverse", StredgeuniverseItem::new);

    public static <T extends Item> RegistryObject<T> register(String id, Supplier<T> obj) {
        return REGISTRY.register(id, obj);
    }
}
