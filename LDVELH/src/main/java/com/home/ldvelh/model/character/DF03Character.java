package com.home.ldvelh.model.character;

import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.SimpleItem;
import com.home.ldvelh.model.value.ListValueHolder;

import static com.home.ldvelh.model.value.ListValueHolder.ItemMergeType.MERGE;

public class DF03Character extends DFCharacter {
	private static final long serialVersionUID = -1335020175012413136L;

	private final ListValueHolder<SimpleItem> magicItems;

	public DF03Character() {
		super();
		magicItems = new ListValueHolder<>(MERGE);
	}

	@Override public void initCharacterValues() {
		super.initCharacterValues();
		CharacterValues.put(Property.MAGIC_ITEM_LIST, magicItems);
	}
}
