package com.home.ldvelh.ui.inflater;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.character.SOCharacter;
import com.home.ldvelh.model.value.IntValueHolder.WatchType;
import com.home.ldvelh.ui.activity.AdventureActivity;
import com.home.ldvelh.ui.widget.CustomNumberPicker;

import java.util.Observable;
import java.util.Observer;

public enum FreeAreaInflaterSO implements FreeAreaInflater, Observer {
    INSTANCE;

    @Override
    public void inflate(final AdventureActivity activity, ViewGroup freeArea) {
        View view = activity.getLayoutInflater().inflate(R.layout.widget_so_meals, freeArea);
        CustomNumberPicker mealPicker = activity.findViewById(R.id.numberPickerMeal);
        mealPicker.init(Property.MEALS.get(), WatchType.VALUE);
        initButtons(view);
    }

    @Override
    public void resume() {}

    @Override
    public void pause() {}

    @Override
    public void update(Observable observable, Object data) {}

    private void initButtons(final View view) {
        final SOCharacter character = (SOCharacter) Property.CHARACTER.get();
        final ImageButton eatButton = view.findViewById(R.id.eatMeal);
        eatButton.setImageResource(character.isFirstMealOfTheDay() ? R.drawable.eat1st : R.drawable.eat);
        eatButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Property.MEALS.get().add(-1);
                int mealValue = character.isFirstMealOfTheDay() ? 2 : 1;
                Property.STAMINA.get().addWithFeedback(mealValue);
                eatButton.setImageResource(R.drawable.eat);
                character.eat();
            }
        });
        final ImageButton sleepButton = view.findViewById(R.id.sleep);
        sleepButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                character.sleep();
                eatButton.setImageResource(R.drawable.eat1st);
            }
        });
    }
}
