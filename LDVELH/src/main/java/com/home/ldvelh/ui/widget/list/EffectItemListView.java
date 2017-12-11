package com.home.ldvelh.ui.widget.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.home.ldvelh.R;
import com.home.ldvelh.model.item.EffectItem;

public class EffectItemListView extends CustomListView<EffectItem> {

    public EffectItemListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initRowView(View row, CustomListViewItem<EffectItem> customListViewItem) {
        setItemName(row, customListViewItem.getText());
        setAddButtonOnClickListener(row, customListViewItem);
        setRemoveButtonOnClickListener(row, customListViewItem);
        if (customListViewItem.getItem().hasEffects()) {
            row.findViewById(R.id.consumeItem).setVisibility(View.VISIBLE);
            setConsumeButtonsOnClickListener(row, customListViewItem);
        } else {
            row.findViewById(R.id.consumeItem).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected EffectItem createItem(String name) {
        return new EffectItem(name);
    }

    @Override
    protected CustomListViewItem<EffectItem> createCustomListViewItem(EffectItem item) {
        return new CustomListViewItem<>(this, item);
    }

    private void setAddButtonOnClickListener(final View row, final CustomListViewItem<EffectItem> customListViewItem) {
        ImageButton button = row.findViewById(R.id.addItem);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                increment(customListViewItem.getItem());
            }
        });
    }

    private void setRemoveButtonOnClickListener(final View row, final CustomListViewItem<EffectItem> customListViewItem) {
        ImageButton button = row.findViewById(R.id.remove);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                decrement(customListViewItem.getItem());
            }
        });
    }

    private void setConsumeButtonsOnClickListener(final View row, final CustomListViewItem<EffectItem> customListViewItem) {
        ImageButton button = row.findViewById(R.id.consumeItem);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                customListViewItem.getItem().applyEffects();
                decrement(customListViewItem.getItem());
            }
        });
    }
}
