package com.home.ldvelh.ui.widget.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;

import com.home.ldvelh.R;
import com.home.ldvelh.model.item.CCGod;
import com.home.ldvelh.model.item.CCGod.Attitude;

import java.util.ArrayList;
import java.util.List;

import static com.home.ldvelh.model.item.CCGod.Attitude.FAVORABLE;
import static com.home.ldvelh.model.item.CCGod.Attitude.NEUTRAL;
import static com.home.ldvelh.model.item.CCGod.Attitude.UNFAVORABLE;

public class CCGodListView extends CustomListView<CCGod> {

    public CCGodListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initRowView(View row, CustomListViewItem<CCGod> customListViewItem) {
        setItemName(row, customListViewItem.getText());
        initRadioButtons(row, customListViewItem.getItem());
    }

    @Override
    protected CCGod createItem(String name) {
        return null;
    }

    @Override
    protected CustomListViewItem<CCGod> createCustomListViewItem(CCGod item) {
        return new CustomListViewItem<>(this, item);
    }

    private void initRadioButtons(View row, final CCGod god) {
        List<RadioButton> radioButtons = getRadioButtons(row);
        for (RadioButton radioButton : radioButtons) {
            switch (radioButton.getId()) {
                case R.id.favorable:
                    initRadioButton(radioButtons, radioButton, god, FAVORABLE);
                    break;
                case R.id.neutral:
                    initRadioButton(radioButtons, radioButton, god, NEUTRAL);
                    break;
                case R.id.unfavorable:
                    initRadioButton(radioButtons, radioButton, god, UNFAVORABLE);
                    break;
            }
        }
    }

    private void initRadioButton(final List<RadioButton> buttons, final RadioButton radioButton, final CCGod god, final Attitude attitude) {
        radioButton.setChecked(god.getAttitude().equals(attitude));
        radioButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                god.setAttitude(attitude);
                checkRadioButton(buttons, radioButton);
            }
        });
    }

    private void checkRadioButton(List<RadioButton> buttons, RadioButton radioButtonToCheck) {
        for (RadioButton radioButton : buttons) {
            radioButton.setChecked(radioButton == radioButtonToCheck);
        }
    }

    private List<RadioButton> getRadioButtons(View view) {
        List<RadioButton> radioButtons = new ArrayList<>();
        radioButtons.add((RadioButton) view.findViewById(R.id.favorable));
        radioButtons.add((RadioButton) view.findViewById(R.id.neutral));
        radioButtons.add((RadioButton) view.findViewById(R.id.unfavorable));
        return radioButtons;
    }
}
