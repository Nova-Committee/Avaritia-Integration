package committee.nova.mods.avaritia_integration.integrations.create.mixin;

import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import com.simibubi.create.content.logistics.depot.DepotBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DepotBlockEntity.class)
public interface DepotBlockEntityAccessor {

    @Accessor("depotBehaviour")
    DepotBehaviour avaritia$getDepotBehaviour();
}
