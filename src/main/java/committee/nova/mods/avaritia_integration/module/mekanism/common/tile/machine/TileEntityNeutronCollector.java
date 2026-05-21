package committee.nova.mods.avaritia_integration.module.mekanism.common.tile.machine;

import committee.nova.mods.avaritia_integration.module.mekanism.api.recipes.cache.ChemicalToItemCachedRecipe;
import committee.nova.mods.avaritia_integration.module.mekanism.api.recipes.chemicals.ChemicalStackToItemStackRecipe;
import committee.nova.mods.avaritia_integration.module.mekanism.common.recipe.MekIntegrationRecipeType;
import committee.nova.mods.avaritia_integration.module.mekanism.common.registries.MekIntegrationBlocks;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.BasicChemicalTank;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalTank;
import mekanism.api.chemical.attribute.ChemicalAttributes;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.radiation.IRadiationManager;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.slot.chemical.ChemicalInventorySlot;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.lib.radiation.RadiationManager;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler.ChemicalRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleChemical;
import mekanism.common.registries.MekanismChemicals;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TileEntityNeutronCollector extends TileEntityProgressMachine<ChemicalStackToItemStackRecipe> implements ChemicalRecipeLookupHandler<ChemicalStackToItemStackRecipe> {

    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );

    private static final long MAX_GAS = 10_000;

    public IChemicalTank gasTank;

    private final IInputHandler<@NotNull ChemicalStack> inputHandler;
    private final IOutputHandler<@NotNull ItemStack> outputHandler;

    private MachineEnergyContainer<TileEntityNeutronCollector> energyContainer;

    ChemicalInventorySlot gasInputSlot;
    OutputInventorySlot outputSlot;
    EnergyInventorySlot energySlot;

    public TileEntityNeutronCollector(BlockPos pos, BlockState state) {
        super(MekIntegrationBlocks.NEUTRON_COLLECTOR, pos, state, TRACKED_ERROR_TYPES, 200);
        configComponent.setupItemIOConfig(gasInputSlot, outputSlot, energySlot);
        configComponent.setupIOConfig(TransmissionType.CHEMICAL, gasTank, RelativeSide.RIGHT).setCanEject(false);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);

        inputHandler = InputHelper.getInputHandler(gasTank, RecipeError.NOT_ENOUGH_INPUT);
        outputHandler = OutputHelper.getOutputHandler(outputSlot, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    }

    @Override
    protected @Nullable IChemicalTankHolder getInitialChemicalTanks(IContentsListener listener, IContentsListener recipeCacheListener, IContentsListener recipeCacheUnpauseListener) {
        ChemicalTankHelper builder = ChemicalTankHelper.forSideWithConfig(this);
        builder.addTank(gasTank = BasicChemicalTank.createModern(MAX_GAS, (type, automationType) -> automationType != AutomationType.EXTERNAL ||
                        (type.isRadioactive() && RadiationManager.isGlobalRadiationEnabled()), ConstantPredicates.alwaysTrueBi(), this::containsRecipe,
                ChemicalAttributeValidator.ALWAYS_ALLOW, recipeCacheListener));
        return builder.build();
    }

    @Override
    protected @Nullable IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener, IContentsListener recipeCacheListener, IContentsListener recipeCacheUnpauseListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

    @Override
    protected @Nullable IInventorySlotHolder getInitialInventory(IContentsListener listener, IContentsListener recipeCacheListener, IContentsListener recipeCacheUnpauseListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this);
        builder.addSlot(gasInputSlot = ChemicalInventorySlot.fill(gasTank, listener, 7, 56));
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 131, 36))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_SPACE_IN_OUTPUT, getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE)));
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 7, 14));
        gasInputSlot.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    private boolean canGasInsert(Chemical Chemical) {
        return Chemical.equals(MekanismChemicals.SPENT_NUCLEAR_WASTE.get()) || Chemical.equals(MekanismChemicals.POLONIUM.get()) || Chemical.equals(MekanismChemicals.PLUTONIUM.get()) || Chemical.equals(MekanismChemicals.ANTIMATTER.get());
    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        gasInputSlot.fillTank();
        recipeCacheLookupMonitor.updateAndProcess();
        return sendUpdatePacket;
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<?, ChemicalStackToItemStackRecipe, SingleChemical<ChemicalStackToItemStackRecipe>> getRecipeType() {
        return MekIntegrationRecipeType.COLLECTING;
    }

    @Override
    public @Nullable ChemicalStackToItemStackRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandler);
    }

    @Override
    public @NotNull CachedRecipe<ChemicalStackToItemStackRecipe> createNewCachedRecipe(@NotNull ChemicalStackToItemStackRecipe recipe, int cacheIndex) {
        return ChemicalToItemCachedRecipe.chemicalToItem(recipe, recheckAllRecipeErrors, inputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(this::canFunction)
                .setActive(this::setActive)
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setRequiredTicks(this::getTicksRequired)
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    public MachineEnergyContainer<TileEntityNeutronCollector> getEnergyContainer() {
        return energyContainer;
    }
}
