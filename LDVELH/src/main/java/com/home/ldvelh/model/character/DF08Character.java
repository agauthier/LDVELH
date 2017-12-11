package com.home.ldvelh.model.character;

import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.EffectItem;
import com.home.ldvelh.model.value.ListValueHolder;

import static com.home.ldvelh.model.value.ListValueHolder.ItemMergeType.MERGE;

public class DF08Character extends DFCharacter {
	private static final long serialVersionUID = -5804099979330654638L;

	private final ListValueHolder<EffectItem> stones = new ListValueHolder<>(MERGE);

	@Override public void initCharacterValues() {
		super.initCharacterValues();
		CharacterValues.put(Property.STONE_LIST, stones);
	}
}
