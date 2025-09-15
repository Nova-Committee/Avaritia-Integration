package committee.nova.mods.avaritia_integration.module.botania;

import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationBlockEntities;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationBlocks;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationItems;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.common.lib.ResourceLocationHelper;
import vazkii.botania.forge.CapabilityUtil;

@ModuleEntry(id = BotaniaModule.MOD_ID, target = @ModMeta(BotaniaModule.MOD_ID))
public final class BotaniaModule implements Module {
    public static final String MOD_ID = "botania";

    @Override
    public void init(IEventBus registryBus) {
        BotaniaIntegrationBlocks.REGISTRY.register(registryBus);
        BotaniaIntegrationBlockEntities.REGISTRY.register(registryBus);
        BotaniaIntegrationItems.REGISTRY.register(registryBus);
    }

    @Override
    public void process() {
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BotaniaIntegrationBlocks.ASGARD_DANDELION.getId(), BotaniaIntegrationBlocks.POTTED_ASGARD_DANDELION);
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BotaniaIntegrationBlocks.SOARLEANDER.getId(), BotaniaIntegrationBlocks.POTTED_SOARLEANDER);
    }

    @Override
    public void registerEvent(IEventBus modBus, IEventBus gameBus) {
        gameBus.register(this);
    }

    @SubscribeEvent
    public static void attachBeCaps(AttachCapabilitiesEvent<BlockEntity> e) {
        BlockEntity be = e.getObject();
        if (be.getType() == BotaniaIntegrationBlockEntities.INFINITY_MANA_POOL.get()) {
            e.addCapability(ResourceLocationHelper.prefix("mana_receiver"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_RECEIVER, (ManaReceiver) be));
            e.addCapability(ResourceLocationHelper.prefix("wandable"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.WANDABLE, (Wandable) be));
        }
    }
}
