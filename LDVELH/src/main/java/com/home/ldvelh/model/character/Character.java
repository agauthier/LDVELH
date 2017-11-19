package com.home.ldvelh.model.character;

import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.combat.CombatRow;
import com.home.ldvelh.model.combat.Fighter;
import com.home.ldvelh.model.combat.strategy.CombatStrategy;
import com.home.ldvelh.model.item.ItemAndQuantity;
import com.home.ldvelh.model.item.Note;
import com.home.ldvelh.model.item.SimpleItem;
import com.home.ldvelh.model.value.ListValueHolder;
import com.home.ldvelh.model.value.StringValueHolder;
import com.home.ldvelh.model.value.ValueHolder;
import com.home.ldvelh.ui.activity.AdventureActivity;
import com.home.ldvelh.ui.dialog.AdventureDialog;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class Character extends ValueHolder<Character> {
    private static final long serialVersionUID = -5749941905515892951L;

    private final List<Class<? extends AdventureDialog>> fulfilledDialogs = new ArrayList<>();
    private final ListValueHolder<SimpleItem> items;
    private final ListValueHolder<Note> notes;
    private final ListValueHolder<CombatRow> fighterGrid;
    private final StringValueHolder lastParagraph;

    Character() {
        super(null);
        items = new ListValueHolder<>(SimpleItem.class);
        notes = new ListValueHolder<>(Note.class);
        fighterGrid = new ListValueHolder<>(CombatRow.class);
        lastParagraph = new StringValueHolder();
    }

    public abstract void addLifeObserver(AdventureActivity activity);

    @SuppressWarnings("unused")
    public abstract void deleteLifeObserver(AdventureActivity activity);

    public abstract void killWithoutUpdate();

    public abstract boolean isDead();

    public abstract CombatStrategy getCombatStrategy();

    public abstract List<Fighter> getFighters();

    @SuppressWarnings("unused")
    public void addNote(String name) {
        Property.NOTE_LIST.get().add(new ItemAndQuantity(name));
    }

    public void initCharacterValues() {
        CharacterValues.clear();
        CharacterValues.put(Property.CHARACTER, this);
        CharacterValues.put(Property.ITEM_LIST, items);
        CharacterValues.put(Property.NOTE_LIST, notes);
        CharacterValues.put(Property.FIGHTER_GRID, fighterGrid);
        CharacterValues.put(Property.LAST_PARAGRAPH, lastParagraph);
    }

    public <T extends AdventureDialog> void fulfillDialog(Class<T> dialogClass) {
        fulfilledDialogs.add(dialogClass);
    }

    public <T extends AdventureDialog> List<Class<T>> getFulfilledDialogs() {
        List<Class<T>> fulfilledDialogsCopy = new ArrayList<>();
        for (Class<? extends AdventureDialog> dialogClass : fulfilledDialogs) {
            @SuppressWarnings("unchecked") final Class<T> dialogClassCast = (Class<T>) dialogClass;
            fulfilledDialogsCopy.add(dialogClassCast);
        }
        return fulfilledDialogsCopy;
    }

    public void startFromPreviousBook() {
        lastParagraph.setValue(StringUtils.EMPTY);
    }
}
