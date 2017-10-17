package com.home.ldvelh.model.item;

import java.util.List;

import com.home.ldvelh.R;
import com.home.ldvelh.model.value.ListValueHolder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Spell extends Item {
	private static final long serialVersionUID = 7470328920788639342L;

	private boolean canCastTwice = false;

	private Spell() {}

	private <T extends ListItem> Spell(ItemAndQuantity itemAndQty, List<Effect> effects, Object data, ListValueHolder<T> list) {
		super(itemAndQty, effects, data, list);
	}

	@Override public <T extends ListItem> T create(ItemAndQuantity itemAndQty, List<Effect> effects, Object data, ListValueHolder<T> list) {
		@SuppressWarnings("unchecked") T newItem = (T) new Spell(itemAndQty, effects, data, list);
		return newItem;
	}

	@Override public void initView(View row) {
		super.initView(row);
		ImageButton castSpellTwiceButton = row.findViewById(R.id.castSpellTwice);
		if (canCastTwice && getQuantity() > 1) {
			castSpellTwiceButton.setVisibility(View.VISIBLE);
			setButtonOnClickListener(castSpellTwiceButton, true);
		} else {
			castSpellTwiceButton.setVisibility(View.INVISIBLE);
		}
		setButtonOnClickListener((ImageButton) row.findViewById(R.id.castSpell), false);
	}

	public void canCastTwice() {
		this.canCastTwice = true;
	}

	private void setButtonOnClickListener(ImageButton button, final boolean castTwice) {
		final Spell currentItem = this;
		button.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View view) {
				subtract(castTwice ? 2 : 1);
				for (Effect effect : getEffects()) {
					if (castTwice) {
						effect.applyTwice();
					} else {
						effect.apply();
					}
				}
				if (getQuantity() <= 0) {
					getList().remove(currentItem);
				} else {
					getList().touch();
				}
			}
		});
	}
}
