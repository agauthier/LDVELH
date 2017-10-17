package com.home.ldvelh.model.character;

import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.SimpleItem;
import com.home.ldvelh.model.value.ListValueHolder;

public class DF08Character extends DFCharacter {
	private static final long serialVersionUID = -5804099979330654638L;

	private final ListValueHolder<SimpleItem> stones = new ListValueHolder<>(SimpleItem.class);

	@Override public void initCharacterValues() {
		super.initCharacterValues();
		CharacterValues.put(Property.STONE_LIST, stones);
	}
}
