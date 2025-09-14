package committee.nova.mods.avaritia_integration;

import com.iafenvoy.integration.IntegrationExecutor;
import com.mojang.logging.LogUtils;
import committee.nova.mods.avaritia_integration.init.registry.BotaniaReg;
import committee.nova.mods.avaritia_integration.init.registry.Registries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
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
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        Registries.ITEMS.register(modEventBus);
        Registries.BLOCKS.register(modEventBus);
        Registries.BLOCK_ENTITIES.register(modEventBus);
        Registries.CREATIVE_TABS.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        IntegrationExecutor.runWhenLoad("botania",()->()-> {
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BotaniaReg.asgard_dandelion.getId(),BotaniaReg.potted_asgard_dandelion);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BotaniaReg.soarleander.getId(),BotaniaReg.potted_soarleander);
        });
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }
}
