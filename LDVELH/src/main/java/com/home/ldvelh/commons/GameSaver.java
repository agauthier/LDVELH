package com.home.ldvelh.commons;

import com.home.ldvelh.AdventureConfig;
import com.home.ldvelh.model.Property;

import android.os.Handler;

public abstract class GameSaver {

	private enum State {
		CONFIGURING, STOPPED, RUNNING
	}

	private static class GameSaverRunnable implements Runnable {

		private final AdventureConfig config;

		public GameSaverRunnable(AdventureConfig config) {
			this.config = config;
		}

		@Override public void run() {
			config.saveGame(Property.CHARACTER.get());
			handler.postDelayed(this, SAVE_FREQUENCY);
		}

		void runOnce() {
			config.saveGame(Property.CHARACTER.get());
		}
	}

	private static final long SAVE_FREQUENCY = 10000;

	private static final Handler handler = new Handler();
	private static GameSaverRunnable saveTask;
	private static State state = State.STOPPED;

	public static void start(final AdventureConfig config) {
		if (state == State.STOPPED) {
			state = State.CONFIGURING;
			saveTask = new GameSaverRunnable(config);
			saveTask.run();
			state = State.RUNNING;
		}
	}

	public static void stop() {
		if (state == State.RUNNING) {
			state = State.CONFIGURING;
			handler.removeCallbacks(saveTask);
			saveTask.runOnce();
			state = State.STOPPED;
		}
	}
}
