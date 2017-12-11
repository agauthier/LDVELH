package com.home.ldvelh.ui.dialog;

import android.content.Context;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.SimpleItem;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.ListValueHolder;

import java.util.Observable;

public class SOIngredientPicker extends Store<SimpleItem> {

    public SOIngredientPicker(Context context) {
        this(context, null);
    }

    @SuppressWarnings({"WeakerAccess", "SameParameterValue"})
    public SOIngredientPicker(Context context, Object data) {
        super(context, data, R.layout.dialog_so_ingredient_picker, new IntValueHolder(0, Constants.BIG_POSITIVE, Constants.BIG_POSITIVE), Property.INGREDIENT_LIST.get(), true);
    }

    @Override
    public void update(Observable observable, Object data) {}

    @Override
    protected void addToList(ShopPickerItem shopItem, ListValueHolder<SimpleItem> list) {
        if (shopItem.getQuantity() > 0) {
            list.add(new SimpleItem(shopItem.getName(), shopItem.getQuantity()));
        }
    }
}
