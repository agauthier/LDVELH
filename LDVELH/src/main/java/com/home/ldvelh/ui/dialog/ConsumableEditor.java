package com.home.ldvelh.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.item.Effect;
import com.home.ldvelh.model.item.EffectTarget;
import com.home.ldvelh.model.item.ItemAndQuantity;
import com.home.ldvelh.ui.widget.VerticalSeekBar;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ConsumableEditor extends AdventureDialog {

	private static final int MIN_AMOUNT_VALUE = -9;
	private static final int MAX_AMOUNT_VALUE = 9;
	private static final int INITIAL_AMOUNT = 1;

	private final List<Effect> effects = new ArrayList<>();
	private EffectTarget target = EffectTarget.SKILL;
	private int amount = INITIAL_AMOUNT;

	public ConsumableEditor(Context context, String itemName) {
		super(context, null);
		setContentView(R.layout.dialog_consumable_editor);
		initPropertyRadioGroup();
		initAmountArea();
		initEffectListArea();
		initOkButton(itemName);
		initCancelButton();
	}

	private void initPropertyRadioGroup() {
		RadioGroup propertyRadioGroup = findViewById(R.id.propertyRadioGroup);
		propertyRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.skillRadioButton:
					target = EffectTarget.SKILL;
					adjustSeekBarMax(true);
					break;
				case R.id.maxSkillRadioButton:
					target = EffectTarget.MAX_SKILL;
					adjustSeekBarMax(false);
					break;
				case R.id.staminaRadioButton:
					target = EffectTarget.STAMINA;
					adjustSeekBarMax(true);
					break;
				case R.id.maxStaminaRadioButton:
					target = EffectTarget.MAX_STAMINA;
					adjustSeekBarMax(false);
					break;
				case R.id.luckRadioButton:
					target = EffectTarget.LUCK;
					adjustSeekBarMax(true);
					break;
				case R.id.maxLuckRadioButton:
					target = EffectTarget.MAX_LUCK;
					adjustSeekBarMax(false);
					break;
				}
			}
		});
	}

	private void initAmountArea() {
		final TextView amountTextView = setText(R.id.amountTextView, Utils.signedIntToString(amount));
		initAmountSeekBar().setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override public void onStopTrackingTouch(SeekBar seekBar) {}

			@Override public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				int value = progress + MIN_AMOUNT_VALUE;
				if (value > MAX_AMOUNT_VALUE) {
					amountTextView.setText(R.string.max);
					amount = Effect.MAX;
				} else {
					amountTextView.setText(Utils.signedIntToString(value));
					amount = value;
				}
			}
		});
	}

	private VerticalSeekBar initAmountSeekBar() {
		return adjustSeekBarMax(true, true);
	}

	private void adjustSeekBarMax(boolean allowAbsMax) {
		adjustSeekBarMax(false, allowAbsMax);
	}

	private VerticalSeekBar adjustSeekBarMax(boolean init, boolean allowAbsMax) {
		VerticalSeekBar seekBar = findViewById(R.id.amountSeekBar);
		int absMax = MAX_AMOUNT_VALUE - MIN_AMOUNT_VALUE + 1;
		if (init) {
			seekBar.setMax(absMax);
			seekBar.setProgress(INITIAL_AMOUNT - MIN_AMOUNT_VALUE);
		} else if (!allowAbsMax) {
			if (seekBar.getProgress() == absMax) {
				seekBar.incrementProgressBy(-1);
			}
			seekBar.setMax(absMax - 1);
		} else {
			seekBar.setMax(absMax);
		}
		return seekBar;
	}

	private void initEffectListArea() {
		final TextView effectsTextView = findViewById(R.id.effectsList);
		effectsTextView.setMovementMethod(new ScrollingMovementMethod());
		Button addEffectButton = findViewById(R.id.addEffectButton);
		addEffectButton.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				effects.add(new Effect(target, amount));
				effectsTextView.setText(getEffects());
			}
		});
	}

	private void initOkButton(final String itemName) {
		Button okButton = findViewById(R.id.okButton);
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				if (!effects.isEmpty()) {
					Property.ITEM_LIST.get().add(new ItemAndQuantity(itemName), effects);
				}
				fulfill();
				dismiss();
			}
		});
	}

	private void initCancelButton() {
		Button cancelButton = findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				effects.clear();
				dismiss();
			}
		});
	}

	private String getEffects() {
		StringBuilder result = new StringBuilder();
		boolean firstEffect = true;
		for (Effect effect : effects) {
			result.append(!firstEffect ? "\n" : "").append(effect.toString());
			firstEffect = false;
		}
		return result.toString();
	}
}
