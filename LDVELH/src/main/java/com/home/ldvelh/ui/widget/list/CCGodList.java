package com.home.ldvelh.ui.widget.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.home.ldvelh.R;
import com.home.ldvelh.model.item.CCGod;
import com.home.ldvelh.model.item.CCGod.Attitude;

import java.util.ArrayList;
import java.util.List;

import static com.home.ldvelh.model.item.CCGod.Attitude.FAVORABLE;
import static com.home.ldvelh.model.item.CCGod.Attitude.NEUTRAL;
import static com.home.ldvelh.model.item.CCGod.Attitude.UNFAVORABLE;
import static com.home.ldvelh.ui.widget.list.CustomListItem.Quantity.MAXIMUM_ONE;

public class CCGodList extends CustomList<CCGod> {

    public CCGodList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initRowView(View row, CustomListItem<CCGod> customListItem) {
        initRadioButtons(customListItem.getItem());
    }

    @Override
    public CustomListItem<CCGod> createListItem(CCGod item) {
        return new CustomListItem<>(this, item, MAXIMUM_ONE);
    }

    private void initRadioButtons(final CCGod god) {
        for (RadioButton radioButton : getAllRadioButtons()) {
            switch (radioButton.getId()) {
                case R.id.favorable:
                    initRadioButton(radioButton, god, FAVORABLE);
                    break;
                case R.id.neutral:
                    initRadioButton(radioButton, god, NEUTRAL);
                    break;
                case R.id.unfavorable:
                    initRadioButton(radioButton, god, UNFAVORABLE);
                    break;
            }
        }
    }

    private List<RadioButton> getAllRadioButtons() {
        List<RadioButton> radioButtons = new ArrayList<>();
        RadioGroup radioGroup = findViewById(R.id.buttonGroup);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioButtons.add((RadioButton) radioGroup.getChildAt(i));
        }
        return radioButtons;
    }

    private void initRadioButton(RadioButton radioButton, final CCGod god, final Attitude attitude) {
        radioButton.setChecked(god.getAttitude().equals(attitude));
        radioButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                god.setAttitude(attitude);
            }
        });
    }
}
