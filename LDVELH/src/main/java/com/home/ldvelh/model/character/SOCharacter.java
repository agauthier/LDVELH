package com.home.ldvelh.model.character;

import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.SimpleItem;
import com.home.ldvelh.model.value.BooleanValueHolder;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.ListValueHolder;

import static com.home.ldvelh.model.value.ListValueHolder.ItemMergeType.MERGE;

public class SOCharacter extends DFCharacter {
    private static final long serialVersionUID = 2522839728342382576L;

    private final IntValueHolder meals;
    private final BooleanValueHolder firstMealOfTheDay = new BooleanValueHolder();
    private final BooleanValueHolder libraInvoked = new BooleanValueHolder();
    private final ListValueHolder<SimpleItem> ingredients;

    public SOCharacter() {
        super();
        meals = new IntValueHolder(0, Constants.BIG_POSITIVE, 2);
        firstMealOfTheDay.set();
        ingredients = new ListValueHolder<>(MERGE);
    }

    @Override
    public void initCharacterValues() {
        super.initCharacterValues();
        CharacterValues.put(Property.MEALS, meals);
        CharacterValues.put(Property.INGREDIENT_LIST, ingredients);
    }

    @Override
    public void startFromPreviousBook() {
        super.startFromPreviousBook();
        firstMealOfTheDay.set();
        libraInvoked.unset();
    }

    public boolean isFirstMealOfTheDay() {
        return firstMealOfTheDay.isSet();
    }

    public void sleep() {
        firstMealOfTheDay.set();
    }

    public void eat() {
        firstMealOfTheDay.unset();
    }

    public boolean isLibraInvoked() {
        return libraInvoked.isSet();
    }

    public void invokeLibra(boolean restoreValues) {
        if (restoreValues) {
            skill.addWithFeedback(skill.getMax());
            stamina.addWithFeedback(stamina.getMax());
            luck.addWithFeedback(luck.getMax());
        }
        libraInvoked.set();
    }
}
