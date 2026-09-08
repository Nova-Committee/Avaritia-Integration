package committee.nova.mods.avaritia_integration.integrations.create.content.extreme_burner;

import java.util.List;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import committee.nova.mods.avaritia.init.registry.ModItems;
import committee.nova.mods.avaritia_integration.integrations.create.compat.cca.CCALiquidBlazeBurnerCompat;
import committee.nova.mods.avaritia_integration.integrations.create.compat.cca.ICCABurnerCompat;
import committee.nova.mods.avaritia_integration.integrations.create.compat.cca.liquid_burning.LiquidBurningRecipe;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationBlockEntityTypes;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationItems;
import committee.nova.mods.avaritia_integration.integrations.create.registry.CreateIntegrationTags;

import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class ExtremeBlazeBurnerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    public static final int MAX_HEAT_CAPACITY = 10000;

    public enum FuelType {
        NONE, BLAZE, STAR
    }

    protected FuelType activeFuel = FuelType.NONE;
    protected int remainingBurnTime;
    public LerpedFloat headAnimation = LerpedFloat.linear();
    public LerpedFloat headAngle = LerpedFloat.angular();
    public boolean goggles;
    public boolean hat;
    public boolean hasStraw;
    public boolean stockKeeper;

    protected ICCABurnerCompat fluidInventory;

    public ExtremeBlazeBurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        fluidInventory = new CCALiquidBlazeBurnerCompat(this);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK,
                CreateIntegrationBlockEntityTypes.EXTREME_HEATER.get(),
                (be, context) -> be.fluidInventory == null ? null : be.fluidInventory.getFluidInventory());
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null) {
            return;
        }
        boolean inWorld = !isVirtual();
        if (level.isClientSide) {
            tickAnimation();
            if (!inWorld) {
                return;
            }
        }
        if (!level.isClientSide) {
            fluidBurningTick();
        }

        if (inWorld && remainingBurnTime > 0) {
            remainingBurnTime--;
        }
        if (remainingBurnTime <= 0) {
            if (activeFuel == FuelType.STAR) {
                activeFuel = FuelType.BLAZE;
                remainingBurnTime = 5000;
            } else {
                activeFuel = FuelType.NONE;
            }
        }
        updateBlockState();
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        stockKeeper = BlazeBurnerBlockEntity.getStockTicker(level, worldPosition) != null;
    }

    @OnlyIn(Dist.CLIENT)
    public void tickAnimation() {
        float target = 0;
        ExtremeBlazeBurnerBlock.ExtremeHeatLevel heat = getHeatLevelForRender();
        if (heat.isAtLeast(ExtremeBlazeBurnerBlock.ExtremeHeatLevel.FADING)) {
            target = 1;
        }
        headAnimation.chase(target, 0.25f, LerpedFloat.Chaser.EXP);
        headAnimation.tickChaser();
        float angle = AngleHelper.horizontalAngle(getBlockState().getValue(ExtremeBlazeBurnerBlock.FACING));
        headAngle.chase(angle, 0.25f, LerpedFloat.Chaser.EXP);
        headAngle.tickChaser();
    }

    public ExtremeBlazeBurnerBlock.ExtremeHeatLevel getExtremeHeatLevelFromBlock() {
        return ExtremeBlazeBurnerBlock.getExtremeHeatLevelOf(getBlockState());
    }

    public ExtremeBlazeBurnerBlock.ExtremeHeatLevel getHeatLevelForRender() {
        return getExtremeHeatLevelFromBlock();
    }

    public boolean isValidBlockAbove() {
        if (level == null) {
            return false;
        }
        return !level.getBlockState(worldPosition.above()).isAir();
    }
    public void updateBlockState() {
        ExtremeBlazeBurnerBlock.ExtremeHeatLevel heat = ExtremeBlazeBurnerBlock.ExtremeHeatLevel.SMOULDERING;
        if (activeFuel == FuelType.STAR) {
            heat = ExtremeBlazeBurnerBlock.ExtremeHeatLevel.STAR;
        } else if (activeFuel == FuelType.BLAZE) {
            heat = remainingBurnTime > 20 ? ExtremeBlazeBurnerBlock.ExtremeHeatLevel.BLAZE
                    : ExtremeBlazeBurnerBlock.ExtremeHeatLevel.FADING;
        }
        if (level != null && getBlockState().hasProperty(ExtremeBlazeBurnerBlock.EXTREME_HEAT_LEVEL)
                && getBlockState().getValue(ExtremeBlazeBurnerBlock.EXTREME_HEAT_LEVEL) != heat) {
            level.setBlockAndUpdate(worldPosition,
                    getBlockState().setValue(ExtremeBlazeBurnerBlock.EXTREME_HEAT_LEVEL, heat));
            notifyUpdate();
        }
    }

    public boolean isCreativeFuel(ItemStack stack) {
        return stack.is(ModItems.infinity_ingot.get());
    }

    public void applyCreativeFuel() {
        activeFuel = FuelType.STAR;
        remainingBurnTime = MAX_HEAT_CAPACITY;
        updateBlockState();
    }

    public boolean tryUpdateFuel(ItemStack itemStack, boolean forceOverflow, boolean simulate) {
        FuelType newFuel = FuelType.NONE;
        int newBurnTime = 0;
        if (CreateIntegrationTags.ItemTags.BLAZE_BURNER_FUEL_STAR.matches(itemStack)
                || itemStack.is(CreateIntegrationItems.STAR_CAKE.get())) {
            newFuel = FuelType.STAR;
            newBurnTime = MAX_HEAT_CAPACITY;
        } else if (CreateIntegrationTags.ItemTags.BLAZE_BURNER_FUEL_BLAZE.matches(itemStack)
                || itemStack.is(CreateIntegrationItems.IGNIS_CAKE.get())) {
            newFuel = FuelType.BLAZE;
            newBurnTime = 5000;
        }
        if (newFuel == FuelType.NONE) {
            return false;
        }
        if (newFuel.ordinal() < activeFuel.ordinal()) {
            return false;
        }
        if (newFuel == activeFuel && remainingBurnTime > MAX_HEAT_CAPACITY / 2 && !forceOverflow) {
            return false;
        }
        if (!simulate) {
            activeFuel = newFuel;
            remainingBurnTime = Math.min(remainingBurnTime + newBurnTime, MAX_HEAT_CAPACITY);
            playSound();
            updateBlockState();
        }
        return true;
    }

    public boolean tryUpdateLiquid(ItemStack stack, net.minecraft.world.entity.player.Player player, boolean simulate) {
        if (!(fluidInventory instanceof CCALiquidBlazeBurnerCompat inv) || level == null) {
            return false;
        }
        return FluidUtil.tryEmptyContainer(stack, inv.getFluidInventory(), 1000, player, !simulate).isSuccess();
    }

    private void fluidBurningTick() {
        if (!(fluidInventory instanceof CCALiquidBlazeBurnerCompat inv)) {
            return;
        }
        inv.update(inv.tank().getFluid());
        if (inv.getRecipeCache().isEmpty() || remainingBurnTime > 6000) {
            return;
        }
        LiquidBurningRecipe recipe = inv.getRecipeCache().get();
        int requiredAmount = Math.max(1, recipe.input().amount());
        FluidStack simDrained = inv.tank().drain(requiredAmount, IFluidHandler.FluidAction.SIMULATE);
        if (simDrained.isEmpty() || simDrained.getAmount() < requiredAmount) {
            return;
        }
        FluidStack actualDrained = inv.tank().drain(requiredAmount, IFluidHandler.FluidAction.EXECUTE);
        activeFuel = recipe.starheated() ? FuelType.STAR : FuelType.BLAZE;
        remainingBurnTime += (int) (((float) recipe.burnTime() / requiredAmount) * actualDrained.getAmount());
        playSound();
        updateBlockState();
    }

    protected void playSound() {
        if (level == null) {
            return;
        }
        level.playSound(null, worldPosition, SoundEvents.BLAZE_SHOOT, SoundSource.BLOCKS,
                0.125f + level.random.nextFloat() * 0.125f, 1.15f - level.random.nextFloat() * 0.25f);
    }

    public void spawnParticleBurst(boolean star) {
        if (level == null) {
            return;
        }
        Vec3 center = VecHelper.getCenterOf(worldPosition);
        for (int i = 0; i < 20; i++) {
            Vec3 motion = VecHelper.offsetRandomly(Vec3.ZERO, level.random, 0.125f);
            level.addParticle(star ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME, center.x, center.y, center.z,
                    motion.x, motion.y, motion.z);
        }
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.putInt("FuelLevel", activeFuel.ordinal());
        compound.putInt("BurnTimeRemaining", remainingBurnTime);
        compound.putBoolean("Goggles", goggles);
        compound.putBoolean("Hat", hat);
        compound.putBoolean("Straw", hasStraw);
        if (fluidInventory instanceof CCALiquidBlazeBurnerCompat inv) {
            compound.put("Tank", inv.tank().writeToNBT(registries, new CompoundTag()));
        }
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        activeFuel = FuelType.values()[Mth.clamp(compound.getInt("FuelLevel"), 0, FuelType.values().length - 1)];
        remainingBurnTime = compound.getInt("BurnTimeRemaining");
        goggles = compound.getBoolean("Goggles");
        hat = compound.getBoolean("Hat");
        hasStraw = compound.getBoolean("Straw");
        if (fluidInventory instanceof CCALiquidBlazeBurnerCompat inv && compound.contains("Tank")) {
            inv.tank().readFromNBT(registries, compound.getCompound("Tank"));
            inv.update(inv.tank().getFluid());
        }
    }
}
