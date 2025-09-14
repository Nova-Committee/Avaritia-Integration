//package committee.nova.mods.avaritia_integration.init.registry;
//
//import committee.nova.mods.avaritia_integration.common.block.AsgardDandelionBlock;
//import committee.nova.mods.avaritia_integration.common.block.InfinityManaPoolBlock;
//import committee.nova.mods.avaritia_integration.common.block.InfinityTinyPotatoBlock;
//import committee.nova.mods.avaritia_integration.common.block.SoarleanderBlock;
//import committee.nova.mods.avaritia_integration.common.blockentity.AsgardDandelionBlockEntity;
//import committee.nova.mods.avaritia_integration.common.blockentity.InfinityManaPoolBlockEntity;
//import committee.nova.mods.avaritia_integration.common.blockentity.InfinityTinyPotatoBlockEntity;
//import committee.nova.mods.avaritia_integration.common.blockentity.SoarleanderBlockEntity;
//import net.minecraft.world.effect.MobEffects;
//import net.minecraft.world.item.BlockItem;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.FlowerPotBlock;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockBehaviour;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.ModList;
//import net.minecraftforge.registries.ForgeRegistries;
//import net.minecraftforge.registries.RegisterEvent;
//import vazkii.botania.common.block.BotaniaBlocks;
//import vazkii.botania.common.block.FloatingSpecialFlowerBlock;
//import vazkii.botania.xplat.XplatAbstractions;
//
///**
// * @author: cnlimiter
// */
//public class BotaniaRegistries {
//    public static Block asgard_dandelion;
//    public static Block asgard_dandelion_floating;
//    public static Block potted_asgard_dandelion;
//    public static Block soarleander;
//    public static Block soarleander_floating;
//    public static Block potted_soarleander;
//    public static Block infinity_mana_pool;
//    public static Block infinity_potato;
//
//    public static BlockEntityType<?> ASGARD_DANDELION;
//    public static BlockEntityType<?> SOARLEANDER;
//    public static BlockEntityType<?> INFINITY_MANA_POOL;
//    public static BlockEntityType<?> INFINITY_TINY_POTATO;
//
//    @SubscribeEvent
//    public static void register(RegisterEvent event) {
//        if (ModList.get().isLoaded("botania")) {
//            BlockBehaviour.Properties ASGARD_FLOWER_PROPS = BlockBehaviour.Properties.copy(Blocks.POPPY).lightLevel(level -> 15);
//            BlockBehaviour.Properties SOARLEANDER_FLOWER_PROPS = BlockBehaviour.Properties.copy(Blocks.POPPY).lightLevel(level -> 5);
//
//            BlockEntityType<AsgardDandelionBlockEntity> ASGARD = XplatAbstractions.INSTANCE.createBlockEntityType(AsgardDandelionBlockEntity::new);
//            BlockEntityType<SoarleanderBlockEntity> SOARLEANDER_X = XplatAbstractions.INSTANCE.createBlockEntityType(SoarleanderBlockEntity::new);
//
//            asgard_dandelion = new AsgardDandelionBlock(MobEffects.HUNGER, 0, ASGARD_FLOWER_PROPS, () -> ASGARD);
//            asgard_dandelion_floating = new FloatingSpecialFlowerBlock(BotaniaBlocks.FLOATING_PROPS, () -> ASGARD);
//            potted_asgard_dandelion = new FlowerPotBlock(()-> ((FlowerPotBlock)Blocks.FLOWER_POT),ModBlocks.asgard_dandelion,BlockBehaviour.Properties.copy(Blocks.POTTED_DANDELION).noOcclusion().lightLevel(level -> 15));
//            soarleander = new SoarleanderBlock(MobEffects.WITHER, 1, SOARLEANDER_FLOWER_PROPS, () -> SOARLEANDER_X);
//            soarleander_floating = new FloatingSpecialFlowerBlock(BotaniaBlocks.FLOATING_PROPS, () -> SOARLEANDER_X);
//            potted_soarleander = new FlowerPotBlock(()-> ((FlowerPotBlock)Blocks.FLOWER_POT),ModBlocks.soarleander,BlockBehaviour.Properties.copy(Blocks.POTTED_DANDELION).noOcclusion().lightLevel(level -> 5));
//            infinity_mana_pool = new InfinityManaPoolBlock(InfinityManaPoolBlock.Variant.CREATIVE, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK));
//            infinity_potato = new InfinityTinyPotatoBlock();
//            event.register(ForgeRegistries.Keys.BLOCKS, helper -> {
//                helper.register("asgard_dandelion", asgard_dandelion);
//                helper.register("asgard_dandelion_floating", asgard_dandelion_floating);
//                helper.register("potted_asgard_dandelion", potted_asgard_dandelion);
//                helper.register("soarleander", soarleander);
//                helper.register("soarleander_floating", soarleander_floating);
//                helper.register("potted_soarleander", potted_soarleander);
//                helper.register("infinity_mana_pool", infinity_mana_pool);
//                helper.register("infinity_potato", infinity_potato);
//            });
//
//            event.register(ForgeRegistries.Keys.ITEMS, helper -> {
//                helper.register("asgard_dandelion", new BlockItem(asgard_dandelion, new Item.Properties()));
//                helper.register("asgard_dandelion_floating", new BlockItem(asgard_dandelion_floating, new Item.Properties()));
//                helper.register("potted_asgard_dandelion", new BlockItem(potted_asgard_dandelion, new Item.Properties()));
//                helper.register("soarleander", new BlockItem(soarleander, new Item.Properties()));
//                helper.register("soarleander_floating", new BlockItem(soarleander_floating, new Item.Properties()));
//                helper.register("potted_soarleander", new BlockItem(potted_soarleander, new Item.Properties()));
//                helper.register("infinity_mana_pool", new BlockItem(infinity_mana_pool, new Item.Properties()));
//                helper.register("infinity_potato", new BlockItem(infinity_potato, new Item.Properties()));
//            });
//            ASGARD_DANDELION = BlockEntityType.Builder.of(
//                    AsgardDandelionBlockEntity::new,
//                    ModBlocks.asgard_dandelion.get(),ModBlocks.asgard_dandelion_floating.get()
//            ).build(null);
//            SOARLEANDER = BlockEntityType.Builder.of(
//                    SoarleanderBlockEntity::new,
//                    ModBlocks.soarleander.get(),ModBlocks.soarleander_floating.get()
//            ).build(null);
//            INFINITY_MANA_POOL = BlockEntityType.Builder.of(
//                    InfinityManaPoolBlockEntity::new,
//                    ModBlocks.infinity_mana_pool.get()
//            ).build(null);
//            INFINITY_TINY_POTATO = BlockEntityType.Builder.of(
//                    InfinityTinyPotatoBlockEntity::new,
//                    ModBlocks.infinity_potato.get()
//            ).build(null);
//            event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, helper -> {
//                helper.register("asgard_dandelion_be", ASGARD_DANDELION);
//                helper.register("soarleander_be", SOARLEANDER);
//                helper.register("infinity_mana_pool", INFINITY_MANA_POOL);
//                helper.register("infinity_tiny_potato", INFINITY_TINY_POTATO);
//            });
//        }
//    }
//}
