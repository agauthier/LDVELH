package com.home.ldvelh.ui.widget.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.home.ldvelh.R;
import com.home.ldvelh.model.item.Spell;

import static com.home.ldvelh.ui.widget.list.CustomListItem.Quantity.MAXIMUM_ONE;

public class SpellList extends CustomList<Spell> {

    public SpellList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initRowView(View row, CustomListItem<Spell> customListItem) {
        ImageButton castSpellTwiceButton = row.findViewById(R.id.castSpellTwice);
        Spell spell = customListItem.getItem();
        if (spell.canCastTwice()) {
            castSpellTwiceButton.setVisibility(View.VISIBLE);
            setButtonOnClickListener(castSpellTwiceButton, spell, true);
        } else {
            castSpellTwiceButton.setVisibility(View.INVISIBLE);
        }
        setButtonOnClickListener((ImageButton) row.findViewById(R.id.castSpell), spell, false);
    }

    @Override
    public CustomListItem<Spell> createListItem(Spell item) {
        return new CustomListItem<>(this, item, MAXIMUM_ONE);
    }

    private void setButtonOnClickListener(ImageButton button, final Spell spell, final boolean castTwice) {
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                spell.removeCharges(castTwice ? 2 : 1);
                if (castTwice) {
                    spell.applyEffectsTwice();
                } else {
                    spell.applyEffects();
                }
                if (spell.hasNoChargesLeft()) {
                    remove(spell);
                }
            }
        });
    }
}
