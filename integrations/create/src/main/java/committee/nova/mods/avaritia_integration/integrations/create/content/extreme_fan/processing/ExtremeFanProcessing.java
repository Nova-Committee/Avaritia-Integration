package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_fan.processing;

import java.util.ArrayList;
import java.util.List;

import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class ExtremeFanProcessing {

    private ExtremeFanProcessing() {}

    public static boolean applyProcessing(ItemEntity entity, FanProcessingType type) {
        if (decrementProcessingTime(entity, type) != 0) {
            return false;
        }
        List<ItemStack> stacks = type.process(entity.getItem(), entity.level());
        if (stacks == null) {
            return false;
        }
        if (stacks.isEmpty()) {
            entity.discard();
            return false;
        }
        entity.setItem(stacks.removeFirst());
        for (ItemStack additional : stacks) {
            ItemEntity entityIn = new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), additional);
            entityIn.setDeltaMovement(entity.getDeltaMovement());
            entity.level().addFreshEntity(entityIn);
        }
        return true;
    }

    public static TransportedItemStackHandlerBehaviour.TransportedResult applyProcessing(TransportedItemStack transported,
            Level world, FanProcessingType type) {
        TransportedItemStackHandlerBehaviour.TransportedResult ignore = TransportedItemStackHandlerBehaviour.TransportedResult
                .doNothing();
        if (transported.processedBy != type) {
            transported.processedBy = type;
            transported.processingTime = 1;
            if (!type.canProcess(transported.stack, world)) {
                transported.processingTime = -1;
            }
            return ignore;
        }
        if (transported.processingTime == -1) {
            return ignore;
        }
        if (transported.processingTime-- > 0) {
            return ignore;
        }
        List<ItemStack> stacks = type.process(transported.stack, world);
        if (stacks == null) {
            return ignore;
        }
        List<TransportedItemStack> transportedStacks = new ArrayList<>();
        for (ItemStack additional : stacks) {
            TransportedItemStack newTransported = transported.getSimilar();
            newTransported.stack = additional.copy();
            transportedStacks.add(newTransported);
        }
        return TransportedItemStackHandlerBehaviour.TransportedResult.convertTo(transportedStacks);
    }

    private static int decrementProcessingTime(ItemEntity entity, FanProcessingType type) {
        CompoundTag nbt = entity.getPersistentData();
        if (!nbt.contains("CreateData")) {
            nbt.put("CreateData", new CompoundTag());
        }
        CompoundTag createData = nbt.getCompound("CreateData");
        if (!createData.contains("Processing")) {
            createData.put("Processing", new CompoundTag());
        }
        CompoundTag processing = createData.getCompound("Processing");
        String typeKey = type.toString();
        if (!processing.contains("Type") || !typeKey.equals(processing.getString("Type"))) {
            processing.putString("Type", typeKey);
            processing.putInt("Time", 1);
        }
        int value = processing.getInt("Time") - 1;
        processing.putInt("Time", value);
        return value;
    }
}
