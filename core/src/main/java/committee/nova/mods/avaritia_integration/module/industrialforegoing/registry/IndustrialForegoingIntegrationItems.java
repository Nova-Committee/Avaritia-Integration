package committee.nova.mods.avaritia_integration.module.industrialforegoing.registry;

import com.buuz135.industrial.module.ModuleCore;
import committee.nova.mods.avaritia.init.registry.ModItems;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.init.registry.AIItems;
import committee.nova.mods.avaritia_integration.module.industrialforegoing.item.AddonInfo;
import committee.nova.mods.avaritia_integration.module.industrialforegoing.item.AddonItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.function.Supplier;

public class IndustrialForegoingIntegrationItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AvaritiaIntegration.MOD_ID);

    public static final HashMap<String, Supplier<? extends AddonItem>> ADDONS = registryAddons();

    private static HashMap<String,Supplier<? extends AddonItem>> registryAddons(){
        HashMap<String,Supplier<? extends AddonItem>> map = new HashMap<>();
        AddonInfo.create(3,AIItems.BLAZE_CUBE_GEAR,() -> ModuleCore.ETHER.getSourceFluid().get(),1000,100,"blaze_cube").registry(map,ITEMS);
        AddonInfo.create(5,AIItems.CRYSTAL_MATRIX_GEAR,IndustrialForegoingIntegrationFluids.ELDERLY_MEDULLA.getSourceFluid(),1000,200,"crystal_matrix").registry(map,ITEMS);
        AddonInfo.create(8,ModItems.neutron_gear,IndustrialForegoingIntegrationFluids.VOID_MATTER.getSourceFluid(),1000,300,"neutron").registry(map,ITEMS);
        AddonInfo.create(12,AIItems.INFINITY_GEAR,IndustrialForegoingIntegrationFluids.VOID_MATTER.getSourceFluid(),2000,400,"infinity").registry(map,ITEMS);
        return map;
    }
    public static <T extends Item> DeferredItem<T> register(String id, Supplier<T> obj) {
        return ITEMS.register(id, obj);
    }
}
