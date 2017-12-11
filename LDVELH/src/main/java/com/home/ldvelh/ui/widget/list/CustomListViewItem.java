package com.home.ldvelh.ui.widget.list;

import android.view.View;

import com.home.ldvelh.model.item.Item;

import org.apache.commons.lang3.StringUtils;

import static com.home.ldvelh.commons.Constants.QTY_SEPARATOR;

public class CustomListViewItem<T extends Item> {

    private final CustomListView<T> parentList;
    private final T item;

    CustomListViewItem(CustomListView<T> parentList, T item) {
        this.parentList = parentList;
        this.item = item;
    }

    public void initView(View row) {
        parentList.initRowView(row, this);
    }

    public T getItem() {
        return item;
    }

    public String getText() {
        return item.getName() + (item.getQuantity() == 1 ? StringUtils.EMPTY : QTY_SEPARATOR + item.getQuantity());
    }
}
