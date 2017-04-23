package tardis.core.console.control;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.handlers.containers.PlayerContainer;
import io.darkcraft.darkcore.mod.nbt.NBTProperty;

import tardis.core.TardisInfo;

public abstract class AbstractControlInt extends AbstractControl
{
	protected final int min;
	protected final int max;

	@NBTProperty
	protected int value;

	protected AbstractControlInt(ControlIntBuilder builder, double regularXSize, double regularYSize, int angle)
	{
		super(builder, regularXSize, regularYSize, angle);
		min = builder.min;
		max = builder.max;
		value = builder.defaultVal;
	}

	public final int getValue()
	{
		return value;
	}

	public abstract void setValue(int value);

	public final void randomize()
	{
		setValue(DarkcoreMod.r.nextInt(max-min)+min);
	}

	@Override
	protected boolean activateControl(TardisInfo info, PlayerContainer player, boolean sneaking)
	{
		setValue(getValue() + (sneaking ? -1 : 1));
		return true;
	}

	public abstract static class ControlIntBuilder<T extends AbstractControlInt> extends ControlBuilder<T>
	{
		private int min;
		private int max;
		private int defaultVal;

		public ControlIntBuilder(int min, int max, int defaultVal)
		{
			this.min = min;
			this.max = max;
			this.defaultVal = defaultVal;
		}
	}
}