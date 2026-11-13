package committee.nova.mods.avaritia_integration.module.botania;

import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;
import committee.nova.mods.avaritia_integration.module.botania.block.behavor.AlphaSparkBehavior;
import committee.nova.mods.avaritia_integration.module.botania.entity.AlphaSparkEntity;
import committee.nova.mods.avaritia_integration.module.botania.entity.AsgardDandelionBlockEntity;
import committee.nova.mods.avaritia_integration.module.botania.entity.InfinityManaPoolBlockEntity;
import committee.nova.mods.avaritia_integration.module.botania.entity.SoarleanderBlockEntity;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationBlockEntities;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationBlocks;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationEntities;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationItems;
import committee.nova.mods.avaritia_integration.module.botania.render.AlphaSparkRender;
import committee.nova.mods.avaritia_integration.module.botania.render.InfinityManaPoolBlockEntityRenderer;
import committee.nova.mods.avaritia_integration.module.botania.render.InfinityTinyPotatoBlockEntityRender;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.client.render.block_entity.SpecialFlowerBlockEntityRenderer;
import vazkii.botania.common.lib.ResourceLocationHelper;
import vazkii.botania.forge.CapabilityUtil;

@ModuleEntry(id = BotaniaModule.MOD_ID, target = @ModMeta(BotaniaModule.MOD_ID))
public final class BotaniaModule implements Module {
    public static final String MOD_ID = "botania";

    @Override
    public void init(IEventBus registryBus) {
        BotaniaIntegrationBlocks.REGISTRY.register(registryBus);
        BotaniaIntegrationBlockEntities.REGISTRY.register(registryBus);
        BotaniaIntegrationEntities.REGISTRY.register(registryBus);
        BotaniaIntegrationItems.REGISTRY.register(registryBus);
    }

    @Override
    public void process() {
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BotaniaIntegrationBlocks.ASGARD_DANDELION.getId(), BotaniaIntegrationBlocks.POTTED_ASGARD_DANDELION);
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BotaniaIntegrationBlocks.SOARLEANDER.getId(), BotaniaIntegrationBlocks.POTTED_SOARLEANDER);
    }

    @Override
    public void registerEvent(IEventBus modBus, IEventBus gameBus) {
        modBus.addListener(BotaniaModule::addDispenserBehaviours);
        gameBus.addGenericListener(BlockEntity.class, BotaniaModule::attachCommonCapability);
    }

    public static void addDispenserBehaviours(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior(BotaniaIntegrationItems.ALPHA_SPARK.get(), new AlphaSparkBehavior());
        });
    }


    public static void attachCommonCapability(AttachCapabilitiesEvent<BlockEntity> e) {
        BlockEntity be = e.getObject();
        if (be.getType() == BotaniaIntegrationBlockEntities.INFINITY_MANA_POOL.get()) {
            e.addCapability(ResourceLocationHelper.prefix("mana_receiver"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_RECEIVER, (ManaReceiver) be));
            e.addCapability(ResourceLocationHelper.prefix("wandable"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.WANDABLE, (Wandable) be));
            e.addCapability(ResourceLocationHelper.prefix("spark_attachable"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.SPARK_ATTACHABLE, (SparkAttachable) be));
        }
    }

    @Override
    public void processClient() {
        EntityRenderers.register(BotaniaIntegrationEntities.ALPHA_SPARK_ENTITIES.get(), AlphaSparkRender::new);
        BlockEntityRenderers.register(BotaniaIntegrationBlockEntities.INFINITY_MANA_POOL.get(), InfinityManaPoolBlockEntityRenderer::new);
        BlockEntityRenderers.register(BotaniaIntegrationBlockEntities.ASGARD_DANDELION.get(), SpecialFlowerBlockEntityRenderer::new);
        BlockEntityRenderers.register(BotaniaIntegrationBlockEntities.SOARLEANDER.get(), SpecialFlowerBlockEntityRenderer::new);
        BlockEntityRenderers.register(BotaniaIntegrationBlockEntities.INFINITY_TINY_POTATO.get(), InfinityTinyPotatoBlockEntityRender::new);
        ItemBlockRenderTypes.setRenderLayer(BotaniaIntegrationBlocks.ASGARD_DANDELION.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BotaniaIntegrationBlocks.ASGARD_DANDELION_FLOATING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BotaniaIntegrationBlocks.SOARLEANDER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BotaniaIntegrationBlocks.SOARLEANDER_FLOATING.get(), RenderType.cutout());
    }

    @Override
    public void registerClientEvent(IEventBus modBus, IEventBus gameBus) {
        gameBus.addGenericListener(BlockEntity.class, BotaniaModule::attachBlockEntityClientCapability);
        gameBus.addGenericListener(Entity.class, BotaniaModule::attachEntityClientCapabilities);
    }

    public static void attachBlockEntityClientCapability(AttachCapabilitiesEvent<BlockEntity> e) {
        BlockEntity be = e.getObject();
        if (be instanceof AsgardDandelionBlockEntity tile)
            e.addCapability(ResourceLocationHelper.prefix("wand_hud"), CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>(tile)));
        if (be instanceof SoarleanderBlockEntity tile)
            e.addCapability(ResourceLocationHelper.prefix("wand_hud"), CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>(tile)));
        if (be instanceof InfinityManaPoolBlockEntity tile)
            e.addCapability(ResourceLocationHelper.prefix("wand_hud"), CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, new InfinityManaPoolBlockEntity.WandHud(tile)));
    }

    private static void attachEntityClientCapabilities(AttachCapabilitiesEvent<Entity> e) {
        var entity = e.getObject();
        if (entity instanceof AlphaSparkEntity alphaSparkEntity)
            e.addCapability(ResourceLocationHelper.prefix("wand_hud"), CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, new AlphaSparkEntity.WandHud(alphaSparkEntity)));
    }

    @Override
    public void collectCreativeTabItems(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(BotaniaIntegrationBlocks.ASGARD_DANDELION.get());
        output.accept(BotaniaIntegrationBlocks.ASGARD_DANDELION_FLOATING.get());

        output.accept(BotaniaIntegrationBlocks.SOARLEANDER.get());
        output.accept(BotaniaIntegrationBlocks.SOARLEANDER_FLOATING.get());

        output.accept(BotaniaIntegrationBlocks.INFINITY_MANA_POOL.get());
        output.accept(BotaniaIntegrationBlocks.INFINITY_POTATO.get());
        output.accept(BotaniaIntegrationItems.ALPHA_SPARK.get());
    }
}
