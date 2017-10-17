package com.home.ldvelh.ui.inflater;

import com.home.ldvelh.ui.activity.AdventureActivity;

import android.view.ViewGroup;

public enum FreeAreaInflaterDefault implements FreeAreaInflater {
	INSTANCE;

	@Override public void inflate(AdventureActivity activity, ViewGroup freeArea) {}
	
	@Override public void resume() {}

	@Override public void pause() {}
}
