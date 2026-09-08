package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_depot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.api.contraption.storage.SyncedMountedStorage;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import com.simibubi.create.api.contraption.storage.item.WrapperMountedItemStorage;
import com.simibubi.create.content.contraptions.Contraption;

import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationMountedStorageTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import net.neoforged.neoforge.items.ItemStackHandler;

import org.jetbrains.annotations.Nullable;

public class ExtremeDepotMountedStorage extends WrapperMountedItemStorage<ExtremeDepotMountedStorage.Handler>
        implements SyncedMountedStorage {

    public static final MapCodec<ExtremeDepotMountedStorage> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(ItemStack.OPTIONAL_CODEC.optionalFieldOf("item", ItemStack.EMPTY)
                    .forGetter(ExtremeDepotMountedStorage::getItem))
            .apply(instance, ExtremeDepotMountedStorage::new));

    private boolean dirty;

    protected ExtremeDepotMountedStorage(ItemStack stack) {
        this(CreateIntegrationMountedStorageTypes.EXTREME_DEPOT.get(), stack);
    }

    protected ExtremeDepotMountedStorage(MountedItemStorageType<?> type, ItemStack stack) {
        super(type, new Handler(stack));
        this.wrapped.onChange = () -> this.dirty = true;
    }

    @Override
    public void unmount(Level level, BlockState state, BlockPos pos, @Nullable BlockEntity be) {
        if (be instanceof ExtremeDepotBlockEntity depot) {
            depot.setHeldItem(this.getStackInSlot(0));
        }
    }

    @Override
    public boolean handleInteraction(ServerPlayer player, Contraption contraption,
            StructureTemplate.StructureBlockInfo info) {
        return false;
    }

    @Override
    public boolean isDirty() {
        return this.dirty;
    }

    @Override
    public void markClean() {
        this.dirty = false;
    }

    @Override
    public void afterSync(Contraption contraption, BlockPos localPos) {
        BlockEntity be = contraption.getBlockEntityClientSide(localPos);
        if (be instanceof ExtremeDepotBlockEntity depot) {
            depot.setHeldItem(this.getItem());
        }
    }

    public ItemStack getItem() {
        return this.getStackInSlot(0);
    }

    public static ExtremeDepotMountedStorage fromDepot(ExtremeDepotBlockEntity depot) {
        return new ExtremeDepotMountedStorage(depot.getHeldItem().copy());
    }

    public static final class Handler extends ItemStackHandler {
        private Runnable onChange = () -> {
        };

        private Handler(ItemStack stack) {
            super(1);
            this.setStackInSlot(0, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            this.onChange.run();
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1024;
        }
    }
}
