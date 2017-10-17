package com.home.ldvelh.model.item;

import java.io.Serializable;
import java.util.List;

import com.home.ldvelh.model.value.ListValueHolder;

import android.view.View;

@SuppressWarnings("unused")
public interface ListItem extends Serializable {

	<T extends ListItem> T create(ItemAndQuantity itemAndQty, List<Effect> effects, Object data, ListValueHolder<T> list);

	String getName();

	String getNameWithQty();

	int getQuantity();

	List<Effect> getEffects();

	Object getData();

	<T extends ListItem> ListValueHolder<T> getList();

	void increment();

	void decrement();

	void add(int amount);

	void subtract(int amount);

	boolean hasName(String name);

	void initView(View row);
}
