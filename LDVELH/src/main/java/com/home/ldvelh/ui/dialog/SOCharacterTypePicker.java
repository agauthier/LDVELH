package com.home.ldvelh.ui.dialog;

import android.content.Context;
import android.util.Pair;

import com.home.ldvelh.AdventureConfig;
import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.ui.page.SOSpellsPage;

import java.util.ArrayList;
import java.util.List;

public class SOCharacterTypePicker extends MultipleChoiceDialog {

    public SOCharacterTypePicker(Context context, Object data) {
        super(context, data);
    }

    @Override
    protected Pair<Integer, List<Choice>> getChoices() {
        return new Pair<>(R.string.so_character_type_choice, getCharacterTypeChoices());
    }

    @Override
    public void onBackPressed() { /* Do nothing */ }

    private List<Choice> getCharacterTypeChoices() {
        List<Choice> choices = new ArrayList<>();
        choices.add(new Choice(R.string.so_character_type_fighter, new Runnable() {
            @Override
            public void run() {
                ((AdventureConfig) getData()).enablePage(SOSpellsPage.class, false);
            }
        }));
        choices.add(new Choice(R.string.so_character_type_sorcerer, new Runnable() {
            @Override
            public void run() {
                Property.SKILL.get().addToMaxWithFeedback(-2);
                ((AdventureConfig) getData()).enablePage(SOSpellsPage.class, true);
            }
        }));
        return choices;
    }
}
