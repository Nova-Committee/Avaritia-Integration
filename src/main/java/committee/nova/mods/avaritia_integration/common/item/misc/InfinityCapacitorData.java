package committee.nova.mods.avaritia_integration.common.item.misc;

import com.enderio.api.capacitor.CapacitorModifier;
import com.enderio.api.capacitor.ICapacitorData;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

public class InfinityCapacitorData implements ICapacitorData, INBTSerializable<Tag> {
    public static final InfinityCapacitorData INSTANCE = new InfinityCapacitorData();

    private static final float BASE = 9.999999046325684F;

    private InfinityCapacitorData() {
    }

    @Override
    public float getBase() {
        return BASE;
    }

    @Override
    public float getModifier(CapacitorModifier modifier) {
        return getBase();
    }

    @Override
    public Map<CapacitorModifier, Float> getAllModifiers() {
        return Map.of();
    }

    @Override
    public Tag serializeNBT() {
        return FloatTag.valueOf(BASE);
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        // 无需实现，因为这是固定值
    }
}
