package committee.nova.mods.avaritia_integration;

import com.mojang.logging.LogUtils;
import committee.nova.mods.avaritia_integration.init.registry.AICreativeTabs;
import committee.nova.mods.avaritia_integration.init.registry.ModItems;
import committee.nova.mods.avaritia_integration.init.registry.Registries;
import committee.nova.mods.avaritia_integration.module.ModuleManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(AvaritiaIntegration.MOD_ID)
public class AvaritiaIntegration {
    public static final String MOD_ID = "avaritia_integration";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AvaritiaIntegration() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        AICreativeTabs.REGISTRY.register(bus);

        ModItems.blaze_cube_bolt.getId();
        Registries.ITEMS.register(bus);
        Registries.BLOCKS.register(bus);
        Registries.BLOCK_ENTITIES.register(bus);
        Registries.CREATIVE_TABS.register(bus);

        ModuleManager.loadModules(bus);
    }
}
