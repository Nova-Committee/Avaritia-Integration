package committee.nova.mods.avaritia_integration.module.botania.registry;

import committee.nova.mods.avaritia.init.registry.ModRarities;
import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.botania.block.AsgardDandelionBlock;
import committee.nova.mods.avaritia_integration.module.botania.block.InfinityManaPoolBlock;
import committee.nova.mods.avaritia_integration.module.botania.block.InfinityTinyPotatoBlock;
import committee.nova.mods.avaritia_integration.module.botania.block.SoarleanderBlock;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.flower.FloatingSpecialFlowerBlock;

import java.util.function.Function;
import java.util.function.Supplier;

public final class BotaniaIntegrationBlocks {
    public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(AvaritiaIntegration.MOD_ID);

    public static final DeferredBlock<Block> ASGARD_DANDELION = register("asgard_dandelion",
            () -> new AsgardDandelionBlock(MobEffects.HUNGER, 0, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY), BotaniaIntegrationBlockEntities.ASGARD_DANDELION::get));
    public static final DeferredBlock<Block> ASGARD_DANDELION_FLOATING = register("asgard_dandelion_floating",
            () -> new FloatingSpecialFlowerBlock(BotaniaBlocks.FLOATING_PROPS, BotaniaIntegrationBlockEntities.ASGARD_DANDELION::get));
    public static final DeferredBlock<Block> POTTED_ASGARD_DANDELION = register("potted_asgard_dandelion", false,
            () -> flowerPot(ASGARD_DANDELION.get(), 15));
    public static final DeferredBlock<Block> SOARLEANDER = register("soarleander",
            () -> new SoarleanderBlock(MobEffects.WITHER, 1, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).lightLevel(level -> 5), BotaniaIntegrationBlockEntities.SOARLEANDER::get));
    public static final DeferredBlock<Block> SOARLEANDER_FLOATING = register("soarleander_floating",
            () -> new FloatingSpecialFlowerBlock(BotaniaBlocks.FLOATING_PROPS.lightLevel(level -> 5), BotaniaIntegrationBlockEntities.SOARLEANDER::get));
    public static final DeferredBlock<Block> POTTED_SOARLEANDER = register("potted_soarleander", false,
            () -> flowerPot(SOARLEANDER.get(), 5));
    public static final DeferredBlock<Block> INFINITY_MANA_POOL = register("infinity_mana_pool",
            () -> new InfinityManaPoolBlock(BlockBehaviour.Properties.ofFullCopy(BotaniaBlocks.livingrock).lightLevel(level -> 15)), new Item.Properties().rarity(ModRarities.COSMIC.getValue()));
    public static final DeferredBlock<Block> INFINITY_POTATO = register("infinity_potato",
            InfinityTinyPotatoBlock::new);

    private static <T extends Block> DeferredBlock<T> register(String id, Supplier<T> obj) {
        return register(id, obj, true, b -> new BlockItem(b, new Item.Properties()));
    }

    private static <T extends Block> DeferredBlock<T> register(String id, boolean hasItem, Supplier<T> obj) {
        return register(id, obj, hasItem, b -> new BlockItem(b, new Item.Properties()));
    }

    private static <T extends Block> DeferredBlock<T> register(String id, Supplier<T> obj, Item.Properties properties) {
        return register(id, obj, true, b -> new BlockItem(b, properties));
    }

    private static <T extends Block> DeferredBlock<T> register(String id, Supplier<T> obj, boolean hasItem, Item.Properties properties) {
        return register(id, obj, hasItem, b -> new BlockItem(b, properties));
    }

    private static <T extends Block> DeferredBlock<T> register(String id, Supplier<T> obj, boolean hasItem, Function<Block, Item> itemBuilder) {
        DeferredBlock<T> r = REGISTRY.register(id, obj);
        if (hasItem) BotaniaIntegrationItems.register(id, () -> itemBuilder.apply(r.get()));
        return r;
    }

    static FlowerPotBlock flowerPot(Block block, int lightLevel) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY);
        return new FlowerPotBlock(block, lightLevel > 0 ? properties.lightLevel((blockState) -> lightLevel) : properties);
    }

}