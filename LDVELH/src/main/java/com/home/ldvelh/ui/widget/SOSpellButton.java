package com.home.ldvelh.ui.widget;

import org.apache.commons.lang3.StringUtils;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.ItemAndQuantity;
import com.home.ldvelh.model.item.SimpleItem;
import com.home.ldvelh.model.value.IntValueHolder;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class SOSpellButton extends LinearLayout {

	private enum SpellResult {
		SUCCESS, FAILURE
	}

	private static final Handler handler = new Handler();

	private final String name;
	private final int cost;
	private final String ingredient;

	public SOSpellButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.widget_so_spell_button, this);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SOSpellButton, 0, 0);
		name = array.getString(R.styleable.SOSpellButton_name);
		cost = array.getInt(R.styleable.SOSpellButton_cost, 0);
		ingredient = array.getString(R.styleable.SOSpellButton_ingredient);
		array.recycle();
		initButton();
	}

	private void initButton() {
		final Button button = findViewById(R.id.button);
		button.setText(name);
		button.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				Property.STAMINA.get().addWithFeedback(-cost);
				IntValueHolder gold = Property.GOLD.get();
				SimpleItem ingredientItem = Property.INGREDIENT_LIST.get().find(ingredient);
				if (StringUtils.isEmpty(ingredient)) {
					displayButtonFeedback(button, SpellResult.SUCCESS);
				} else if (ingredient.equals(Utils.getString(R.string.so_ingredient_21)) && gold.getValue() > 0) {
					gold.addWithFeedback(-1);
					displayButtonFeedback(button, SpellResult.SUCCESS);
				} else if (ingredientItem != null) {
					Property.INGREDIENT_LIST.get().add(new ItemAndQuantity(ingredientItem.getName(), -1));
					displayButtonFeedback(button, SpellResult.SUCCESS);
				} else {
					displayButtonFeedback(button, SpellResult.FAILURE);
				}
			}
		});
	}

	private void displayButtonFeedback(final Button button, SpellResult result) {
		switch (result) {
		case SUCCESS:
			button.setTextColor(Color.GREEN);
			break;
		case FAILURE:
			button.setTextColor(Color.RED);
			button.setText(Utils.getString(R.string.failed));
			break;
		}
		handler.postDelayed(new Runnable() {
			@Override public void run() {
				button.setTextColor(Color.BLACK);
				button.setText(name);
			}
		}, Constants.RESULT_ANIM_DELAY);
	}
}
