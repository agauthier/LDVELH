package com.home.ldvelh.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.ldvelh.R;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.IntValueHolder.WatchType;
import com.home.ldvelh.ui.widget.DamageAnimView.Direction;

import java.util.Observable;
import java.util.Observer;

public class AnimatedNumber extends RelativeLayout implements Observer {

    private static final int DEFAULT_NB_DIGITS = 2;
    private static final int DIGIT_WIDTH = 25;

    private IntValueHolder value;
    private WatchType type;
    private final Orientation animOrientation;
    private final int nbDigits;

    public AnimatedNumber(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.widget_animated_number, this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AnimatedNumber, 0, 0);
        //noinspection ConstantConditions
        animOrientation = Orientation.valueOf(array.getString(R.styleable.AnimatedNumber_animOrientation).toUpperCase());
        nbDigits = array.getInt(R.styleable.AnimatedNumber_nbDigits, DEFAULT_NB_DIGITS);
        array.recycle();
    }

    @Override protected void onDetachedFromWindow() {
        if (value != null) {
            value.deleteObserver(this);
        }
        super.onDetachedFromWindow();
    }

    @Override public void update(Observable observable, Object data) {
        setTextView();
        if (data != null && (Boolean) data) {
            displayValueChangeAnimation(getWatchedDiff());
        }
    }

    @SuppressWarnings("SameParameterValue")
    public void init(IntValueHolder valueHolder, IntValueHolder.WatchType type) {
        this.value = valueHolder;
        this.type = type;
        setTextView();
        value.addObserver(this);
    }

    private void setTextView() {
        TextView textView = findViewById(R.id.animatedNumberValue);
        textView.setWidth(nbDigits * DIGIT_WIDTH);
        textView.setText(String.valueOf(getWatchedValue()));
    }

    private void displayValueChangeAnimation(int diff) {
        if (diff > 0) {
            switch (animOrientation) {
                case VERTICAL:
                    addView(new DamageAnimView(getContext(), Direction.UP, "+" + String.valueOf(diff)));
                    break;
                case HORIZONTAL:
                    addView(new DamageAnimView(getContext(), Direction.RIGHT, "+" + String.valueOf(diff)));
                    break;
            }
        } else if (diff < 0) {
            switch (animOrientation) {
                case VERTICAL:
                    addView(new DamageAnimView(getContext(), Direction.DOWN, String.valueOf(diff)));
                    break;
                case HORIZONTAL:
                    addView(new DamageAnimView(getContext(), Direction.LEFT, String.valueOf(diff)));
                    break;
            }
        }
    }

    private int getWatchedValue() {
        switch (type) {
            case MIN:
                return value.getMin();
            case MAX:
                return value.getMax();
            case VALUE:
                return value.getValue();
        }
        return value.getValue();
    }

    private int getWatchedDiff() {
        switch (type) {
            case MIN:
                return value.getMinDiff();
            case MAX:
                return value.getMaxDiff();
            case VALUE:
                return value.getValueDiff();
        }
        return value.getValue();
    }
}
