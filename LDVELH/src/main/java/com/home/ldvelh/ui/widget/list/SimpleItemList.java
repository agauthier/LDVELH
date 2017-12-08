package com.home.ldvelh.ui.widget.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.home.ldvelh.R;
import com.home.ldvelh.model.item.SimpleItem;

public class SimpleItemList extends CustomList<SimpleItem> {

    public SimpleItemList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initRowView(View row, CustomListItem<SimpleItem> customListItem) {
        setAddButtonOnClickListener(row, customListItem);
        setRemoveButtonOnClickListener(row, customListItem);
        if (customListItem.getItem().hasEffects()) {
            row.findViewById(R.id.consumeItem).setVisibility(View.VISIBLE);
            setConsumeButtonsOnClickListener(row, customListItem);
        } else {
            row.findViewById(R.id.consumeItem).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public CustomListItem<SimpleItem> createListItem(SimpleItem item) {
        return new CustomListItem<>(this, item);
    }

    private void setAddButtonOnClickListener(final View row, final CustomListItem<SimpleItem> customListItem) {
        ImageButton button = row.findViewById(R.id.addItem);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                add(customListItem.getItem());
            }
        });
    }

    private void setRemoveButtonOnClickListener(final View row, final CustomListItem<SimpleItem> customListItem) {
        ImageButton button = row.findViewById(R.id.remove);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(customListItem.getItem());
            }
        });
    }

    private void setConsumeButtonsOnClickListener(final View row, final CustomListItem<SimpleItem> customListItem) {
        ImageButton button = row.findViewById(R.id.consumeItem);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                customListItem.getItem().applyEffects();
                remove(customListItem.getItem());
            }
        });
    }
}
