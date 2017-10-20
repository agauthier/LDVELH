package com.home.ldvelh.ui.dialog;

import android.content.Context;
import android.util.Pair;
import android.widget.TextView;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.CCGod.God;

import java.util.ArrayList;
import java.util.List;

public class CCTutelaryGodPicker extends MultipleChoiceDialog {

    public CCTutelaryGodPicker(Context context, Object data) {
        super(context, data);
    }

    @Override
    protected Pair<Integer, List<Choice>> getChoices() {
        return new Pair<>(R.string.cc_tutelary_god_choice, getGodChoices());
    }

    @Override
    public void onBackPressed() { /* Do nothing */ }

    private List<Choice> getGodChoices() {
        List<Choice> choices = new ArrayList<>();
        for (final God god : God.tutelaryGods()) {
            choices.add(new Choice(god.getNameResId(), new Runnable() {
                @SuppressWarnings("ConstantConditions")
                @Override
                public void run() {
                    Property.CC_GOD_LIST.get().remove(Property.CC_GOD_LIST.get().find(god));
                    Property.TUTELARY_GOD.get().setValue(god);
                    ((TextView) getOwnerActivity().findViewById(R.id.tutelaryGod)).setText(Property.TUTELARY_GOD.get().getValue().getNameResId());
                }
            }));
        }
        return choices;
    }
}
