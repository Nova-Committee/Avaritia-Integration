package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_fan;

import java.util.Iterator;

import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import com.simibubi.create.content.kinetics.fan.IAirCurrentSource;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessing;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.advancement.AllAdvancements;

import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_fan.processing.ExtremeFanProcessing;

import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.apache.commons.lang3.tuple.Pair;

public class ExtremeAirCurrent extends AirCurrent {

    public ExtremeAirCurrent(IAirCurrentSource source) {
        super(source);
    }

    @Override
    protected void tickAffectedEntities(Level world) {
        for (Iterator<Entity> iterator = caughtEntities.iterator(); iterator.hasNext();) {
            Entity entity = iterator.next();
            if (!entity.isAlive() || !entity.getBoundingBox().intersects(bounds) || isPlayerCreativeFlying(entity)) {
                iterator.remove();
                continue;
            }

            Vec3i flow = (pushing ? direction : direction.getOpposite()).getNormal();
            float speed = Math.abs(source.getSpeed());
            float sneakModifier = entity.isShiftKeyDown() ? 4096f : 512f;
            double entityDistance = VecHelper.alignedDistanceToFace(entity.position(), source.getAirCurrentPos(),
                    direction);
            double entityDistanceOld = entity.position().distanceTo(VecHelper.getCenterOf(source.getAirCurrentPos()));
            float acceleration = (float) (speed / sneakModifier / (entityDistanceOld / Math.max(maxDistance, 0.0001f)));
            Vec3 previousMotion = entity.getDeltaMovement();
            float maxAcceleration = 5;

            double xIn = Mth.clamp(flow.getX() * acceleration - previousMotion.x, -maxAcceleration, maxAcceleration);
            double yIn = Mth.clamp(flow.getY() * acceleration - previousMotion.y, -maxAcceleration, maxAcceleration);
            double zIn = Mth.clamp(flow.getZ() * acceleration - previousMotion.z, -maxAcceleration, maxAcceleration);
            entity.setDeltaMovement(previousMotion.add(new Vec3(xIn, yIn, zIn).scale(1 / 8f)));
            entity.fallDistance = 0;

            FanProcessingType processingType = getTypeAt((float) entityDistance);
            if (processingType == null) {
                continue;
            }
            if (entity instanceof ItemEntity itemEntity) {
                if (world != null && world.isClientSide) {
                    processingType.spawnProcessingParticles(world, entity.position());
                    continue;
                }
                if (FanProcessing.canProcess(itemEntity, processingType)
                        && ExtremeFanProcessing.applyProcessing(itemEntity, processingType)
                        && source instanceof ExtremeEncasedFanBlockEntity fan) {
                    fan.award(AllAdvancements.FAN_PROCESSING);
                }
                continue;
            }
            if (world != null) {
                processingType.affectEntity(entity, world);
            }
        }
    }

    @Override
    public void tickAffectedHandlers() {
        for (Pair<TransportedItemStackHandlerBehaviour, FanProcessingType> pair : affectedItemHandlers) {
            TransportedItemStackHandlerBehaviour handler = pair.getKey();
            Level world = handler.getWorld();
            FanProcessingType processingType = pair.getRight();
            if (processingType == null) {
                continue;
            }
            handler.handleProcessingOnAllItems(transported -> {
                if (world.isClientSide) {
                    processingType.spawnProcessingParticles(world, handler.getWorldPositionOf(transported));
                    return TransportedItemStackHandlerBehaviour.TransportedResult.doNothing();
                }
                TransportedItemStackHandlerBehaviour.TransportedResult applyProcessing = ExtremeFanProcessing
                        .applyProcessing(transported, world, processingType);
                if (!applyProcessing.doesNothing() && source instanceof ExtremeEncasedFanBlockEntity fan) {
                    fan.award(AllAdvancements.FAN_PROCESSING);
                }
                return applyProcessing;
            });
        }
    }
}
