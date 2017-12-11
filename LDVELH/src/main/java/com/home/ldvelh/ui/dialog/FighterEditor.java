package com.home.ldvelh.ui.dialog;

import android.content.Context;
import android.widget.TextView;

import com.home.ldvelh.R;
import com.home.ldvelh.model.combat.CombatCore;
import com.home.ldvelh.model.combat.EditableFighter;
import com.home.ldvelh.model.value.IntValueHolder.WatchType;
import com.home.ldvelh.ui.widget.CustomNumberPicker;

public class FighterEditor extends NameEditor<EditableFighter> {

    public FighterEditor(Context context, EditableFighter fighter) {
        super(context, fighter, R.layout.dialog_name_value_pair_editor, R.string.fighter_name);
    }

    @Override
    void initView() {
        super.initView();
        ((TextView) findViewById(R.id.value1Name)).setText(getNamable().getEditableValue1NameResId());
        ((TextView) findViewById(R.id.value2Name)).setText(getNamable().getEditableValue2NameResId());
        ((CustomNumberPicker) findViewById(R.id.numberPickerValue1)).init(getNamable().getEditableValue1(), WatchType.VALUE);
        ((CustomNumberPicker) findViewById(R.id.numberPickerValue2)).init(getNamable().getEditableValue2(), WatchType.VALUE);
    }

    @Override
    protected boolean isItemSavable() {
        return super.isItemSavable() && CombatCore.findFighterByName(getNamable().getName()) == null;
    }
}
