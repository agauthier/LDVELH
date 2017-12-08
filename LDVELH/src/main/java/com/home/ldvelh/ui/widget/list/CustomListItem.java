package com.home.ldvelh.ui.widget.list;

import android.view.View;

import com.home.ldvelh.model.item.Item;

import org.apache.commons.lang3.StringUtils;

import static com.home.ldvelh.commons.Constants.QTY_SEPARATOR;
import static com.home.ldvelh.ui.widget.list.CustomListItem.Quantity.NO_MAXIMUM;

public class CustomListItem<T extends Item> {

    public enum Quantity {MAXIMUM_ONE, NO_MAXIMUM}

    private final CustomList<T> parentList;
    private final T itemSample;
    private final Quantity maxQuantity;
    private int quantity;

    CustomListItem(CustomList<T> parentList, T itemSample) {
        this(parentList, itemSample, NO_MAXIMUM);
    }

    CustomListItem(CustomList<T> parentList, T itemSample, Quantity maxQuantity) {
        this.parentList = parentList;
        this.itemSample = itemSample;
        this.maxQuantity = maxQuantity;
        this.quantity = 1;
    }

    public void initView(View row) {
        parentList.initRowView(row, this);
    }

    public T getItem() {
        return itemSample;
    }

    public boolean add(T item) {
        if (itemSample.hasName(item.getName()) && maxQuantity == NO_MAXIMUM) {
            quantity++;
            return true;
        }
        return false;
    }

    public boolean remove(T item) {
        if (itemSample.hasName(item.getName())) {
            quantity--;
            return true;
        }
        return false;
    }

    public String getText() {
        return itemSample.getName() + (quantity == 1 ? StringUtils.EMPTY : QTY_SEPARATOR + quantity);
    }
}
