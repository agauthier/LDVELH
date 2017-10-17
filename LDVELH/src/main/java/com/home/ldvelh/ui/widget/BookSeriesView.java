package com.home.ldvelh.ui.widget;

import java.util.List;

import com.home.ldvelh.AdventureConfig;
import com.home.ldvelh.BookConfig;
import com.home.ldvelh.BookConfig.Series;
import com.home.ldvelh.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

public class BookSeriesView extends LinearLayout {

    public BookSeriesView(Context context) { super(context); }

    public BookSeriesView(Context context, Series series, List<BookConfig> bookConfigs) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.widget_book_series, this);
        Button seriesNameButton = findViewById(R.id.seriesName);
        seriesNameButton.setText(series.getNameResId());
        for (BookConfig bookConfig : bookConfigs) {
            addGamebook(context, bookConfig);
        }
    }

    private void addGamebook(final Context context, final BookConfig bookConfig) {
        LinearLayout layout = findViewById(R.id.seriesBooks);
        layout.addView(new ThumbnailButton(context, bookConfig.getBookThumbnailResId(), new Runnable() {
            @Override public void run() {
                AdventureConfig config = bookConfig.createAdventureConfig();
                if (config != null) {
                    config.launch();
                }
            }
        }));
    }
}
