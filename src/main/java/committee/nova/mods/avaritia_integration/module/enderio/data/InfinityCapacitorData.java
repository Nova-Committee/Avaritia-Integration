package committee.nova.mods.avaritia_integration.module.enderio.data;

import com.enderio.api.capacitor.CapacitorModifier;
import com.enderio.api.capacitor.ICapacitorData;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class InfinityCapacitorData implements ICapacitorData, INBTSerializable<Tag> {
    public static final InfinityCapacitorData INSTANCE = new InfinityCapacitorData();

    private InfinityCapacitorData() {
    }

    @Override
    public float getBase() {
        return 10;
    }

    @Override
    public float getModifier(@NotNull CapacitorModifier modifier) {
        return this.getBase();
    }

    @Override
    public @NotNull Map<CapacitorModifier, Float> getAllModifiers() {
        return Map.of();
    }

    @Override
    public Tag serializeNBT() {
        return FloatTag.valueOf(this.getBase());
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        // We use fixed value so don't need to modify it.
    }
}
