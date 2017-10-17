package com.home.ldvelh.ui.inflater;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.character.SOCharacter;
import com.home.ldvelh.ui.activity.AdventureActivity;
import com.home.ldvelh.ui.dialog.MultipleChoiceDialog;
import com.home.ldvelh.ui.dialog.MultipleChoiceDialog.Choice;

import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public enum FreeAreaInflaterSO implements FreeAreaInflater, Observer {
    INSTANCE;

    @Override public void inflate(final AdventureActivity activity, ViewGroup freeArea) {
        final View view = activity.getLayoutInflater().inflate(R.layout.widget_so_libra_button, freeArea);
        final Button button = view.findViewById(R.id.button);
        button.setEnabled(!((SOCharacter) Property.CHARACTER.get()).isLibraInvoked());
        button.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                MultipleChoiceDialog invokeLibraDialog = new MultipleChoiceDialog(activity) {
                    @Override protected Pair<Integer, List<Choice>> getChoices() {
                        return getLibraChoices(button);
                    }
                };
                invokeLibraDialog.show();
            }
        });
    }

    @Override public void resume() {}

    @Override public void pause() {}

    @Override public void update(Observable observable, Object data) {}

    private Pair<Integer, List<Choice>> getLibraChoices(final Button button) {
        List<Choice> choices = new ArrayList<>();
        final SOCharacter character = (SOCharacter) Property.CHARACTER.get();
        choices.add(new Choice(R.string.so_restore_initial_values, new Runnable() {
            @Override public void run() {
                character.invokeLibra(true);
                button.setEnabled(false);
            }
        }));
        choices.add(new Choice(R.string.so_flee_or_heal, new Runnable() {
            @Override public void run() {
                character.invokeLibra(false);
                button.setEnabled(false);
            }
        }));
        return new Pair<>(R.string.so_invoke_libra, choices);
    }
}
