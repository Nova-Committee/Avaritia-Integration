package committee.nova.mods.avaritia_integration;

import com.mojang.logging.LogUtils;
import committee.nova.mods.avaritia_integration.init.registry.AICreativeTabs;
import committee.nova.mods.avaritia_integration.init.registry.AIItems;
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
        AIItems.REGISTRY.register(bus);
        ModuleManager.loadModules(bus);
    }
}
