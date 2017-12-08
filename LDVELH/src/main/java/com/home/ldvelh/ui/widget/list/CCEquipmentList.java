package com.home.ldvelh.ui.widget.list;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.CCEquipment;
import com.home.ldvelh.model.item.Item;
import com.home.ldvelh.ui.dialog.CCEquipmentEditor;

import static com.home.ldvelh.ui.widget.list.CustomListItem.Quantity.MAXIMUM_ONE;

public class CCEquipmentList extends CustomList<CCEquipment> {

    public CCEquipmentList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setAddButtonOnClickListener();
    }

    @Override
    public void initRowView(View row, CustomListItem<CCEquipment> customListItem) {
        initTextView(row, R.id.strengthAttribute, customListItem.getItem().getStrength());
        initTextView(row, R.id.protectionAttribute, customListItem.getItem().getProtection());
        setEquippedCheckboxOnCheckedListener(row, customListItem.getItem());
        setRemoveButtonOnClickListener(row, customListItem.getItem());
    }

    @Override
    public CustomListItem<CCEquipment> createListItem(CCEquipment item) {
        return new CustomListItem<>(this, item, MAXIMUM_ONE);
    }

    private void initTextView(View row, int viewResId, int value) {
        TextView textView = row.findViewById(viewResId);
        textView.setText(String.valueOf(value));
    }

    private void setEquippedCheckboxOnCheckedListener(final View row, final CCEquipment equipment) {
        CheckBox checkbox = row.findViewById(R.id.equipped);
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
                remove(item);
            }
        });
    }

    private void setAddButtonOnClickListener() {
        ImageButton button = findViewById(R.id.addEquipment);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final CCEquipment newEquipment = Item.create(CCEquipment.class);
                CCEquipmentEditor editor = new CCEquipmentEditor(getContext(), newEquipment);
                editor.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (!newEquipment.getName().isEmpty()) {
                            Property.CC_EQUIPMENT_LIST.get().add(newEquipment);
                        }
                    }
                });
                editor.show();
            }
        });
    }
}
