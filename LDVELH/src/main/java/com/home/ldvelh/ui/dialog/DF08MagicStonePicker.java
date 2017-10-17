package com.home.ldvelh.ui.dialog;

import java.util.Collections;
import java.util.Observable;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.Effect;
import com.home.ldvelh.model.item.EffectTarget;
import com.home.ldvelh.model.item.ItemAndQuantity;
import com.home.ldvelh.model.item.SimpleItem;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.ListValueHolder;

import android.content.Context;

public class DF08MagicStonePicker extends Store<SimpleItem> {

	public DF08MagicStonePicker(Context context) {
		this(context, null);
	}

	public DF08MagicStonePicker(Context context, Object data) {
		super(context, data, R.layout.dialog_df08_magic_stone_picker, new IntValueHolder(0, Constants.BIG_POSITIVE, Constants.BIG_POSITIVE), Property.STONE_LIST.get());
	}

	@Override public void update(Observable observable, Object data) {}

	@Override protected void addToList(ShopPickerItem shopItem, ListValueHolder<SimpleItem> list) {
		if (shopItem.getQuantity() > 0) {
			ItemAndQuantity itemAndQty = new ItemAndQuantity(shopItem.getName(), shopItem.getQuantity());
			switch (shopItem.getId()) {
			case R.id.df08StoneSkill:
				list.add(itemAndQty, Collections.singletonList(new Effect(EffectTarget.SKILL, Effect.HALF_OF_MAX_ROUNDED_UP)));
				break;
			case R.id.df08StoneStamina:
				list.add(itemAndQty, Collections.singletonList(new Effect(EffectTarget.STAMINA, Effect.HALF_OF_MAX_ROUNDED_UP)));
				break;
			case R.id.df08StoneLuck:
				list.add(itemAndQty, Collections.singletonList(new Effect(EffectTarget.LUCK, Effect.HALF_OF_MAX_ROUNDED_UP)));
				break;
			default:
				list.add(itemAndQty);
				break;
			}
		}
	}
}
