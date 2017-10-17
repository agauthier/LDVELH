package com.home.ldvelh.model.item;

import java.util.List;

import com.home.ldvelh.R;
import com.home.ldvelh.model.value.ListValueHolder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Note extends Item {
	private static final long serialVersionUID = -709116404688838779L;

	private Note() {}

	private <T extends ListItem> Note(ItemAndQuantity itemAndQty, List<Effect> effects, Object data, ListValueHolder<T> list) {
		super(itemAndQty, effects, data, list);
	}

	@Override public <T extends ListItem> T create(ItemAndQuantity itemAndQty, List<Effect> effects, Object data, ListValueHolder<T> list) {
		@SuppressWarnings("unchecked") T newItem = (T) new Note(itemAndQty, effects, data, list);
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
		setRemoveButtonOnClickListener(row);
	}

	private void setRemoveButtonOnClickListener(final View row) {
		final Note currentItem = this;
		ImageButton button = row.findViewById(R.id.remove);
		button.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View view) {
				getList().remove(currentItem);
			}
		});
	}
}
