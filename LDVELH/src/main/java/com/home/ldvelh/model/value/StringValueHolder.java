package com.home.ldvelh.model.value;

import org.apache.commons.lang3.StringUtils;

public class StringValueHolder extends ValueHolder<String> {
	private static final long serialVersionUID = 4840124512397844330L;

	public StringValueHolder() {
		super(StringUtils.EMPTY);
	}
}
