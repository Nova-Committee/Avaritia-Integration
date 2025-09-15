package committee.nova.mods.avaritia_integration.api.util.schedule.impl;

import committee.nova.mods.avaritia_integration.api.util.schedule.Task;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.fml.LogicalSide;

import java.util.function.IntPredicate;

public class SimpleGlobalTask extends Task<GlobalTicker> implements INBTSerializable<CompoundTag> {

	protected int tick = 0;
	protected LogicalSide side;
	protected Phase phase;
	protected IntPredicate function;

	public SimpleGlobalTask() {
	}

	public SimpleGlobalTask(LogicalSide side, Phase phase, IntPredicate function) {
		this.side = side;
		this.phase = phase;
		this.function = function;
	}

	@Override
	public boolean tick(GlobalTicker ticker) {
		return function.test(++tick);
	}

	@Override
	public GlobalTicker ticker() {
		return GlobalTicker.get(side, phase);
	}

	@Override
	public boolean shouldSave() {
		return getClass() != SimpleGlobalTask.class;
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag data = new CompoundTag();
		data.putInt("tick", tick);
		data.putBoolean("client", side.isClient());
		data.putBoolean("start", phase == Phase.START);
		return data;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		tick = nbt.getInt("tick");
		side = nbt.getBoolean("client") ? LogicalSide.CLIENT : LogicalSide.SERVER;
		phase = nbt.getBoolean("start") ? Phase.START : Phase.END;
	}

}
