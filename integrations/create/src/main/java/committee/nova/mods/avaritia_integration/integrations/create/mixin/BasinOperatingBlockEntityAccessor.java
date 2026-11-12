package committee.nova.mods.avaritia_integration.integrations.create.mixin;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(BasinOperatingBlockEntity.class)
public interface BasinOperatingBlockEntityAccessor {

    @Invoker("getBasin")
    Optional<BasinBlockEntity> avaritia$getBasin();
}
