package com.home.ldvelh.ui.widget.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.home.ldvelh.R;
import com.home.ldvelh.model.item.SimpleItem;

public class NoIncrementItemListView extends CustomListView<SimpleItem> {

    public NoIncrementItemListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initRowView(View row, CustomListViewItem<SimpleItem> customListViewItem) {
        setItemName(row, customListViewItem.getText());
        setRemoveButtonOnClickListener(row, customListViewItem);
    }

    @Override
    protected SimpleItem createItem(String name) {
        return new SimpleItem(name);
    }

    @Override
    protected CustomListViewItem<SimpleItem> createCustomListViewItem(SimpleItem item) {
        return new CustomListViewItem<>(this, item);
    }

    private void setRemoveButtonOnClickListener(final View row, final CustomListViewItem<SimpleItem> customListViewItem) {
        ImageButton button = row.findViewById(R.id.remove);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                decrement(customListViewItem.getItem());
            }
        });
    }
}
