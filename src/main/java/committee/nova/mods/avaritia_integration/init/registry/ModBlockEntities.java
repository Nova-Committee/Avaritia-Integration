package committee.nova.mods.avaritia_integration.init.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.common.blockentity.AsgardDandelionBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, AvaritiaIntegration.MOD_ID);

    public static final RegistryObject<BlockEntityType<AsgardDandelionBlockEntity>> ASGARD_DANDELION = BLOCK_ENTITIES.register(
            "asgard_dandelion",
            () -> BlockEntityType.Builder.of(
                    AsgardDandelionBlockEntity::new,
                    ModBlocks.asgard_dandelion.get()
            ).build(null)
    );
}
