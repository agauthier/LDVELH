package com.home.ldvelh.ui.activity;

import android.os.Bundle;
import android.view.ViewGroup;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.value.IntValueHolder.WatchType;
import com.home.ldvelh.ui.widget.CustomNumberPicker;

public class DFAdventureActivity extends AdventureActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGold();
        initFreeArea();
    }

    @Override protected void onResume() {
        super.onResume();
        config.getFreeAreaInflater().resume();
    }

    @Override protected void onPause() {
        super.onPause();
        config.getFreeAreaInflater().pause();
    }

    private void initGold() {
        ((CustomNumberPicker) findViewById(R.id.numberPickerGold)).init(Property.GOLD.get(), WatchType.VALUE);
    }

    private void initFreeArea() {
        config.getFreeAreaInflater().inflate(this, (ViewGroup) findViewById(R.id.freeArea));
    }
}
