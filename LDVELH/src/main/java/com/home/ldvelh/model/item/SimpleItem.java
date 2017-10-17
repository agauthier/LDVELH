package com.home.ldvelh.model.item;

import java.util.List;

import com.home.ldvelh.R;
import com.home.ldvelh.model.value.ListValueHolder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class SimpleItem extends Item {
	private static final long serialVersionUID = -7774443363852624332L;

	protected SimpleItem() {}

	protected <T extends ListItem> SimpleItem(ItemAndQuantity itemAndQty, List<Effect> effects, Object data, ListValueHolder<T> list) {
		super(itemAndQty, effects, data, list);
	}

	@Override public <T extends ListItem> T create(ItemAndQuantity itemAndQty, List<Effect> effects, Object data, ListValueHolder<T> list) {
		@SuppressWarnings("unchecked") T newItem = (T) new SimpleItem(itemAndQty, effects, data, list);
		return newItem;
	}

	@Override public void initView(View row) {
		super.initView(row);
		setAddButtonOnClickListener(row);
		setRemoveButtonOnClickListener(row);
		if (getEffects().isEmpty()) {
			row.findViewById(R.id.consumeItem).setVisibility(View.INVISIBLE);
		} else {
			row.findViewById(R.id.consumeItem).setVisibility(View.VISIBLE);
			setConsumeButtonsOnClickListener(row);
		}
	}

	private void setAddButtonOnClickListener(final View row) {
		ImageButton button = row.findViewById(R.id.addItem);
		button.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View view) {
				increment();
				getList().touch();
			}
		});
	}

	private void setRemoveButtonOnClickListener(final View row) {
		final SimpleItem currentItem = this;
		ImageButton button = row.findViewById(R.id.remove);
		button.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View view) {
				decrement();
				if (getQuantity() <= 0) {
					getList().remove(currentItem);
				} else {
					getList().touch();
				}
			}
		});
	}

	private void setConsumeButtonsOnClickListener(final View row) {
		final SimpleItem currentItem = this;
		ImageButton button = row.findViewById(R.id.consumeItem);
		button.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View view) {
				decrement();
				for (Effect effect : getEffects()) {
					effect.apply();
				}
				if (getQuantity() <= 0) {
					getList().remove(currentItem);
				} else {
					getList().touch();
				}
			}
		});
	}
}
