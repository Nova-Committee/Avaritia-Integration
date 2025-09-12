package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.common.blockentity.AsgardDandelionBlockEntity;
import committee.nova.mods.avaritia_integration.common.blockentity.InfinityManaPoolBlockEntity;
import committee.nova.mods.avaritia_integration.common.blockentity.InfinityTinyPotatoBlockEntity;
import committee.nova.mods.avaritia_integration.common.blockentity.SoarleanderBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AvaritiaIntegration.MOD_ID);

    public static final RegistryObject<BlockEntityType<AsgardDandelionBlockEntity>> ASGARD_DANDELION = BLOCK_ENTITIES.register(
            "asgard_dandelion_be",
            () -> BlockEntityType.Builder.of(
                    AsgardDandelionBlockEntity::new,
                    ModBlocks.asgard_dandelion.get(),ModBlocks.asgard_dandelion_floating.get()
            ).build(null)
    );
    public static final RegistryObject<BlockEntityType<SoarleanderBlockEntity>> SOARLEANDER = BLOCK_ENTITIES.register(
            "soarleander_be",
            () -> BlockEntityType.Builder.of(
                    SoarleanderBlockEntity::new,
                    ModBlocks.soarleander.get(),ModBlocks.soarleander_floating.get()
            ).build(null)
    );
    public static final RegistryObject<BlockEntityType<InfinityManaPoolBlockEntity>> INFINITY_MANA_POOL = BLOCK_ENTITIES.register(
            "infinity_mana_pool",
            () -> BlockEntityType.Builder.of(
                    InfinityManaPoolBlockEntity::new,
                    ModBlocks.infinity_mana_pool.get()
            ).build(null)
    );
    public static final RegistryObject<BlockEntityType<InfinityTinyPotatoBlockEntity>> INFINITY_TINY_POTATO = BLOCK_ENTITIES.register(
            "infinity_tiny_potato",
            () -> BlockEntityType.Builder.of(
                    InfinityTinyPotatoBlockEntity::new,
                    ModBlocks.infinity_potato.get()
            ).build(null)
    );
}
