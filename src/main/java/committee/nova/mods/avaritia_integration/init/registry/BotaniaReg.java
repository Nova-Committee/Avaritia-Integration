package committee.nova.mods.avaritia_integration.init.registry;

import com.iafenvoy.integration.entrypoint.EntryPointProvider;
import com.iafenvoy.integration.entrypoint.IntegrationEntryPoint;
import committee.nova.mods.avaritia.api.common.block.BaseBlock;
import committee.nova.mods.avaritia_integration.common.block.AsgardDandelionBlock;
import committee.nova.mods.avaritia_integration.common.block.InfinityManaPoolBlock;
import committee.nova.mods.avaritia_integration.common.block.InfinityTinyPotatoBlock;
import committee.nova.mods.avaritia_integration.common.block.SoarleanderBlock;
import committee.nova.mods.avaritia_integration.common.blockentity.AsgardDandelionBlockEntity;
import committee.nova.mods.avaritia_integration.common.blockentity.InfinityManaPoolBlockEntity;
import committee.nova.mods.avaritia_integration.common.blockentity.InfinityTinyPotatoBlockEntity;
import committee.nova.mods.avaritia_integration.common.blockentity.SoarleanderBlockEntity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.FloatingSpecialFlowerBlock;
import vazkii.botania.xplat.XplatAbstractions;

import static committee.nova.mods.avaritia_integration.init.registry.Registries.BLOCK_ENTITIES;

/**
 * @author: cnlimiter
 */
@EntryPointProvider(slug = "botania")
public class BotaniaReg implements IntegrationEntryPoint {
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
//    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
//    public static class Bus {
//        @SubscribeEvent
//        public static void commonSetup(final FMLCommonSetupEvent event)
//        {
//            IntegrationExecutor.runWhenLoad("botania",()->()-> {
////                ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BotaniaReg.asgard_dandelion.getId(),BotaniaReg.potted_asgard_dandelion);
////                ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BotaniaReg.soarleander.getId(),BotaniaReg.potted_soarleander);
//            });
//        }
//    }
}
