package com.home.ldvelh.ui.dialog;

import java.util.Collections;
import java.util.Observer;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.Effect;
import com.home.ldvelh.model.item.EffectTarget;
import com.home.ldvelh.model.item.ItemAndQuantity;
import com.home.ldvelh.model.item.Spell;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.ListValueHolder;

import android.content.Context;

public class DF02SpellPicker extends Store<Spell> implements Observer {

    public DF02SpellPicker(Context context) {
        this(context, null);
    }

    @SuppressWarnings("SameParameterValue")
    public DF02SpellPicker(Context context, Object data) {
        super(context, data, R.layout.dialog_df02_spell_picker, Property.MAGIC.get(), Property.SPELL_LIST.get());
    }

    @Override
    public void onBackPressed() { /* Do nothing */ }

    @Override
    protected void addToList(ShopPickerItem shopItem, ListValueHolder<Spell> list) {
        if (shopItem.getQuantity() > 0) {
            ItemAndQuantity itemAndQty = new ItemAndQuantity(shopItem.getName(), shopItem.getQuantity());
            switch (shopItem.getId()) {
                case R.id.df02SpellSkill:
                    Spell skillSpell = list.add(itemAndQty, Collections.singletonList(new Effect(EffectTarget.SKILL, Effect.HALF_OF_MAX_ROUNDED_DOWN)));
                    skillSpell.canCastTwice();
                    break;
                case R.id.df02SpellStamina:
                    Spell staminaSpell = list.add(itemAndQty, Collections.singletonList(new Effect(EffectTarget.STAMINA, Effect.HALF_OF_MAX_ROUNDED_DOWN)));
                    staminaSpell.canCastTwice();
                    break;
                case R.id.df02SpellLuck:
                    Spell luckSpell = list.add(itemAndQty, Collections.singletonList(new Effect(EffectTarget.LUCK, Effect.HALF_OF_MAX_ROUNDED_DOWN)));
                    luckSpell.canCastTwice();
                    break;
                default:
                    list.add(itemAndQty);
                    break;
            }
        }
    }

    @Override
    protected boolean okButtonEnabled(IntValueHolder currency) {
        return currency.getValue() <= 0;
    }
}
