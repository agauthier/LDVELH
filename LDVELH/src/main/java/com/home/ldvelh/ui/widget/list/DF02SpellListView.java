package com.home.ldvelh.ui.widget.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.home.ldvelh.R;
import com.home.ldvelh.model.item.DF02Spell;

public class DF02SpellListView extends CustomListView<DF02Spell> {

    public DF02SpellListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initRowView(View row, CustomListViewItem<DF02Spell> customListViewItem) {
        setItemName(row, customListViewItem.getText());
        ImageButton castSpellTwiceButton = row.findViewById(R.id.castSpellTwice);
        DF02Spell spell = customListViewItem.getItem();
        if (spell.canCastTwice()) {
            castSpellTwiceButton.setVisibility(View.VISIBLE);
            setButtonOnClickListener(castSpellTwiceButton, spell, true);
        } else {
            castSpellTwiceButton.setVisibility(View.INVISIBLE);
        }
        setButtonOnClickListener((ImageButton) row.findViewById(R.id.castSpell), spell, false);
    }

    @Override
    protected DF02Spell createItem(String name) {
        return new DF02Spell(name);
    }

    @Override
    protected CustomListViewItem<DF02Spell> createCustomListViewItem(DF02Spell item) {
        return new CustomListViewItem<>(this, item);
    }

    private void setButtonOnClickListener(ImageButton button, final DF02Spell spell, final boolean castTwice) {
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (castTwice) {
                    decrement(spell);
                    decrement(spell);
                    spell.applyEffectsTwice();
                } else {
                    decrement(spell);
                    spell.applyEffects();
                }
            }
        });
    }
}
