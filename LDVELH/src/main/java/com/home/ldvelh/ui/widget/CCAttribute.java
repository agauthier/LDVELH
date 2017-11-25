package com.home.ldvelh.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.character.CCCharacter;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.IntValueHolder.WatchType;

import java.util.Observable;
import java.util.Observer;

public class CCAttribute extends LinearLayout implements Observer {

    private final String name;
    private final int textStyleResId;
    private final boolean withPicker;
    private final String propertyName;

    private IntValueHolder value = null;
    private IntValueHolder bonus = null;

    public CCAttribute(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CCAttribute, 0, 0);
        name = array.getString(R.styleable.CCAttribute_attributeName);
        textStyleResId = array.getResourceId(R.styleable.CCAttribute_textStyle, R.style.ldvelhTitles);
        withPicker = array.getBoolean(R.styleable.CCAttribute_withPicker, Boolean.TRUE);
        propertyName = array.getString(R.styleable.CCAttribute_propertyName);
        array.recycle();
        if (propertyName != null) {
            //noinspection ConstantConditions
            value = (IntValueHolder) Property.getPropertyByName(propertyName).get();
        }
        LayoutInflater.from(context).inflate(R.layout.widget_cc_attribute, this);
        initLayout();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (propertyName != null && !withPicker) {
            Property.CC_EQUIPMENT_LIST.get().addObserver(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (propertyName != null && !withPicker) {
            Property.CC_EQUIPMENT_LIST.get().deleteObserver(this);
        }
        super.onDetachedFromWindow();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (!withPicker) {
            setValueAndBonus();
        }
    }

    public void initEditableFighter(IntValueHolder strength, IntValueHolder bonus) {
        this.value = strength;
        this.bonus = bonus;
        setValueAndBonus();
    }

    private void initLayout() {
        TextView nameTextView = findViewById(R.id.attributeName);
        nameTextView.setText(name);
        nameTextView.setTextAppearance(textStyleResId);
        if (withPicker) {
            removeView(R.id.valueAndBonus);
            ((CustomNumberPicker) findViewById(R.id.numberPickerValue)).init(value, WatchType.VALUE);
        } else {
            removeView(R.id.numberPickerValue);
            if (value != null) {
                setValueAndBonus();
            }
        }
    }

    private void removeView(int viewId) {
        View viewToRemove = findViewById(viewId);
        ((ViewGroup) viewToRemove.getParent()).removeView(viewToRemove);
    }

    private void setValueAndBonus() {
        TextView textView = findViewById(R.id.valueAndBonus);
        textView.setText(propertyName == null ? getString(bonus.getValue()) : getString(((CCCharacter) Property.CHARACTER.get()).getEquipmentBonus(value)));
        textView.setTextAppearance(textStyleResId);
    }

    private String getString(int bonus) {
        if (bonus == 0) {
            return String.format(Utils.getString(R.string.cc_value), value.getValue());
        } else {
            return String.format(Utils.getString(R.string.cc_value_bonus), value.getValue(), bonus);
        }
    }
}
