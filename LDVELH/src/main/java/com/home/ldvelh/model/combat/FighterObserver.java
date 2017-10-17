package com.home.ldvelh.model.combat;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.value.IntValueHolder;

import android.os.Handler;

class FighterObserver implements Observer {

	private static final Map<FighterObserver, Fighter> map = new HashMap<>();
	private static final Handler handler = new Handler();

	static void addAll(Collection<Fighter> fighters) {
		for (Fighter fighter : fighters) {
			add(fighter);
		}
	}

	static void removeAll() {
		for (Entry<FighterObserver, Fighter> entry : map.entrySet()) {
			entry.getValue().getStamina().deleteObserver(entry.getKey());
		}
		map.clear();
	}

	static void add(Fighter fighter) {
		FighterObserver fighterObserver = new FighterObserver();
		fighter.getStamina().addObserver(fighterObserver);
		map.put(fighterObserver, fighter);
	}

	@Override public void update(Observable observable, Object data) {
		IntValueHolder fighterStamina = (IntValueHolder) observable;
		if (fighterStamina.getValue() <= 0) {
			final Fighter fighter = map.remove(this);
			fighter.getStamina().deleteObserver(this);
			handler.postDelayed(new Runnable() {
				@Override public void run() {
					CombatCore.kill(fighter);
				}
			}, Constants.WOUND_ANIM_DELAY);
		}
	}
}
