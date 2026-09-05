package committee.nova.mods.avaritia_integration.integrations.mekanism.common.capabilities.energy;

import committee.nova.mods.avaritia_integration.integrations.mekanism.common.config.MekBigEnergy;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

import com.google.common.base.Preconditions;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.SerializationConstants;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.functions.ConstantPredicates;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.util.NBTUtils;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.function.Predicate;

@NothingNullByDefault
public class BigIntegerEnergyContainer extends BasicEnergyContainer {

    private BigInteger storedBig = BigInteger.ZERO;
    private final BigInteger maxBig;

    public static BigIntegerEnergyContainer output(BigInteger maxEnergy, @Nullable IContentsListener listener) {
        return new BigIntegerEnergyContainer(maxEnergy, ConstantPredicates.alwaysTrue(), internalOnly, listener);
    }

    protected BigIntegerEnergyContainer(BigInteger maxEnergy, Predicate<AutomationType> canExtract,
                                        Predicate<AutomationType> canInsert, @Nullable IContentsListener listener) {
        super(Long.MAX_VALUE, canExtract, canInsert, listener);
        this.maxBig = maxEnergy.max(MekBigEnergy.LONG_MAX);
    }

    public BigInteger getStoredBig() {
        return storedBig;
    }

    public BigInteger getDoubleMaxOverflows() {
        return MekBigEnergy.doubleMaxOverflows(storedBig);
    }

    @Override
    public long getEnergy() {
        return MekBigEnergy.toMekanismLong(storedBig);
    }

    @Override
    public void setEnergy(long energy) {
        Preconditions.checkArgument(energy >= 0, "Energy cannot be negative");
        if (storedBig.compareTo(MekBigEnergy.LONG_MAX) > 0 && energy == Long.MAX_VALUE) {
            return;
        }
        storedBig = MekBigEnergy.fromMekanismLong(energy).min(maxBig);
        onContentsChanged();
    }

    @Override
    public boolean isEmpty() {
        return storedBig.signum() == 0;
    }

    @Override
    public long getNeeded() {
        return MekBigEnergy.toMekanismLong(maxBig.subtract(storedBig));
    }

    @Override
    public long insert(long amount, Action action, AutomationType automationType) {
        if (amount <= 0L || !canInsert.test(automationType)) {
            return amount;
        }
        BigInteger room = maxBig.subtract(storedBig);
        if (room.signum() <= 0) {
            return amount;
        }
        BigInteger requested = automationType == AutomationType.INTERNAL ?
                MekBigEnergy.infinitySupplyTick() : MekBigEnergy.fromMekanismLong(amount);
        BigInteger toAdd = requested.min(room);
        if (toAdd.signum() <= 0) {
            return amount;
        }
        if (action.execute()) {
            storedBig = storedBig.add(toAdd);
            onContentsChanged();
        }
        if (automationType == AutomationType.INTERNAL) {
            return 0L;
        }
        return amount - MekBigEnergy.toMekanismLong(toAdd.min(MekBigEnergy.LONG_MAX));
    }

    @Override
    public long extract(long amount, Action action, AutomationType automationType) {
        if (isEmpty() || amount <= 0L || !canExtract.test(automationType)) {
            return 0L;
        }
        BigInteger toTake = MekBigEnergy.fromMekanismLong(amount).min(storedBig).min(MekBigEnergy.LONG_MAX);
        long extracted = MekBigEnergy.toMekanismLong(toTake);
        if (extracted > 0L && action.execute()) {
            storedBig = storedBig.subtract(toTake);
            onContentsChanged();
        }
        return extracted;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        if (!isEmpty()) {
            nbt.putString(MekBigEnergy.NBT_STORED_BIG, storedBig.toString());
            nbt.putLong(SerializationConstants.STORED, getEnergy());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt.contains(MekBigEnergy.NBT_STORED_BIG)) {
            try {
                storedBig = new BigInteger(nbt.getString(MekBigEnergy.NBT_STORED_BIG)).max(BigInteger.ZERO).min(maxBig);
                return;
            } catch (NumberFormatException ignored) {
                // Fall through to legacy long.
            }
        }
        NBTUtils.setLegacyEnergyIfPresent(nbt, SerializationConstants.STORED, this::setEnergy);
    }
}
