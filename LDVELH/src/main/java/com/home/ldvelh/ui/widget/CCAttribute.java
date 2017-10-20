package com.home.ldvelh.ui.widget;

import java.util.Observable;
import java.util.Observer;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.character.CCCharacter;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.ui.widget.CustomNumberPicker.WatchType;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CCAttribute extends LinearLayout implements Observer {

    private final String name;
    private final IntValueHolder value;
    private final boolean withPicker;

    @SuppressWarnings("ConstantConditions")
    public CCAttribute(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CCAttributeOptions, 0, 0);
        name = array.getString(R.styleable.CCAttributeOptions_attributeName);
        value = (IntValueHolder) Property.getPropertyByName(array.getString(R.styleable.CCAttributeOptions_propertyName)).get();
        withPicker = array.getBoolean(R.styleable.CCAttributeOptions_withPicker, Boolean.TRUE);
        array.recycle();
        LayoutInflater.from(context).inflate(R.layout.widget_cc_attribute, this);
        initLayout();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!withPicker) {
            Property.CC_EQUIPMENT_LIST.get().addObserver(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (!withPicker) {
            Property.CC_EQUIPMENT_LIST.get().deleteObserver(this);
        }
        super.onDetachedFromWindow();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (!withPicker) {
            setValueBonusTotal();
        }
    }

    private void initLayout() {
        TextView nameTextView = findViewById(R.id.attributeName);
        nameTextView.setText(name);
        if (withPicker) {
            removeView(R.id.valueBonusTotal);
            ((CustomNumberPicker) findViewById(R.id.numberPickerValue)).init(value, WatchType.VALUE);
        } else {
            removeView(R.id.numberPickerValue);
            setValueBonusTotal();
        }
    }

    private void removeView(int viewId) {
        View viewToRemove = findViewById(viewId);
        ((ViewGroup) viewToRemove.getParent()).removeView(viewToRemove);
    }

    private void setValueBonusTotal() {
        int bonus = ((CCCharacter) Property.CHARACTER.get()).getEquipmentBonus(value);
        TextView view = findViewById(R.id.valueBonusTotal);
        view.setText(String.format(Utils.getString(R.string.cc_value_bonus_total), value.getValue(), bonus, value.getValue() + bonus));
    }
}
