package committee.nova.mods.avaritia_integration.init.handler;

import committee.nova.mods.avaritia_integration.init.registry.BotaniaReg;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.lib.ResourceLocationHelper;
import vazkii.botania.forge.CapabilityUtil;

/**
 * @author: cnlimiter
 */
@Mod.EventBusSubscriber
public class CapHandler {
    @SubscribeEvent
    public static void attachBeCaps(AttachCapabilitiesEvent<BlockEntity> e) {
        BlockEntity be = e.getObject();

        if (be.getType() == BotaniaReg.INFINITY_MANA_POOL.get())  {
            e.addCapability(ResourceLocationHelper.prefix("mana_receiver"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_RECEIVER, (ManaReceiver)be));
            e.addCapability(ResourceLocationHelper.prefix("wandable"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.WANDABLE, (Wandable)be));
        }

    }
}
