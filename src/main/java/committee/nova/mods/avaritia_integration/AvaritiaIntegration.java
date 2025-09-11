package committee.nova.mods.avaritia_integration;

import com.mojang.logging.LogUtils;
import committee.nova.mods.avaritia_integration.init.registry.ModBlocks;
import committee.nova.mods.avaritia_integration.init.registry.ModCreativeModeTabs;
import committee.nova.mods.avaritia_integration.init.registry.ModItems;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(AvaritiaIntegration.MOD_ID)
public class AvaritiaIntegration
{

    public static final String MOD_ID = "avaritia_integration";

    private static final Logger LOGGER = LogUtils.getLogger();

    public AvaritiaIntegration()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModCreativeModeTabs.CREATIVE_TABS.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() ->{
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.asgard_dandelion.getId(),ModBlocks.potted_asgard_dandelion);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.soarleander.getId(),ModBlocks.potted_soarleander);
        });
    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            
        }
    }
}
