package com.home.ldvelh.ui.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.IntValueHolder.WatchType;
import com.home.ldvelh.ui.widget.CustomNumberPicker;

import java.util.Observer;

public class ShopPickerItem extends LinearLayout {

    private static final int DEFAULT_PRICE = 1;

    private final String itemName;
    private final int price;
    private final IntValueHolder value;

    public ShopPickerItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.dialog_shop_picker_item, this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ShopPickerItem, 0, 0);
        itemName = array.getString(R.styleable.ShopPickerItem_itemName);
        ((TextView) findViewById(R.id.itemName)).setText(itemName);
        price = array.getInt(R.styleable.ShopPickerItem_price, DEFAULT_PRICE);
        if (price != DEFAULT_PRICE) {
            ((TextView) findViewById(R.id.price)).setText(String.format(Utils.getString(R.string.gold_amount), price));
        }
        array.recycle();
        value = new IntValueHolder(0, 0, 0);
    }

    public void initialize(int initValue, int maxValue) {
        setMax(maxValue);
        setQuantity(initValue);
        CustomNumberPicker picker = findViewById(R.id.itemPicker);
        picker.init(value, WatchType.VALUE);
    }

    public void addObserver(Observer observer) {
        value.addObserver(observer);
    }

    public void deleteObserver(Observer observer) {
        value.deleteObserver(observer);
    }

    public boolean hasValue(IntValueHolder value) {
        return this.value == value;
    }

    public String getName() {
        return itemName;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return value.getValue();
    }

    public void setMax(int maxValue) {
        value.addToMax(maxValue - value.getMax());
    }

    public void setQuantity(int quantity) {
        value.add(quantity - value.getValue());
    }
}
