package committee.nova.mods.avaritia_integration.module.bloodmagic.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.bloodmagic.item.BloodOrbOfArmokItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.common.item.BloodOrb;

import java.util.function.Supplier;

public final class BloodMagicIntegrationItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, AvaritiaIntegration.MOD_ID);

    public static final RegistryObject<Item> BLOOD_ORB_OF_ARMOK = register("blood_orb_of_armok", () -> new BloodOrbOfArmokItem(() -> new BloodOrb(new ResourceLocation(AvaritiaIntegration.MOD_ID), 1, 1000000000, 10)));

    public static <T extends Item> RegistryObject<T> register(String id, Supplier<T> obj) {
        return REGISTRY.register(id, obj);
    }
}
