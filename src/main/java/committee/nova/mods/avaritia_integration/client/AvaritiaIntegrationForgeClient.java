package committee.nova.mods.avaritia_integration.client;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AvaritiaIntegration.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AvaritiaIntegrationForgeClient {
}
