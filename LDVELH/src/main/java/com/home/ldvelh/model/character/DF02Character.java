package com.home.ldvelh.model.character;

import com.home.ldvelh.model.Die;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.DF02Spell;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.ListValueHolder;

import static com.home.ldvelh.model.value.ListValueHolder.ItemMergeType.MERGE;

public class DF02Character extends DFCharacter {
	private static final long serialVersionUID = -1335020175012413136L;

	private final IntValueHolder magicScore;
	private final ListValueHolder<DF02Spell> spells;

	public DF02Character() {
		super();
		int maxMagic = 6 + Die.SIX_FACES.roll(2);
		magicScore = new IntValueHolder(0, maxMagic, maxMagic);
		spells = new ListValueHolder<>(MERGE);
	}

	@Override public void initCharacterValues() {
		super.initCharacterValues();
		CharacterValues.put(Property.MAGIC, magicScore);
		CharacterValues.put(Property.SPELL_LIST, spells);
	}
}
