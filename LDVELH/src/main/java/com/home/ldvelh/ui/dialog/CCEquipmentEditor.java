package com.home.ldvelh.ui.dialog;

import android.content.Context;
import android.widget.TextView;

import com.home.ldvelh.R;
import com.home.ldvelh.model.item.CCEquipment;
import com.home.ldvelh.model.value.IntValueHolder.WatchType;
import com.home.ldvelh.ui.widget.CustomNumberPicker;

public class CCEquipmentEditor extends NameEditor<CCEquipment> {

    public CCEquipmentEditor(Context context, CCEquipment equipment) {
        super(context, equipment, R.layout.dialog_name_value_pair_editor, R.string.cc_equipment_name_caption);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getNamable().getStrength().addObserver(this);
        getNamable().getProtection().addObserver(this);
    }

    @Override
    public void onDetachedFromWindow() {
        getNamable().getStrength().deleteObserver(this);
        getNamable().getProtection().deleteObserver(this);
        super.onDetachedFromWindow();
    }

    @Override
    void initView() {
        super.initView();
        ((TextView) findViewById(R.id.value1Name)).setText(R.string.cc_strength);
        ((TextView) findViewById(R.id.value2Name)).setText(R.string.cc_protection);
        ((CustomNumberPicker) findViewById(R.id.numberPickerValue1)).init(getNamable().getStrength(), WatchType.VALUE);
        ((CustomNumberPicker) findViewById(R.id.numberPickerValue2)).init(getNamable().getProtection(), WatchType.VALUE);
    }

    @Override
    public void cancelButtonPressed() {
        setOnDismissListener(null);
        dismiss();
    }

    @Override
    public boolean isItemSavable() {
        return super.isItemSavable() && (getNamable().getStrength().getValue() != 0 || getNamable().getProtection().getValue() != 0);
    }
}
