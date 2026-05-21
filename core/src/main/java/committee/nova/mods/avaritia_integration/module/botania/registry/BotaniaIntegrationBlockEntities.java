package committee.nova.mods.avaritia_integration.module.botania.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.botania.entity.AsgardDandelionBlockEntity;
import committee.nova.mods.avaritia_integration.module.botania.entity.InfinityManaPoolBlockEntity;
import committee.nova.mods.avaritia_integration.module.botania.entity.InfinityTinyPotatoBlockEntity;
import committee.nova.mods.avaritia_integration.module.botania.entity.SoarleanderBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class BotaniaIntegrationBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, AvaritiaIntegration.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AsgardDandelionBlockEntity>> ASGARD_DANDELION = register(
            "asgard_dandelion",
            () -> BlockEntityType.Builder.of(
                    AsgardDandelionBlockEntity::new,
                    BotaniaIntegrationBlocks.ASGARD_DANDELION.get(), BotaniaIntegrationBlocks.ASGARD_DANDELION_FLOATING.get()
            ).build(null)
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SoarleanderBlockEntity>> SOARLEANDER = register(
            "soarleander",
            () -> BlockEntityType.Builder.of(
                    SoarleanderBlockEntity::new,
                    BotaniaIntegrationBlocks.SOARLEANDER.get(), BotaniaIntegrationBlocks.SOARLEANDER_FLOATING.get()
            ).build(null)
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InfinityManaPoolBlockEntity>> INFINITY_MANA_POOL = register(
            "infinity_mana_pool",
            () -> BlockEntityType.Builder.of(
                    InfinityManaPoolBlockEntity::new,
                    BotaniaIntegrationBlocks.INFINITY_MANA_POOL.get()
            ).build(null)
    );
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<InfinityTinyPotatoBlockEntity>> INFINITY_TINY_POTATO = register(
            "infinity_tiny_potato",
            () -> BlockEntityType.Builder.of(
                    InfinityTinyPotatoBlockEntity::new,
                    BotaniaIntegrationBlocks.INFINITY_POTATO.get()
            ).build(null)
    );

    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String id, Supplier<BlockEntityType<T>> obj) {
        return REGISTRY.register(id, obj);
    }
}