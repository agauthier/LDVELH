package com.home.ldvelh.ui.widget.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.character.CCCharacter;
import com.home.ldvelh.model.character.CCCharacter.ZeusInvocation;
import com.home.ldvelh.ui.dialog.MultipleChoiceDialog;
import com.home.ldvelh.ui.dialog.MultipleChoiceDialog.Choice;

import java.util.ArrayList;
import java.util.List;

import static com.home.ldvelh.model.character.CCCharacter.ZeusInvocation.ALL_GODS_NEUTRAL;
import static com.home.ldvelh.model.character.CCCharacter.ZeusInvocation.CANCEL;
import static com.home.ldvelh.model.character.CCCharacter.ZeusInvocation.GAIN_RANDOM_HONOR;
import static com.home.ldvelh.model.character.CCCharacter.ZeusInvocation.REGAIN_HONOR_POINTS;
import static com.home.ldvelh.model.character.CCCharacter.ZeusInvocation.RESUSCITATE;

public class Zeus extends UtilityView {

    public Zeus(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.widget_utility_zeus, this);
    }

    @Override
    public void initLayout() {
        super.initLayout();
        final ImageButton button = findViewById(R.id.zeusButton);
        button.setEnabled(!((CCCharacter) Property.CHARACTER.get()).isZeusInvoked());
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MultipleChoiceDialog invokeLibraDialog = new MultipleChoiceDialog(getContext()) {
                    @Override
                    protected Pair<Integer, List<Choice>> getChoices() {
                        return getZeusChoices(button);
                    }
                };
                invokeLibraDialog.show();
            }
        });
    }

    private Pair<Integer, List<Choice>> getZeusChoices(final ImageButton button) {
        List<Choice> choices = new ArrayList<>();
        addChoice(choices, RESUSCITATE, R.string.cc_zeus_ressuscitate, button);
        addChoice(choices, GAIN_RANDOM_HONOR, R.string.cc_zeus_gain_random_honor_points, button);
        addChoice(choices, REGAIN_HONOR_POINTS, R.string.cc_zeus_regain_honor_points, button);
        addChoice(choices, ALL_GODS_NEUTRAL, R.string.cc_zeus_all_gods_to_neutral_attitude, button);
        addChoice(choices, CANCEL, R.string.cancel, button);
        return new Pair<>(R.string.cc_invoke_zeus, choices);
    }

    private void addChoice(List<Choice> choices, final ZeusInvocation invocation, int choiceResId, final ImageButton button) {
        final CCCharacter character = (CCCharacter) Property.CHARACTER.get();
        if (invocation.canPerform(character)) {
            choices.add(new Choice(choiceResId, new Runnable() {
                @Override
                public void run() {
                    button.setEnabled(!character.invokeZeus(invocation));
                }
            }));
        }
    }
}
