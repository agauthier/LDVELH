package com.home.ldvelh.model.item;

import java.util.List;

import com.home.ldvelh.R;
import com.home.ldvelh.model.value.ListValueHolder;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class CCEquipment extends Item {
	private static final long serialVersionUID = -4360888170458662309L;

	private int strength = 0;
	private int protection = 0;
	private boolean equipped = false;

	private CCEquipment() {}

	private <T extends ListItem> CCEquipment(ItemAndQuantity itemAndQty, List<Effect> effects, Object data, ListValueHolder<T> list) {
		super(itemAndQty, effects, data, list);
	}

	@Override public <T extends ListItem> T create(ItemAndQuantity itemAndQty, List<Effect> effects, Object data, ListValueHolder<T> list) {
		@SuppressWarnings("unchecked") T newItem = (T) new CCEquipment(itemAndQty, effects, data, list);
		return newItem;
	}

	@Override public void increment() {}

	@Override public void decrement() {}

	@Override public void add(int quantity) {}

	@Override public void subtract(int amount) {}

	@Override public boolean hasName(String name) {
		return false;
	}

	@Override public void initView(View row) {
		super.initView(row);
		initTextView(row, R.id.strength, strength);
		initTextView(row, R.id.protection, protection);
		setRemoveButtonOnClickListener(row);
		setEquippedCheckboxOnCheckedListener(row);
	}

	private void initTextView(View row, int viewResId, int value) {
		TextView textView = row.findViewById(viewResId);
		textView.setText(String.valueOf(value));
		textView.setGravity(Gravity.CENTER);
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getProtection() {
		return protection;
	}

	public void setProtection(int protection) {
		this.protection = protection;
	}

	public boolean isEquipped() {
		return equipped;
	}

	public void setEquipped(boolean equipped) {
		this.equipped = equipped;
	}

	private void setEquippedCheckboxOnCheckedListener(final View row) {
		CheckBox checkbox = row.findViewById(R.id.equipped);
		checkbox.setChecked(equipped);
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				setEquipped(isChecked);
				getList().touch();
			}
		});
	}

	private void setRemoveButtonOnClickListener(final View row) {
		final CCEquipment currentItem = this;
		ImageButton button = row.findViewById(R.id.remove);
		button.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View view) {
				getList().remove(currentItem);
			}
		});
	}
}
