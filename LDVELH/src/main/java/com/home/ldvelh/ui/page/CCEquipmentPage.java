package com.home.ldvelh.ui.page;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.CCEquipment;
import com.home.ldvelh.ui.dialog.CCEquipmentEditor;

public class CCEquipmentPage extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_page_cc_equipment, container, false);

    }
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        ImageButton button = view.findViewById(R.id.addEquipment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CCEquipment newEquipment = new CCEquipment();
                final CCEquipmentEditor editor = new CCEquipmentEditor(getContext(), newEquipment);
                editor.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (editor.isItemSavable()) {
                            Property.CC_EQUIPMENT_LIST.get().add(newEquipment);
                        }
                    }
                });
                editor.show();
            }
        });
    }
}
