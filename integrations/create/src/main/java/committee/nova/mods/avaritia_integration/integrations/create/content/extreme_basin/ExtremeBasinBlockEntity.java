package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_basin;

import java.util.List;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinInventory;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;

import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_burner.ExtremeBlazeBurnerBlock;
import committee.nova.mods.avaritia_integration.integrations.create.content.matrix_mixer.MatrixMechanicalMixerBlockEntity;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlockEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import org.jetbrains.annotations.Nullable;

public class ExtremeBasinBlockEntity extends BasinBlockEntity {
    private @Nullable ExtremeBlazeBurnerBlock.ExtremeHeatLevel cachedExtremeHeatLevel;

    public ExtremeBasinBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inputInventory = new BasinInventory(9, this);
        inputInventory.withMaxStackSize(1024);
        inputInventory.whenContentsChanged($ -> notifyChangeOfContents());
        outputInventory = new BasinInventory(9, this);
        outputInventory.forbidInsertion().withMaxStackSize(1024);
        itemCapability = new CombinedInvWrapper(inputInventory, outputInventory);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,
                CreateIntegrationBlockEntityTypes.EXTREME_BASIN.get(), (be, context) -> be.itemCapability);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
                CreateIntegrationBlockEntityTypes.EXTREME_BASIN.get(), (be, context) -> be.fluidCapability);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        inputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 2, 16000, true)
                .whenFluidUpdates(this::notifyChangeOfContents);
        outputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.OUTPUT, this, 2, 16000, true)
                .whenFluidUpdates(this::notifyChangeOfContents)
                .forbidInsertion();
        behaviours.removeIf(behaviour -> behaviour instanceof SmartFluidTankBehaviour);
        behaviours.add(inputTank);
        behaviours.add(outputTank);
        fluidCapability = new CombinedTankWrapper(outputTank.getCapability(), inputTank.getCapability());
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (level == null || !level.isClientSide) {
            return;
        }
        BlockEntity above = level.getBlockEntity(worldPosition.above(2));
        if (above instanceof MatrixMechanicalMixerBlockEntity mixer) {
            setAreFluidsMoving(mixer.running && mixer.runningTicks <= 20);
        }
    }

    public @Nullable ExtremeBlazeBurnerBlock.ExtremeHeatLevel getCachedExtremeHeatLevel() {
        return cachedExtremeHeatLevel;
    }

    public void setCachedExtremeHeatLevel(ExtremeBlazeBurnerBlock.ExtremeHeatLevel level) {
        this.cachedExtremeHeatLevel = level;
    }

    public static ExtremeBlazeBurnerBlock.ExtremeHeatLevel getExtremeHeatLevelOf(net.minecraft.world.level.block.state.BlockState state) {
        return ExtremeBlazeBurnerBlock.getExtremeHeatLevelOf(state);
    }
}

