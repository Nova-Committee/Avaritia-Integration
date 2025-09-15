package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia.api.common.block.BaseBlock;
import committee.nova.mods.avaritia_integration.api.event.ClientInitEvent;
import committee.nova.mods.avaritia_integration.api.event.InitEvent;
import committee.nova.mods.avaritia_integration.api.module.AbModule;
import committee.nova.mods.avaritia_integration.api.module.InModule;
import committee.nova.mods.avaritia_integration.client.render.InfinityManaPoolRender;
import committee.nova.mods.avaritia_integration.client.render.InfinityTinyPotatoBlockEntityRender;
import committee.nova.mods.avaritia_integration.common.block.AsgardDandelionBlock;
import committee.nova.mods.avaritia_integration.common.block.InfinityManaPoolBlock;
import committee.nova.mods.avaritia_integration.common.block.InfinityTinyPotatoBlock;
import committee.nova.mods.avaritia_integration.common.block.SoarleanderBlock;
import committee.nova.mods.avaritia_integration.common.blockentity.AsgardDandelionBlockEntity;
import committee.nova.mods.avaritia_integration.common.blockentity.InfinityManaPoolBlockEntity;
import committee.nova.mods.avaritia_integration.common.blockentity.InfinityTinyPotatoBlockEntity;
import committee.nova.mods.avaritia_integration.common.blockentity.SoarleanderBlockEntity;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.client.render.block_entity.SpecialFlowerBlockEntityRenderer;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.FloatingSpecialFlowerBlock;
import vazkii.botania.common.lib.ResourceLocationHelper;
import vazkii.botania.forge.CapabilityUtil;
import vazkii.botania.xplat.XplatAbstractions;

import static committee.nova.mods.avaritia_integration.init.registry.Registries.BLOCK_ENTITIES;

/**
 * @author: cnlimiter
 */
@InModule(value = "botania_reg", dependencies = "botania")
@InModule.Subscriber()
public class BotaniaReg extends AbModule {

    public static final BlockBehaviour.Properties ASGARD_FLOWER_PROPS = BlockBehaviour.Properties.copy(Blocks.POPPY).lightLevel(level -> 15);
    public static final BlockBehaviour.Properties SOARLEANDER_FLOWER_PROPS = BlockBehaviour.Properties.copy(Blocks.POPPY).lightLevel(level -> 5);

    public static final BlockEntityType<AsgardDandelionBlockEntity> ASGARD = XplatAbstractions.INSTANCE.createBlockEntityType(AsgardDandelionBlockEntity::new);
    public static final BlockEntityType<SoarleanderBlockEntity> SOARLEANDER = XplatAbstractions.INSTANCE.createBlockEntityType(SoarleanderBlockEntity::new);


    public static final RegistryObject<Block> asgard_dandelion = Registries.itemBlock("asgard_dandelion",
            () -> ModList.get().isLoaded("botania") ? new AsgardDandelionBlock(MobEffects.HUNGER, 0, ASGARD_FLOWER_PROPS, () -> ASGARD) : new BaseBlock(BlockBehaviour.Properties.of()),
            true, ModList.get().isLoaded("botania")
    );

    public static final RegistryObject<Block> asgard_dandelion_floating =  Registries.itemBlock("asgard_dandelion_floating",
            () -> ModList.get().isLoaded("botania") ? new FloatingSpecialFlowerBlock(BotaniaBlocks.FLOATING_PROPS, () -> ASGARD) : new BaseBlock(BlockBehaviour.Properties.of()),
            true, ModList.get().isLoaded("botania")
    );

    public static final RegistryObject<Block> potted_asgard_dandelion =  Registries.itemBlock("potted_asgard_dandelion",
            () -> ModList.get().isLoaded("botania") ?
                    new FlowerPotBlock(()-> ((FlowerPotBlock)Blocks.FLOWER_POT),asgard_dandelion,BlockBehaviour.Properties.copy(Blocks.POTTED_DANDELION).noOcclusion().lightLevel(level -> 15))
                    : new BaseBlock(BlockBehaviour.Properties.of()),
            true, ModList.get().isLoaded("botania")
    );

    public static final RegistryObject<Block> soarleander =  Registries.itemBlock("soarleander",
            () -> ModList.get().isLoaded("botania") ?
                    new SoarleanderBlock(MobEffects.WITHER, 1, SOARLEANDER_FLOWER_PROPS, () -> SOARLEANDER)
                    : new BaseBlock(BlockBehaviour.Properties.of()),
            true, ModList.get().isLoaded("botania")
    );

    public static final RegistryObject<Block> soarleander_floating =  Registries.itemBlock("soarleander_floating",
            () -> ModList.get().isLoaded("botania") ?
                    new FloatingSpecialFlowerBlock(BotaniaBlocks.FLOATING_PROPS, () -> SOARLEANDER)
                    : new BaseBlock(BlockBehaviour.Properties.of()),
            true, ModList.get().isLoaded("botania")
    );

    public static final RegistryObject<Block> potted_soarleander =  Registries.itemBlock("potted_soarleander",
            () -> ModList.get().isLoaded("botania") ?
                    new FlowerPotBlock(()-> ((FlowerPotBlock)Blocks.FLOWER_POT),soarleander,BlockBehaviour.Properties.copy(Blocks.POTTED_DANDELION).noOcclusion().lightLevel(level -> 5))
                    : new BaseBlock(BlockBehaviour.Properties.of()),
            true, ModList.get().isLoaded("botania")
    );

    public static final RegistryObject<Block> infinity_mana_pool =  Registries.itemBlock("infinity_mana_pool",
            () -> ModList.get().isLoaded("botania") ?
                    new InfinityManaPoolBlock(InfinityManaPoolBlock.Variant.CREATIVE, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK))
                    : new BaseBlock(BlockBehaviour.Properties.of()),
            true, ModList.get().isLoaded("botania")
    );

    public static final RegistryObject<Block> infinity_potato =  Registries.itemBlock("infinity_potato",
            () -> ModList.get().isLoaded("botania") ?
                    new InfinityTinyPotatoBlock()
                    : new BaseBlock(BlockBehaviour.Properties.of()),
            true, ModList.get().isLoaded("botania")
    );

    public static final RegistryObject<BlockEntityType<AsgardDandelionBlockEntity>> ASGARD_DANDELION = BLOCK_ENTITIES.register(
            "asgard_dandelion_be",
            () -> BlockEntityType.Builder.of(
                    AsgardDandelionBlockEntity::new,
                    asgard_dandelion.get(),asgard_dandelion_floating.get()
            ).build(null)
    );
    public static final RegistryObject<BlockEntityType<SoarleanderBlockEntity>> SOARLEANDER_BLOCK_ENTITIES = BLOCK_ENTITIES.register(
            "soarleander_be",
            () -> BlockEntityType.Builder.of(
                    SoarleanderBlockEntity::new,
                    soarleander.get(),soarleander_floating.get()
            ).build(null)
    );
    public static final RegistryObject<BlockEntityType<InfinityManaPoolBlockEntity>> INFINITY_MANA_POOL = BLOCK_ENTITIES.register(
            "infinity_mana_pool",
            () -> BlockEntityType.Builder.of(
                    InfinityManaPoolBlockEntity::new,
                    infinity_mana_pool.get()
            ).build(null)
    );
    public static final RegistryObject<BlockEntityType<InfinityTinyPotatoBlockEntity>> INFINITY_TINY_POTATO = BLOCK_ENTITIES.register(
            "infinity_tiny_potato",
            () -> BlockEntityType.Builder.of(
                    InfinityTinyPotatoBlockEntity::new,
                    infinity_potato.get()
            ).build(null)
    );

    @Override
    protected void init(InitEvent event) {
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addGenericListener(BlockEntity.class, this::attachCommonCap);
        event.enqueueWork(() ->{
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BotaniaReg.asgard_dandelion.getId(),BotaniaReg.potted_asgard_dandelion);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BotaniaReg.soarleander.getId(),BotaniaReg.potted_soarleander);
        });
    }

    @Override
    protected void clientInit(ClientInitEvent event) {
        BlockEntityRenderers.register(INFINITY_MANA_POOL.get(), InfinityManaPoolRender::new);
        BlockEntityRenderers.register(ASGARD_DANDELION.get(), SpecialFlowerBlockEntityRenderer::new);
        BlockEntityRenderers.register(SOARLEANDER_BLOCK_ENTITIES.get(), SpecialFlowerBlockEntityRenderer::new);
        BlockEntityRenderers.register(INFINITY_TINY_POTATO.get(), InfinityTinyPotatoBlockEntityRender::new);
        ItemBlockRenderTypes.setRenderLayer(asgard_dandelion.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(asgard_dandelion_floating.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(soarleander.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(soarleander_floating.get(), RenderType.cutout());
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addGenericListener(BlockEntity.class, this::attachClientCap);
    }

    public void attachClientCap(AttachCapabilitiesEvent<BlockEntity> e) {
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

    public void attachCommonCap(AttachCapabilitiesEvent<BlockEntity> e) {
        BlockEntity be = e.getObject();
        if (be instanceof InfinityManaPoolBlockEntity) {
            e.addCapability(ResourceLocationHelper.prefix("mana_receiver"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_RECEIVER, (ManaReceiver) be));
            e.addCapability(ResourceLocationHelper.prefix("wandable"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.WANDABLE, (Wandable) be));
        }
    }
}
