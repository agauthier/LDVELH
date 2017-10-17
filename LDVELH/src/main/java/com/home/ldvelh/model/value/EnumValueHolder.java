package com.home.ldvelh.model.value;

public class EnumValueHolder<T extends Enum<T>> extends ValueHolder<T> {
	private static final long serialVersionUID = 6996498751589016510L;

	public EnumValueHolder(T value) {
		super(value);
	}

	public boolean is(T otherValue) {
		return getValue() == otherValue;
	}
}
