package committee.nova.mods.avaritia_integration.integrations.create.content.neutron_press;

import java.util.List;
import java.util.Optional;

import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class NeutronMechanicalPressBlockEntity extends MechanicalPressBlockEntity {

    public NeutronMechanicalPressBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.remove(pressingBehaviour);
        pressingBehaviour = new NeutronPressingBehaviour(this);
        behaviours.add(pressingBehaviour);
    }

    @Override
    protected void applyBasinRecipe() {
        if (currentRecipe == null) {
            return;
        }
        Optional<BasinBlockEntity> optionalBasin = getBasin();
        if (optionalBasin.isEmpty()) {
            return;
        }
        BasinBlockEntity basin = optionalBasin.get();
        boolean wasEmpty = basin.canContinueProcessing();
        boolean processedAtLeastOnce = false;
        int safetyNet = 64;
        while (safetyNet-- > 0 && BasinRecipe.apply(basin, currentRecipe)) {
            processedAtLeastOnce = true;
        }
        if (!processedAtLeastOnce) {
            return;
        }
        getProcessedRecipeTrigger().ifPresent(this::award);
        basin.inputTank.sendDataImmediately();
        if (wasEmpty && matchBasinRecipe(currentRecipe)) {
            continueWithPreviousRecipe();
            sendData();
        }
        basin.notifyChangeOfContents();
    }
}
