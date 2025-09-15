package committee.nova.mods.avaritia_integration.module;

import committee.nova.mods.avaritia_integration.init.registry.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public final class Shared {
    public static <T extends Item> void appendItemToGroup(RegistryObject<T> item) {
        Registries.ACCEPT_ITEM.add(item);
    }
}
