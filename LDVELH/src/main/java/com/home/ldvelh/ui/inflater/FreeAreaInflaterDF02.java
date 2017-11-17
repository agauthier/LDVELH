package com.home.ldvelh.ui.inflater;

import android.view.ViewGroup;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.IntValueHolder.WatchType;
import com.home.ldvelh.ui.activity.AdventureActivity;
import com.home.ldvelh.ui.dialog.DF02SpellPicker;
import com.home.ldvelh.ui.widget.CustomNumberPicker;

import java.util.Observable;
import java.util.Observer;

public enum FreeAreaInflaterDF02 implements FreeAreaInflater, Observer {
	INSTANCE;

	private AdventureActivity activity;

	@Override public void inflate(AdventureActivity activity, ViewGroup freeArea) {
		this.activity = activity;
		activity.getLayoutInflater().inflate(R.layout.widget_magic_score, freeArea);
		CustomNumberPicker magicPicker = activity.findViewById(R.id.numberPickerMagic);
		magicPicker.init(Property.MAGIC.get(), WatchType.MAX);
	}

	@Override public void resume() {
		Property.MAGIC.get().addObserver(this);
	}

	@Override public void pause() {
		Property.MAGIC.get().deleteObserver(this);
	}

	@Override public void update(Observable observable, Object data) {
		IntValueHolder magic = Property.MAGIC.get();
		if (magic.getMaxDiff() > 0) {
			Property.MAGIC.get().add(magic.getMaxDiff());
			DF02SpellPicker spellPicker = new DF02SpellPicker(activity);
			spellPicker.show();
		}
	}
}
