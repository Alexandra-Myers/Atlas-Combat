package net.alexandra.atlas.atlas_combat.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;
import net.minecraft.util.OptionEnum;

import java.util.Arrays;
import java.util.Comparator;

@Environment(EnvType.CLIENT)
public enum ShieldIndicatorStatus implements OptionEnum {
	OFF(0, "options.off"),
	CROSSHAIR(1, "options.attack.crosshair"),
	HOTBAR(2, "options.attack.hotbar");

	private static final ShieldIndicatorStatus[] BY_ID = (ShieldIndicatorStatus[]) Arrays.stream(values())
		.sorted(Comparator.comparingInt(ShieldIndicatorStatus::getId))
		.toArray(i -> new ShieldIndicatorStatus[i]);
	private final int id;
	private final String key;

	private ShieldIndicatorStatus(int j, String string2) {
		this.id = j;
		this.key = string2;
	}

	@Override
	public int getId() {
		return this.id;
	}

	@Override
	public String getKey() {
		return this.key;
	}

	public static ShieldIndicatorStatus byId(int id) {
		return BY_ID[Mth.positiveModulo(id, BY_ID.length)];
	}
}
