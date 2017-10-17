package com.home.ldvelh.model.character;

import com.home.ldvelh.model.Die;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.ui.activity.AdventureActivity;

public class DF10Character extends DFCharacter {
	private static final long serialVersionUID = -8075913840824276855L;

	private static final int STARTING_SKILL_PENALTY = -3;

	private final IntValueHolder fear;

	public DF10Character() {
		super();
		int maxFear = 6 + Die.SIX_FACES.roll(1);
		fear = new IntValueHolder(0, maxFear, 0);
		skill.add(STARTING_SKILL_PENALTY);
	}

	@Override public void initCharacterValues() {
		super.initCharacterValues();
		CharacterValues.put(Property.FEAR, fear);
	}

	@Override public void addLifeObserver(AdventureActivity activity) {
		super.addLifeObserver(activity);
		fear.addObserver(this);
		addObserver(activity);
	}

	@Override public void deleteLifeObserver(AdventureActivity activity) {
		super.deleteLifeObserver(activity);
		deleteObserver(activity);
		fear.deleteObserver(this);
	}

	@Override public boolean isDead() {
		return super.isDead() || fear.getValue() >= fear.getMax();
	}
}
