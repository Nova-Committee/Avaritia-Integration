package committee.nova.mods.avaritia_integration.client;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.common.blockentity.AsgardDandelionBlockEntity;
import committee.nova.mods.avaritia_integration.common.blockentity.InfinityManaPoolBlockEntity;
import committee.nova.mods.avaritia_integration.common.blockentity.SoarleanderBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import vazkii.botania.common.lib.ResourceLocationHelper;
import vazkii.botania.forge.CapabilityUtil;


@Mod.EventBusSubscriber(modid = AvaritiaIntegration.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AvaritiaIntegrationForgeClient {

    public AvaritiaIntegrationForgeClient(){

    }

    @SubscribeEvent
    public static void registerLoaders(ModelEvent.RegisterGeometryLoaders event) {
    }
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addGenericListener(BlockEntity.class, AvaritiaIntegrationForgeClient::attachBeCapabilities);
    }
    public static void attachBeCapabilities(AttachCapabilitiesEvent<BlockEntity> e) {
        BlockEntity be = e.getObject();
        if (be instanceof AsgardDandelionBlockEntity tile) {
            e.addCapability(ResourceLocationHelper.prefix("wand_hud"),
                    CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud(tile))
            );
        }
        if (be instanceof SoarleanderBlockEntity tile) {
            e.addCapability(ResourceLocationHelper.prefix("wand_hud"),
                    CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud(tile))
            );
        }
        if (be instanceof InfinityManaPoolBlockEntity tile) {
            e.addCapability(ResourceLocationHelper.prefix("wand_hud"),
                    CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, new InfinityManaPoolBlockEntity.WandHud(tile))
            );
        }
    }


}
