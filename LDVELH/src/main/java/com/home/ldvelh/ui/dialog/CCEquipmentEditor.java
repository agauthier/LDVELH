package com.home.ldvelh.ui.dialog;

import android.content.Context;
import android.widget.TextView;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.item.CCEquipment;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.IntValueHolder.WatchType;
import com.home.ldvelh.ui.widget.CustomNumberPicker;

@SuppressWarnings("unused")
public class CCEquipmentEditor extends NameEditor<CCEquipment> {

    private final IntValueHolder strength = new IntValueHolder(Constants.BIG_NEGATIVE, Constants.BIG_POSITIVE, 0);
    private final IntValueHolder protection = new IntValueHolder(Constants.BIG_NEGATIVE, Constants.BIG_POSITIVE, 0);

    public CCEquipmentEditor(Context context, CCEquipment equipment) {
        super(context, equipment, R.layout.dialog_name_value_pair_editor, R.string.cc_equipment_name_caption);
    }

    @Override
    void initView() {
        super.initView();
        ((TextView) findViewById(R.id.value1Name)).setText(R.string.cc_strength);
        ((TextView) findViewById(R.id.value2Name)).setText(R.string.cc_protection);
        ((CustomNumberPicker) findViewById(R.id.numberPickerValue1)).init(strength, WatchType.VALUE);
        ((CustomNumberPicker) findViewById(R.id.numberPickerValue2)).init(protection, WatchType.VALUE);
    }

    @Override
    protected boolean isOkButtonEnabled(String name) {
        return !name.isEmpty() && (strength.getValue() != 0 || protection.getValue() != 0);
    }
}
