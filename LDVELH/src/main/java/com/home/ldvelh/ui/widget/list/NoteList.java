package com.home.ldvelh.ui.widget.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.home.ldvelh.R;
import com.home.ldvelh.model.item.SimpleItem;

import static com.home.ldvelh.ui.widget.list.CustomListItem.Quantity.MAXIMUM_ONE;

public class NoteList extends CustomList<SimpleItem> {

    public NoteList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initRowView(View row, CustomListItem<SimpleItem> customListItem) {
        setRemoveButtonOnClickListener(row, customListItem);
    }

    @Override
    public CustomListItem<SimpleItem> createListItem(SimpleItem item) {
        return new CustomListItem<>(this, item, MAXIMUM_ONE);
    }

    private void setRemoveButtonOnClickListener(final View row, final CustomListItem customListItem) {
        ImageButton button = row.findViewById(R.id.remove);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                remove((SimpleItem) customListItem.getItem());
            }
        });
    }
}
