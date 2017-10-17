package com.home.ldvelh.model.value;

public class BooleanValueHolder extends ValueHolder<Boolean> {
	private static final long serialVersionUID = 9111142037769058360L;

	public BooleanValueHolder() {
		super(false);
	}

	public boolean isSet() {
		return getValue();
	}

	public void set() {
		setValue(true);
	}

	public void unset() {
		setValue(false);
	}
}
