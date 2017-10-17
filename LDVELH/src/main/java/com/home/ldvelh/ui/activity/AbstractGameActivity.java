package com.home.ldvelh.ui.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import android.os.Bundle;

public abstract class AbstractGameActivity extends ObservableFragmentActivity implements Observer {

	private static final List<AbstractGameActivity> activities = new ArrayList<>();

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activities.add(0, this);
	}

	@Override protected void onDestroy() {
		super.onDestroy();
		removeActivity(this);
	}

	static void removeActivity(AbstractGameActivity activity) {
		activities.remove(activity);
	}

	public static AbstractGameActivity getCurrentActivity() {
		return activities.get(0);
	}

	public static String getResourcesString(int resId) {
		return activities.get(0).getResources().getString(resId);
	}
}
