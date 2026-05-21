package committee.nova.mods.avaritia_integration.module.botania.registry;

import committee.nova.mods.avaritia_integration.AvaritiaIntegration;
import committee.nova.mods.avaritia_integration.module.botania.entity.AlphaSparkEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class BotaniaIntegrationEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, AvaritiaIntegration.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<AlphaSparkEntity>> ALPHA_SPARK_ENTITIES = register(
            "alpha_spark",
            () -> EntityType.Builder.<AlphaSparkEntity>of(AlphaSparkEntity::new, MobCategory.MISC)
                    .sized(0.2F, 0.5F)
                    .fireImmune()
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(ResourceLocation.tryBuild(AvaritiaIntegration.MOD_ID, "alpha_spark").toString())
    );


    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String id, Supplier<EntityType<T>> obj) {
        return REGISTRY.register(id, obj);
    }
}