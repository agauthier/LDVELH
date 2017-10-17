package com.home.ldvelh.ui.dialog;

import java.util.Observable;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.ItemAndQuantity;
import com.home.ldvelh.model.item.SimpleItem;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.ListValueHolder;

import android.content.Context;

public class SOIngredientPicker extends Store<SimpleItem> {

	public SOIngredientPicker(Context context) {
		super(context, null, R.layout.dialog_so_ingredient_picker, new IntValueHolder(0, Constants.BIG_POSITIVE, Constants.BIG_POSITIVE), Property.INGREDIENT_LIST.get(), true);
	}

	@Override public void update(Observable observable, Object data) {}

	@Override protected void addToList(ShopPickerItem shopItem, ListValueHolder<SimpleItem> list) {
		list.add(new ItemAndQuantity(shopItem.getName(), shopItem.getQuantity()));
	}
}
