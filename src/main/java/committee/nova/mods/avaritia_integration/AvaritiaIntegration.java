package committee.nova.mods.avaritia_integration;

import com.mojang.logging.LogUtils;
import committee.nova.mods.avaritia_integration.init.registry.Registries;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(AvaritiaIntegration.MOD_ID)
public class AvaritiaIntegration
{

    public static final String MOD_ID = "avaritia_integration";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AvaritiaIntegration()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        Registries.BLOCKS.register(modEventBus);
        Registries.ITEMS.register(modEventBus);
        Registries.BLOCK_ENTITIES.register(modEventBus);
        Registries.CREATIVE_TABS.register(modEventBus);
    }



    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }
}
