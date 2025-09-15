package committee.nova.mods.avaritia_integration.module.enderio.item;

import com.enderio.api.capability.IMultiCapabilityItem;
import com.enderio.api.capability.MultiCapabilityProvider;
import com.enderio.api.capacitor.ICapacitorData;
import com.enderio.base.common.init.EIOCapabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public class InfinityCapacitorItem extends Item implements IMultiCapabilityItem {
    private final ICapacitorData data;

    public InfinityCapacitorItem(ICapacitorData data, Properties properties) {
        super(properties);
        this.data = data;
    }

    @Override
    public @Nullable MultiCapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt, MultiCapabilityProvider provider) {
        provider.add(EIOCapabilities.CAPACITOR, LazyOptional.of(() -> this.data));
        return provider;
    }
}
