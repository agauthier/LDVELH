package com.home.ldvelh.model.item;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.home.ldvelh.R;
import com.home.ldvelh.model.value.ListValueHolder;

import android.view.View;
import android.widget.TextView;

public abstract class Item implements ListItem {
	private static final long serialVersionUID = -7145542173208649480L;

	private final String name;
	private int quantity;
	private final List<Effect> effects = new ArrayList<>();
	private final Object data;
	private final ListValueHolder<? extends ListItem> list;

	protected Item() {
		this.name = StringUtils.EMPTY;
		this.quantity = 0;
		this.data = null;
		this.list = null;
	}

	protected <T extends ListItem> Item(ItemAndQuantity itemAndQty, List<Effect> effects, Object data, ListValueHolder<T> list) {
		this.name = itemAndQty.getName();
		this.quantity = itemAndQty.getQuantity();
		this.effects.addAll(effects);
		this.data = data;
		this.list = list;
	}

	@Override public String getName() {
		return name;
	}

	@Override public String getNameWithQty() {
		String fullName = name;
		if (quantity > 1) {
			fullName += ItemAndQuantity.QTY_SEPARATOR + " " + quantity;
		}
		return fullName;
	}

	@Override public int getQuantity() {
		return quantity;
	}

	@Override public List<Effect> getEffects() {
		return effects;
	}

	@Override public Object getData() {
		return data;
	}

	@Override public <T extends ListItem> ListValueHolder<T> getList() {
		@SuppressWarnings("unchecked") ListValueHolder<T> castedList = (ListValueHolder<T>) list;
		return castedList;
	}

	@Override public void increment() {
		quantity += 1;
	}

	@Override public void decrement() {
		quantity -= 1;
		quantity = (quantity < 0) ? 0 : quantity;
	}

	@Override public void add(int amount) {
		quantity += amount;
	}

	@Override public void subtract(int amount) {
		quantity -= amount;
		quantity = (quantity < 0) ? 0 : quantity;
	}

	@Override public boolean hasName(String name) {
		return this.name.equals(name);
	}

	@Override public void initView(View row) {
		TextView textView = row.findViewById(R.id.itemName);
		textView.setText(getNameWithQty());
	}
}
