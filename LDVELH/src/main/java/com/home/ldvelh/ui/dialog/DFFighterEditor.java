package com.home.ldvelh.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.combat.CombatCore;
import com.home.ldvelh.model.combat.DFEditableFighter;
import com.home.ldvelh.model.combat.Fighter;
import com.home.ldvelh.model.value.IntValueHolder.WatchType;
import com.home.ldvelh.ui.widget.CustomNumberPicker;

import org.apache.commons.lang3.StringUtils;

public class DFFighterEditor extends AdventureDialog {

    private String fighterName = StringUtils.EMPTY;
    private final Fighter fighter = new DFEditableFighter();

    public DFFighterEditor(Context context) {
        super(context, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_name_value_pair_editor);
        initFighterNameTextEdit();
        initView();
    }

    @Override
    public void dismiss() {
        Utils.hideKeyboard(findViewById(R.id.itemName));
        super.dismiss();
    }

    public Fighter getFighter() {
        return fighter;
    }

    void initView() {
        super.initView();
        ((TextView) findViewById(R.id.itemName)).setHint(R.string.fighter_name);
        ((TextView) findViewById(R.id.firstValueName)).setText(R.string.skill);
        ((TextView) findViewById(R.id.secondValueName)).setText(R.string.stamina);
        ((CustomNumberPicker) findViewById(R.id.numberPickerFirstValue)).init(fighter.getSkill(), WatchType.VALUE);
        ((CustomNumberPicker) findViewById(R.id.numberPickerSecondValue)).init(fighter.getStamina(), WatchType.VALUE);
        Button okButton = findViewById(R.id.okButton);
        okButton.setEnabled(false);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fighter.setName(fighterName);
                dismiss();
            }
        });
    }

    private void initFighterNameTextEdit() {
        EditText nameEditText = findViewById(R.id.itemName);
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                fighterName = s.toString();
                findViewById(R.id.okButton).setEnabled(!fighterName.isEmpty() && CombatCore.findFighterByName(fighterName) == null);
            }
        });
    }
}
