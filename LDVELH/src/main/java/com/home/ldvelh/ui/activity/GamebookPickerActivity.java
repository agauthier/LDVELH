package com.home.ldvelh.ui.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import com.home.ldvelh.BookConfig;
import com.home.ldvelh.BookConfig.Series;
import com.home.ldvelh.BookConfig.Series.SeriesComparator;
import com.home.ldvelh.ui.widget.BookSeriesView;
import com.home.ldvelh.R;

import android.os.Bundle;
import android.widget.LinearLayout;

public class GamebookPickerActivity extends AbstractGameActivity {

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gamebook_picker);
		Map<Series, List<BookConfig>> bookConfigs = BookConfig.getConfigs();
		LinearLayout mainLayout = findViewById(R.id.gamebook_picker_layout);
		List<Series> orderedSeries = new ArrayList<>(bookConfigs.keySet());
		Collections.sort(orderedSeries, SeriesComparator.INSTANCE);
		for (Series series : orderedSeries) {
			mainLayout.addView(new BookSeriesView(this, series, bookConfigs.get(series)));
		}
	}

	@Override public void update(Observable observable, Object data) {}
}
