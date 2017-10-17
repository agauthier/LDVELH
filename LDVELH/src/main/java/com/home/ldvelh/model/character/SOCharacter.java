package com.home.ldvelh.model.character;

import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.SimpleItem;
import com.home.ldvelh.model.value.BooleanValueHolder;
import com.home.ldvelh.model.value.ListValueHolder;

public class SOCharacter extends DFCharacter {
	private static final long serialVersionUID = -6641438938581221588L;

	private final BooleanValueHolder libraInvoked = new BooleanValueHolder();
	private final ListValueHolder<SimpleItem> ingredients;

	public SOCharacter() {
		super();
		ingredients = new ListValueHolder<>(SimpleItem.class);
	}

	@Override public void initCharacterValues() {
		super.initCharacterValues();
		CharacterValues.put(Property.INGREDIENT_LIST, ingredients);
	}

	@Override public void startFromPreviousBook() {
		super.startFromPreviousBook();
		libraInvoked.unset();
	}

	public void invokeLibra(boolean restoreValues) {
		if (restoreValues) {
			skill.addWithFeedback(skill.getMax());
			stamina.addWithFeedback(stamina.getMax());
			luck.addWithFeedback(luck.getMax());
		}
		libraInvoked.set();
	}

	public boolean isLibraInvoked() {
		return libraInvoked.isSet();
	}
}
