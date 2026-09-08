package committee.nova.mods.avaritia_integration.integrations.create.content.neutron_press;

import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class NeutronPressingBehaviour extends PressingBehaviour {
    public <T extends SmartBlockEntity & PressingBehaviourSpecifics> NeutronPressingBehaviour(T be) {
        super(be);
    }

    @Override
    protected void applyInWorld() {
        Level level = getWorld();
        BlockPos worldPosition = getPos();
        AABB bb = new AABB(worldPosition.below(1));

        particleItems.clear();

        if (level.isClientSide)
            return;

        for (Entity entity : level.getEntities(null, bb)) {
            if (!(entity instanceof ItemEntity itemEntity))
                continue;
            if (!entity.isAlive() || !entity.onGround())
                continue;

            if (specifics.tryProcessInWorld(itemEntity, false))
                blockEntity.sendData();
        }

        super.applyInWorld();
    }
}
