package com.home.ldvelh.ui.dialog;

import android.content.Context;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.DF02Spell;
import com.home.ldvelh.model.item.Effect;
import com.home.ldvelh.model.item.EffectTarget;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.ListValueHolder;

import java.util.Collections;
import java.util.Observer;

public class DF02SpellPicker extends Store<DF02Spell> implements Observer {

    public DF02SpellPicker(Context context) {
        this(context, null);
    }

    @SuppressWarnings({"WeakerAccess", "SameParameterValue"})
    public DF02SpellPicker(Context context, Object data) {
        super(context, data, R.layout.dialog_df02_spell_picker, Property.MAGIC.get(), Property.SPELL_LIST.get());
    }

    @Override
    public void onBackPressed() { /* Do nothing */ }

    @Override
    protected void addToList(ShopPickerItem shopItem, ListValueHolder<DF02Spell> list) {
        if (shopItem.getQuantity() > 0) {
            switch (shopItem.getId()) {
                case R.id.df02SpellSkill:
                    list.add(new DF02Spell(shopItem.getName(), shopItem.getQuantity(), Collections.singletonList(new Effect(EffectTarget.SKILL, Effect.HALF_OF_MAX_ROUNDED_DOWN))));
                    break;
                case R.id.df02SpellStamina:
                    list.add(new DF02Spell(shopItem.getName(), shopItem.getQuantity(), Collections.singletonList(new Effect(EffectTarget.STAMINA, Effect.HALF_OF_MAX_ROUNDED_DOWN))));
                    break;
                case R.id.df02SpellLuck:
                    list.add(new DF02Spell(shopItem.getName(), shopItem.getQuantity(), Collections.singletonList(new Effect(EffectTarget.LUCK, Effect.HALF_OF_MAX_ROUNDED_DOWN))));
                    break;
                default:
                    list.add(new DF02Spell(shopItem.getName(), shopItem.getQuantity(), Collections.<Effect>emptyList()));
                    break;
            }
        }
    }

    @Override
    protected boolean okButtonEnabled(IntValueHolder currency) {
        return currency.getValue() <= 0;
    }
}
