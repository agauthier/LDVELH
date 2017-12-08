package com.home.ldvelh.ui.dialog;

import android.content.Context;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.Effect;
import com.home.ldvelh.model.item.EffectTarget;
import com.home.ldvelh.model.item.Spell;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.ListValueHolder;

import java.util.Collections;
import java.util.Observer;

public class DF02SpellPicker extends Store<Spell> implements Observer {

    public DF02SpellPicker(Context context) {
        super(context, null, R.layout.dialog_df02_spell_picker, Property.MAGIC.get(), Property.SPELL_LIST.get());
    }

    @Override
    public void onBackPressed() { /* Do nothing */ }

    @Override
    protected void addToList(ShopPickerItem shopItem, ListValueHolder<Spell> list) {
        if (shopItem.getQuantity() > 0) {
            switch (shopItem.getId()) {
                case R.id.df02SpellSkill:
                    Spell skillSpell = list.addNewItem(shopItem.getName(), Collections.singletonList(new Effect(EffectTarget.SKILL, Effect.HALF_OF_MAX_ROUNDED_DOWN)));
                    skillSpell.addCharges(shopItem.getQuantity());
                    skillSpell.allowDoubleCast();
                    break;
                case R.id.df02SpellStamina:
                    Spell staminaSpell = list.addNewItem(shopItem.getName(), Collections.singletonList(new Effect(EffectTarget.STAMINA, Effect.HALF_OF_MAX_ROUNDED_DOWN)));
                    staminaSpell.canCastTwice();
                    staminaSpell.addCharges(shopItem.getQuantity());
                    staminaSpell.allowDoubleCast();
                    break;
                case R.id.df02SpellLuck:
                    Spell luckSpell = list.addNewItem(shopItem.getName(), Collections.singletonList(new Effect(EffectTarget.LUCK, Effect.HALF_OF_MAX_ROUNDED_DOWN)));
                    luckSpell.addCharges(shopItem.getQuantity());
                    luckSpell.allowDoubleCast();
                    break;
                default:
                    list.addNewItem(shopItem.getName());
                    break;
            }
        }
    }

    @Override
    protected boolean okButtonEnabled(IntValueHolder currency) {
        return currency.getValue() <= 0;
    }
}
