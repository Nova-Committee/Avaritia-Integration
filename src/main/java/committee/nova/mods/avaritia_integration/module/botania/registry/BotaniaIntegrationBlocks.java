package committee.nova.mods.avaritia_integration.module.botania.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.botania.item.block.AsgardDandelionBlock;
import committee.nova.mods.avaritia_integration.module.botania.item.block.InfinityManaPoolBlock;
import committee.nova.mods.avaritia_integration.module.botania.item.block.InfinityTinyPotatoBlock;
import committee.nova.mods.avaritia_integration.module.botania.item.block.SoarleanderBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.FloatingSpecialFlowerBlock;

import java.util.function.Function;
import java.util.function.Supplier;

public final class BotaniaIntegrationBlocks {
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(Registries.BLOCK, AvaritiaIntegration.MOD_ID);

    public static final BlockBehaviour.Properties ASGARD_FLOWER_PROPS = BlockBehaviour.Properties.copy(Blocks.POPPY).lightLevel(level -> 15);
    public static final BlockBehaviour.Properties SOARLEANDER_FLOWER_PROPS = BlockBehaviour.Properties.copy(Blocks.POPPY).lightLevel(level -> 5);

    public static final RegistryObject<Block> ASGARD_DANDELION = register("asgard_dandelion", () -> new AsgardDandelionBlock(MobEffects.HUNGER, 0, ASGARD_FLOWER_PROPS, () -> BotaniaIntegrationBlockEntities.ASGARD));
    public static final RegistryObject<Block> ASGARD_DANDELION_FLOATING = register("asgard_dandelion_floating", () -> new FloatingSpecialFlowerBlock(BotaniaBlocks.FLOATING_PROPS, () -> BotaniaIntegrationBlockEntities.ASGARD));
    public static final RegistryObject<Block> POTTED_ASGARD_DANDELION = register("potted_asgard_dandelion", () -> new FlowerPotBlock(() -> ((FlowerPotBlock) Blocks.FLOWER_POT), ASGARD_DANDELION, BlockBehaviour.Properties.copy(Blocks.POTTED_DANDELION).noOcclusion().lightLevel(level -> 15)));
    public static final RegistryObject<Block> SOARLEANDER = register("soarleander", () -> new SoarleanderBlock(MobEffects.WITHER, 1, SOARLEANDER_FLOWER_PROPS, () -> BotaniaIntegrationBlockEntities.SOARLEANDER));
    public static final RegistryObject<Block> SOARLEANDER_FLOATING = register("soarleander_floating", () -> new FloatingSpecialFlowerBlock(BotaniaBlocks.FLOATING_PROPS, () -> BotaniaIntegrationBlockEntities.SOARLEANDER));
    public static final RegistryObject<Block> POTTED_SOARLEANDER = register("potted_soarleander", () -> new FlowerPotBlock(() -> ((FlowerPotBlock) Blocks.FLOWER_POT), SOARLEANDER, BlockBehaviour.Properties.copy(Blocks.POTTED_DANDELION).noOcclusion().lightLevel(level -> 5)));
    public static final RegistryObject<Block> INFINITY_MANA_POOL = register("infinity_mana_pool", () -> new InfinityManaPoolBlock(InfinityManaPoolBlock.Variant.CREATIVE, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Block> INFINITY_POTATO = register("infinity_potato", InfinityTinyPotatoBlock::new);

    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> obj) {
        return register(id, obj, b -> new BlockItem(b, new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> register(String id, Supplier<T> obj, Function<Block, Item> itemBuilder) {
        RegistryObject<T> r = REGISTRY.register(id, obj);
        BotaniaIntegrationItems.register(id, () -> itemBuilder.apply(r.get()));
        return r;
    }
}
