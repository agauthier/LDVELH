package com.home.ldvelh.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import com.home.ldvelh.R;
import com.home.ldvelh.ui.widget.ChoiceButton;

import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.ViewGroup;

public abstract class MultipleChoiceDialog extends AdventureDialog {

	public static class Choice {

		private final int textResId;
		private final Runnable action;

		public Choice(int textResId, Runnable action) {
			this.textResId = textResId;
			this.action = action;
		}

		int getTextResId() {
			return textResId;
		}

		Runnable getAction() {
			return action;
		}
	}

	private final int questionResId;
	private final List<Choice> choices = new ArrayList<>();

	protected MultipleChoiceDialog(Context context) {
		this(context, null);
	}
	
	MultipleChoiceDialog(Context context, Object data) {
		super(context, data);
		Pair<Integer, List<Choice>> choices = getChoices();
		this.questionResId = choices.first;
		this.choices.addAll(choices.second);
	}

	protected abstract Pair<Integer, List<Choice>> getChoices();
	
	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_multiple_choice);
		setQuestionText(questionResId);
		ViewGroup buttons = findViewById(R.id.choiceButtons);
		for (final Choice choice : choices) {
			buttons.addView(new ChoiceButton(getContext(), choice.getTextResId(), new Runnable() {
				@Override public void run() {
					choice.getAction().run();
					fulfill();
					dismiss();
				}
			}));
		}
	}
}
