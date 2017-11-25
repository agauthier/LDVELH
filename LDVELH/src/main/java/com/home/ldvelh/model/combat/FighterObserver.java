package com.home.ldvelh.model.combat;

import android.os.Handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import static com.home.ldvelh.commons.Constants.WOUND_ANIM_DELAY;

class FighterObserver implements Observer {

	private static final Map<FighterObserver, Fighter> map = new HashMap<>();
	private static final Handler handler = new Handler();

	static <T extends Fighter> void addAll(Collection<T> fighters) {
		for (T fighter : fighters) {
			add(fighter);
		}
	}

	static void removeAll() {
		for (Entry<FighterObserver, Fighter> entry : map.entrySet()) {
			entry.getValue().getLifeObservable().deleteObserver(entry.getKey());
		}
		map.clear();
	}

	static void add(Fighter fighter) {
		FighterObserver fighterObserver = new FighterObserver();
		fighter.getLifeObservable().addObserver(fighterObserver);
		map.put(fighterObserver, fighter);
	}

	@Override public void update(Observable observable, Object data) {
		final Fighter fighter = map.get(this);
		if (fighter.isDead()) {
			map.remove(this);
			fighter.getLifeObservable().deleteObserver(this);
			handler.postDelayed(new Runnable() {
				@Override public void run() {
					CombatCore.kill(fighter);
				}
			}, WOUND_ANIM_DELAY);
		}
	}
}
