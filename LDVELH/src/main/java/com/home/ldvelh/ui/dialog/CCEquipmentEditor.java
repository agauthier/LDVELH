package com.home.ldvelh.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.CCEquipment;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.ListValueHolder;
import com.home.ldvelh.ui.widget.CustomNumberPicker;
import com.home.ldvelh.ui.widget.CustomNumberPicker.WatchType;

import org.apache.commons.lang3.StringUtils;

public class CCEquipmentEditor extends Store<CCEquipment> {

	private String equipmentName = StringUtils.EMPTY;
	private final IntValueHolder strength = new IntValueHolder(0, Constants.BIG_POSITIVE, 0);
	private final IntValueHolder protection = new IntValueHolder(0, Constants.BIG_POSITIVE, 0);

	public CCEquipmentEditor(Context context) {
		this(context, null);
	}

	private CCEquipmentEditor(Context context, Object data) {
		super(context, data, R.layout.dialog_cc_equipment_editor, new IntValueHolder(0, Constants.BIG_POSITIVE, Constants.BIG_POSITIVE), Property.CC_EQUIPMENT_LIST.get());
	}

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initEquipmentNameTextEdit();
		initPickers();
	}

	@Override public void dismiss() {
		hideKeyboard();
		super.dismiss();
	}

	@Override protected void addToList(ShopPickerItem shopItem, ListValueHolder<CCEquipment> list) {}

	@Override protected void acquireItems() {
		CCEquipment newEquipment = list.add(equipmentName);
		newEquipment.setStrength(strength.getValue());
		newEquipment.setProtection(protection.getValue());
	}

	@Override boolean okButtonEnabled(IntValueHolder currency) {
		return !equipmentName.isEmpty();
	}

	private void initEquipmentNameTextEdit() {
		EditText nameEditText = findViewById(R.id.equipmentName);
		nameEditText.addTextChangedListener(new TextWatcher() {
			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override public void afterTextChanged(Editable s) {
				equipmentName = s.toString();
				findViewById(R.id.okButton).setEnabled(okButtonEnabled(null));
			}
		});
	}

	private void initPickers() {
		CustomNumberPicker strengthPicker = findViewById(R.id.numberPickerStrength);
		strengthPicker.init(strength, WatchType.VALUE);
		CustomNumberPicker protectionPicker = findViewById(R.id.numberPickerProtection);
		protectionPicker.init(protection, WatchType.VALUE);
	}

	private void hideKeyboard() {
		EditText nameEditText = findViewById(R.id.equipmentName);
		InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(nameEditText.getWindowToken(), 0);
		nameEditText.clearFocus();
	}
}
