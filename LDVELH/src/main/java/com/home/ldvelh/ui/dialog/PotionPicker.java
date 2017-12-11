package com.home.ldvelh.ui.dialog;

import android.content.Context;
import android.util.Pair;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.Effect;
import com.home.ldvelh.model.item.EffectItem;
import com.home.ldvelh.model.item.EffectTarget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PotionPicker extends MultipleChoiceDialog {

    public PotionPicker(Context context, Object data) {
        super(context, data);
    }

    @Override
    protected Pair<Integer, List<Choice>> getChoices() {
        return new Pair<>(R.string.potion_choice, getPotionChoices());
    }

    @Override
    public void onBackPressed() { /* Do nothing */ }

    private List<Choice> getPotionChoices() {
        List<Choice> choices = new ArrayList<>();
        choices.add(new Choice(R.string.skill_potion, new Runnable() {
            @Override
            public void run() {
                acquireItem(R.string.skill_potion, Collections.singletonList(new Effect(EffectTarget.SKILL, Effect.MAX)));
            }
        }));
        choices.add(new Choice(R.string.stamina_potion, new Runnable() {
            @Override
            public void run() {
                acquireItem(R.string.stamina_potion, Collections.singletonList(new Effect(EffectTarget.STAMINA, Effect.MAX)));
            }
        }));
        choices.add(new Choice(R.string.luck_potion, new Runnable() {
            @Override
            public void run() {
                List<Effect> effects = new ArrayList<>();
                effects.add(new Effect(EffectTarget.MAX_LUCK, 1));
                effects.add(new Effect(EffectTarget.LUCK, Effect.MAX));
                acquireItem(R.string.luck_potion, effects);
            }
        }));
        return choices;
    }

    private void acquireItem(int potionResId, List<Effect> effects) {
        int doses = (getData() == null) ? 1 : (Integer) getData();
        Property.ITEM_LIST.get().add(new EffectItem(Utils.getString(potionResId), doses, effects));
    }
}
