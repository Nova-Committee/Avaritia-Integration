package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.api.module.AbModule;
import committee.nova.module.InModule;
import committee.nova.mods.avaritia_integration.common.item.BloodOrbOfArmokItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import wayoftime.bloodmagic.common.item.BloodOrb;

import static committee.nova.mods.avaritia_integration.init.registry.Registries.item;

/**
 * @author: cnlimiter
 */
@InModule(value = "bloodmagic_reg", dependencies = "bloodmagic")
public class BloodMagicReg extends AbModule {
    public static final RegistryObject<Item> blood_orb_of_armok = item("blood_orb_of_armok", () -> new BloodOrbOfArmokItem(() -> new BloodOrb(new ResourceLocation(AvaritiaIntegration.MOD_ID), 1,1000000000, 10)));

}
