package committee.nova.mods.avaritia_integration.module.mekanism.common.tile.machine;

import committee.nova.mods.avaritia_integration.module.mekanism.api.recipes.cache.ChemicalToItemCachedRecipe;
import committee.nova.mods.avaritia_integration.module.mekanism.api.recipes.chemicals.GasStackToItemStackRecipe;
import committee.nova.mods.avaritia_integration.module.mekanism.common.recipe.MekIntegrationRecipeType;
import committee.nova.mods.avaritia_integration.module.mekanism.common.config.MekIntegrationEnergy;
import committee.nova.mods.avaritia_integration.module.mekanism.common.registry.MekIntegrationBlocks;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.gas.attribute.GasAttributes;
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
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.ISingleRecipeLookupHandler.ChemicalRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleChemical;
import mekanism.common.registries.MekanismGases;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityProgressMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TileEntityNeutronCollector extends TileEntityProgressMachine<GasStackToItemStackRecipe> implements ChemicalRecipeLookupHandler<Gas, GasStack, GasStackToItemStackRecipe> {

    private static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(
            RecipeError.NOT_ENOUGH_ENERGY,
            RecipeError.NOT_ENOUGH_INPUT,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT
    );

    private static final long MAX_GAS = MekIntegrationEnergy.NEUTRON_GAS_TANK;

    public IGasTank gasTank;

    private final IInputHandler<@NotNull GasStack> inputHandler;
    private final IOutputHandler<@NotNull ItemStack> outputHandler;

    private MachineEnergyContainer<TileEntityNeutronCollector> energyContainer;

    GasInventorySlot gasInputSlot;
    OutputInventorySlot outputSlot;
    EnergyInventorySlot energySlot;

    public TileEntityNeutronCollector(BlockPos pos, BlockState state) {
        super(MekIntegrationBlocks.NEUTRON_COLLECTOR, pos, state, TRACKED_ERROR_TYPES, MekIntegrationEnergy.NEUTRON_TICKS);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.GAS, TransmissionType.ENERGY);
        configComponent.setupItemIOConfig(gasInputSlot, outputSlot, energySlot);
        configComponent.setupIOConfig(TransmissionType.GAS, gasTank, RelativeSide.RIGHT).setCanEject(false);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);

        ejectorComponent = new TileComponentEjector(this);
        ejectorComponent.setOutputData(configComponent, TransmissionType.ITEM);

        inputHandler = InputHelper.getInputHandler(gasTank, RecipeError.NOT_ENOUGH_INPUT);
        outputHandler = OutputHelper.getOutputHandler(outputSlot, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    }

    @Override
    protected @Nullable IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper.forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(gasTank = ChemicalTankBuilder.GAS.create(MAX_GAS, (type, automationType) ->
                        automationType != AutomationType.EXTERNAL || (type.has(GasAttributes.Radiation.class) && IRadiationManager.INSTANCE.isRadiationEnabled()),
                ChemicalTankBuilder.GAS.alwaysTrueBi, this::containsRecipe,
                ChemicalAttributeValidator.ALWAYS_ALLOW, recipeCacheListener));
        return builder.build();
    }

    @Override
    protected @Nullable IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener, IContentsListener recipeCacheListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

    @Override
    protected @Nullable IInventorySlotHolder getInitialInventory(IContentsListener listener, IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(gasInputSlot = GasInventorySlot.fill(gasTank, listener, 7, 56));
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 131, 36))
                .tracksWarnings(slot -> slot.warning(WarningType.NO_SPACE_IN_OUTPUT, getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE)));
        builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 7, 14));
        gasInputSlot.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    private boolean canGasInsert(Gas gas) {
        return gas.equals(MekanismGases.SPENT_NUCLEAR_WASTE.get()) || gas.equals(MekanismGases.POLONIUM.get()) || gas.equals(MekanismGases.PLUTONIUM.get()) || gas.equals(MekanismGases.ANTIMATTER.get());
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        gasInputSlot.fillTank();
        recipeCacheLookupMonitor.updateAndProcess();
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<GasStackToItemStackRecipe, SingleChemical<Gas, GasStack, GasStackToItemStackRecipe>> getRecipeType() {
        return MekIntegrationRecipeType.COLLECTING;
    }

    @Override
    public @Nullable GasStackToItemStackRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(inputHandler);
    }

    @Override
    public @NotNull CachedRecipe<GasStackToItemStackRecipe> createNewCachedRecipe(@NotNull GasStackToItemStackRecipe recipe, int cacheIndex) {
        return ChemicalToItemCachedRecipe.gasToItem(recipe, recheckAllRecipeErrors, inputHandler, outputHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
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
