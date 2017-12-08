package com.home.ldvelh.ui.dialog;

import android.content.Context;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.SimpleItem;
import com.home.ldvelh.model.value.ListValueHolder;

public class DF03MagicItemPicker extends Store<SimpleItem> {

    @SuppressWarnings("unused")
    public DF03MagicItemPicker(Context context) {
        super(context, null, R.layout.dialog_df03_magic_item_picker, Property.GOLD.get(), Property.MAGIC_ITEM_LIST.get());
    }

    @Override
    public void onBackPressed() { /* Do nothing */ }

    @Override
    protected void addToList(ShopPickerItem shopItem, ListValueHolder<SimpleItem> list) {
        for (int i = 0; i < shopItem.getQuantity(); i++) {
            list.addNewItem(shopItem.getName());
        }
    }
}