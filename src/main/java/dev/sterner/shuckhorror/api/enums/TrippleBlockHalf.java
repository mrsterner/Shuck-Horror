package dev.sterner.shuckhorror.api.enums;

import net.minecraft.util.StringIdentifiable;

public enum TrippleBlockHalf implements StringIdentifiable {
	UPPER,
	MIDDLE,
	LOWER;

	public String toString() {
		return this.asString();
	}

	@Override
	public String asString() {
		return this == UPPER ? "upper" : this == MIDDLE ? "middle" : "lower";
	}
}
