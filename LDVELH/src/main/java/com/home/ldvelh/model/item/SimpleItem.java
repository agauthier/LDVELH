package com.home.ldvelh.model.item;

import java.util.List;

public class SimpleItem extends Item {
	private static final long serialVersionUID = 4589875732128486040L;

	private SimpleItem() { super(); }

	@SuppressWarnings("unused")
	private SimpleItem(String name, List<Effect> effects, Object data) {
		super(name, effects, data);
	}

	@Override
	public <T extends Item> T copy() {
		SimpleItem simpleItem = new SimpleItem();
		super.populate(simpleItem);
		//noinspection unchecked
		return (T) simpleItem;
	}
}
