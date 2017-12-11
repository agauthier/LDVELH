package com.home.ldvelh.ui.widget.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.home.ldvelh.R;
import com.home.ldvelh.model.item.CCEquipment;

public class CCEquipmentListView extends CustomListView<CCEquipment> {

    public CCEquipmentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initRowView(View row, CustomListViewItem<CCEquipment> customListViewItem) {
        setItemName(row, customListViewItem.getText());
        initTextView(row, R.id.strengthAttribute, customListViewItem.getItem().getStrength().getValue());
        initTextView(row, R.id.protectionAttribute, customListViewItem.getItem().getProtection().getValue());
        initEquippedCheckbox(row, customListViewItem.getItem());
        setRemoveButtonOnClickListener(row, customListViewItem.getItem());
    }

    @Override
    protected CCEquipment createItem(String name) {
        return new CCEquipment(name);
    }

    @Override
    protected CustomListViewItem<CCEquipment> createCustomListViewItem(CCEquipment item) {
        return new CustomListViewItem<>(this, item);
    }

    private void initTextView(View row, int viewResId, int value) {
        TextView textView = row.findViewById(viewResId);
        textView.setText(String.valueOf(value));
    }

    private void initEquippedCheckbox(final View row, final CCEquipment equipment) {
        CheckBox checkbox = row.findViewById(R.id.equipped);
        checkbox.setOnCheckedChangeListener(null);
        checkbox.setChecked(equipment.isEquipped());
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                equipment.setEquipped(isChecked);
                touch();
            }
        });
    }

    private void setRemoveButtonOnClickListener(final View row, final CCEquipment item) {
        ImageButton button = row.findViewById(R.id.remove);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                decrement(item);
            }
        });
    }
}
