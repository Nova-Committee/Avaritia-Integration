package committee.nova.mods.avaritia_integration.integrations.create.compat.cca;

import java.util.Optional;

import com.simibubi.create.foundation.fluid.SmartFluidTank;

import committee.nova.mods.avaritia_integration.integrations.create.compat.cca.liquid_burning.FluidRecipeInput;
import committee.nova.mods.avaritia_integration.integrations.create.compat.cca.liquid_burning.LiquidBurningRecipe;
import committee.nova.mods.avaritia_integration.integrations.create.content.extreme_burner.ExtremeBlazeBurnerBlockEntity;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationRecipeTypes;

import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class CCALiquidBlazeBurnerCompat implements ICCABurnerCompat {

    private final ExtremeBlazeBurnerBlockEntity be;
    private final SmartFluidTank fluidInventory;
    private Optional<LiquidBurningRecipe> recipeCache = Optional.empty();

    public CCALiquidBlazeBurnerCompat(ExtremeBlazeBurnerBlockEntity be) {
        this.be = be;
        this.fluidInventory = new SmartFluidTank(4000, this::update);
        this.fluidInventory.setValidator(stack -> find(stack, be.getLevel()).isPresent());
    }

    @Override
    public IFluidHandler getFluidInventory() {
        return fluidInventory;
    }

    public SmartFluidTank tank() {
        return fluidInventory;
    }

    public void update(FluidStack stack) {
        if (!be.hasLevel() || be.getLevel() == null || be.getLevel().isClientSide) {
            return;
        }
        recipeCache = find(stack, be.getLevel());
    }

    public Optional<LiquidBurningRecipe> find(FluidStack stack, Level level) {
        if (stack == null || stack.isEmpty() || level == null) {
            return Optional.empty();
        }
        return CreateIntegrationRecipeTypes.LIQUID_BURNING.find(new FluidRecipeInput(stack), level)
                .map(holder -> (LiquidBurningRecipe) holder.value());
    }

    public Optional<LiquidBurningRecipe> getRecipeCache() {
        return recipeCache;
    }

    @Override
    public FluidStack getFuelFluid() {
        return fluidInventory.getFluid();
    }
}
