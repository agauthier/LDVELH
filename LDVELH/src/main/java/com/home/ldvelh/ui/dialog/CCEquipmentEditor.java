package com.home.ldvelh.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.CCEquipment;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.IntValueHolder.WatchType;
import com.home.ldvelh.model.value.ListValueHolder;
import com.home.ldvelh.ui.widget.CustomNumberPicker;

import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("unused")
public class CCEquipmentEditor extends Store<CCEquipment> {

    private String equipmentName = StringUtils.EMPTY;
    private final IntValueHolder strength = new IntValueHolder(Constants.BIG_NEGATIVE, Constants.BIG_POSITIVE, 0);
    private final IntValueHolder protection = new IntValueHolder(Constants.BIG_NEGATIVE, Constants.BIG_POSITIVE, 0);

    public CCEquipmentEditor(Context context) {
        this(context, null);
    }

    private CCEquipmentEditor(Context context, Object data) {
        super(context, data, R.layout.dialog_name_value_pair_editor, new IntValueHolder(0, Constants.BIG_POSITIVE, Constants.BIG_POSITIVE), Property.CC_EQUIPMENT_LIST.get());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEquipmentNameTextEdit();
        initView();
    }

    @Override
    public void dismiss() {
        Utils.hideKeyboard(findViewById(R.id.name));
        super.dismiss();
    }

    @Override
    protected void addToList(ShopPickerItem shopItem, ListValueHolder<CCEquipment> list) {}

    @Override
    protected void acquireItems() {
        CCEquipment newEquipment = list.add(equipmentName);
        newEquipment.setStrength(strength.getValue());
        newEquipment.setProtection(protection.getValue());
    }

    @Override
    boolean okButtonEnabled(IntValueHolder currency) {
        return !equipmentName.isEmpty();
    }

    public void initView() {
        super.initView();
        ((TextView) findViewById(R.id.name)).setHint(R.string.cc_equipment_name_caption);
        ((TextView) findViewById(R.id.value1Name)).setText(R.string.cc_strength);
        ((TextView) findViewById(R.id.value2Name)).setText(R.string.cc_protection);
        ((CustomNumberPicker) findViewById(R.id.numberPickerValue1)).init(strength, WatchType.VALUE);
        ((CustomNumberPicker) findViewById(R.id.numberPickerValue2)).init(protection, WatchType.VALUE);
    }

    private void initEquipmentNameTextEdit() {
        EditText nameEditText = findViewById(R.id.name);
        nameEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            completeAcquisition();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                equipmentName = s.toString();
                findViewById(R.id.okButton).setEnabled(okButtonEnabled(null));
            }
        });
    }
}
