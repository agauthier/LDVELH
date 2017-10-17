package com.home.ldvelh.ui.inflater;

import com.home.ldvelh.ui.activity.AdventureActivity;

import android.view.ViewGroup;

public interface FreeAreaInflater {
	void inflate(AdventureActivity activity, ViewGroup freeArea);
	void resume();
	void pause();
}
