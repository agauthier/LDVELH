package com.home.ldvelh.ui.widget.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.character.SOCharacter;
import com.home.ldvelh.ui.dialog.MultipleChoiceDialog;
import com.home.ldvelh.ui.dialog.MultipleChoiceDialog.Choice;

import java.util.ArrayList;
import java.util.List;

public class Libra extends LinearLayout implements UtilityView {

    public Libra(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.widget_utility_libra, this);
    }

    @Override
    public void initLayout() {
        final ImageButton button = findViewById(R.id.libraButton);
        button.setEnabled(!((SOCharacter) Property.CHARACTER.get()).isLibraInvoked());
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MultipleChoiceDialog invokeLibraDialog = new MultipleChoiceDialog(getContext()) {
                    @Override
                    protected Pair<Integer, List<Choice>> getChoices() {
                        return getLibraChoices(button);
                    }
                };
                invokeLibraDialog.show();
            }
        });
    }

    private Pair<Integer, List<Choice>> getLibraChoices(final ImageButton button) {
        List<Choice> choices = new ArrayList<>();
        final SOCharacter character = (SOCharacter) Property.CHARACTER.get();
        choices.add(new Choice(R.string.so_restore_initial_values, new Runnable() {
            @Override
            public void run() {
                character.invokeLibra(true);
                button.setEnabled(false);
            }
        }));
        choices.add(new Choice(R.string.so_flee_or_heal, new Runnable() {
            @Override
            public void run() {
                character.invokeLibra(false);
                button.setEnabled(false);
            }
        }));
        choices.add(new Choice(R.string.cancel, new Runnable() {
            @Override
            public void run() {}
        }));
        return new Pair<>(R.string.so_invoke_libra, choices);
    }
}
