package com.home.ldvelh.ui.dialog;

import java.util.ArrayList;
import java.util.List;

import com.home.ldvelh.AdventureConfig;
import com.home.ldvelh.R;

import android.content.Context;
import android.util.Pair;

public class NewCharacterConfirmation extends MultipleChoiceDialog {

	public NewCharacterConfirmation(Context context, Object data) {
		super(context, data);
	}

	@Override protected Pair<Integer, List<Choice>> getChoices() {
		return new Pair<>(R.string.new_game, getNewGameChoices());
	}

	private List<Choice> getNewGameChoices() {
		List<Choice> choices = new ArrayList<>();
		final AdventureConfig config = (AdventureConfig) getData();
		choices.add(new Choice(R.string.default_initialization, new Runnable() {
			@Override public void run() {
				config.launchNewDefaultCharacter();
			}
		}));
		choices.add(new Choice(R.string.load_game_from_previous_book, new Runnable() {
			@Override public void run() {
				config.launchExistingPreviousCharacter();
			}
		}));
		return choices;
	}
}
