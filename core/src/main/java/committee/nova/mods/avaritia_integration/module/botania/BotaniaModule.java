package committee.nova.mods.avaritia_integration.module.botania;

import committee.nova.mods.avaritia_integration.module.ModMeta;
import committee.nova.mods.avaritia_integration.module.Module;
import committee.nova.mods.avaritia_integration.module.ModuleEntry;
import committee.nova.mods.avaritia_integration.module.botania.block.behavor.AlphaSparkBehavior;
import committee.nova.mods.avaritia_integration.module.botania.entity.AlphaSparkEntity;
import committee.nova.mods.avaritia_integration.module.botania.entity.InfinityManaPoolBlockEntity;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationBlockEntities;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationBlocks;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationEntities;
import committee.nova.mods.avaritia_integration.module.botania.registry.BotaniaIntegrationItems;
import committee.nova.mods.avaritia_integration.module.botania.render.AlphaSparkRender;
import committee.nova.mods.avaritia_integration.module.botania.render.InfinityManaPoolBlockEntityRender;
import committee.nova.mods.avaritia_integration.module.botania.render.InfinityTinyPotatoBlockEntityRender;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import vazkii.botania.client.render.block_entity.SpecialFlowerBlockEntityRenderer;

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
        modBus.addListener(BotaniaModule::registerCapabilities);
    }

    public static void addDispenserBehaviours(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior(BotaniaIntegrationItems.ALPHA_SPARK.get(), new AlphaSparkBehavior());
        });
    }


    public static void registerCapabilities(RegisterCapabilitiesEvent e) {
        e.registerBlockEntity(
                BotaniaForgeCapabilities.MANA_RECEIVER,
                BotaniaIntegrationBlockEntities.INFINITY_MANA_POOL.get(),
                (be, direction) -> be
        );
        e.registerBlockEntity(
                BotaniaForgeCapabilities.WANDABLE,
                BotaniaIntegrationBlockEntities.INFINITY_MANA_POOL.get(),
                (be, direction) -> be
        );
        e.registerBlockEntity(
                BotaniaForgeCapabilities.SPARK_ATTACHABLE,
                BotaniaIntegrationBlockEntities.INFINITY_MANA_POOL.get(),
                (be, direction) -> be
        );
    }

    @Override
    public void processClient() {
        EntityRenderers.register(BotaniaIntegrationEntities.ALPHA_SPARK_ENTITIES.get(), AlphaSparkRender::new);
        BlockEntityRenderers.register(BotaniaIntegrationBlockEntities.ASGARD_DANDELION.get(), SpecialFlowerBlockEntityRenderer::new);
        BlockEntityRenderers.register(BotaniaIntegrationBlockEntities.SOARLEANDER.get(), SpecialFlowerBlockEntityRenderer::new);
        BlockEntityRenderers.register(BotaniaIntegrationBlockEntities.INFINITY_TINY_POTATO.get(), InfinityTinyPotatoBlockEntityRender::new);
        BlockEntityRenderers.register(BotaniaIntegrationBlockEntities.INFINITY_MANA_POOL.get(), InfinityManaPoolBlockEntityRender::new);
        ItemBlockRenderTypes.setRenderLayer(BotaniaIntegrationBlocks.ASGARD_DANDELION.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BotaniaIntegrationBlocks.ASGARD_DANDELION_FLOATING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BotaniaIntegrationBlocks.SOARLEANDER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BotaniaIntegrationBlocks.SOARLEANDER_FLOATING.get(), RenderType.cutout());
    }

    @Override
    public void registerClientEvent(IEventBus modBus, IEventBus gameBus) {
        modBus.addListener(BotaniaModule::registerBlockEntityClientCapability);
        modBus.addListener(BotaniaModule::registerEntityClientCapabilities);
    }

    public static void registerBlockEntityClientCapability(RegisterCapabilitiesEvent e) {
        e.registerBlockEntity(
                BotaniaForgeClientCapabilities.BLOCK_WAND_HUD,
                BotaniaIntegrationBlockEntities.ASGARD_DANDELION.get(),
                (be, unused) -> new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>(be)
        );
        e.registerBlockEntity(
                BotaniaForgeClientCapabilities.BLOCK_WAND_HUD,
                BotaniaIntegrationBlockEntities.SOARLEANDER.get(),
                (be, unused) -> new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>(be)
        );
        e.registerBlockEntity(
                BotaniaForgeClientCapabilities.BLOCK_WAND_HUD,
                BotaniaIntegrationBlockEntities.INFINITY_MANA_POOL.get(),
                (be, unused) -> new InfinityManaPoolBlockEntity.WandHud(be)
        );
    }

    private static void registerEntityClientCapabilities(RegisterCapabilitiesEvent e) {
        e.registerEntity(
                BotaniaForgeClientCapabilities.ENTITY_WAND_HUD,
                BotaniaIntegrationEntities.ALPHA_SPARK_ENTITIES.get(),
                (entity, unused) -> new AlphaSparkEntity.WandHud(entity)
        );
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