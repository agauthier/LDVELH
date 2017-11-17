package com.home.ldvelh.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.ldvelh.R;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.model.value.IntValueHolder.WatchType;
import com.home.ldvelh.ui.widget.DamageAnimView.Direction;

import java.util.Observable;
import java.util.Observer;

public class CustomNumberPicker extends RelativeLayout implements Observer {

    private static final boolean DEFAULT_ALLOW_LONG_PRESS = false;
    private static final int DEFAULT_NB_DIGITS = 2;
    private static final int LONG_PRESS_NB_DIGITS = 3;
    private static final int DIGIT_WIDTH = 25;
    private static final int LONG_CLICK_REFRESH_MILLIS = 300;
    private static final int CLICK_ADD = 1;
    private static final int LONG_CLICK_ADD = 10;

    private IntValueHolder value;
    private WatchType type;
    private final Orientation orientation;
    private final float scaleFactor;
    private final boolean allowLongPress;
    private final int nbDigits;

    private static boolean longPress = false;
    private static final Handler handler = new Handler();

    private Runnable longPressRunnable;

    public CustomNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomNumberPicker, 0, 0);
        boolean vertical = array.getBoolean(R.styleable.CustomNumberPicker_vertical, true);
        orientation = (vertical) ? Orientation.VERTICAL : Orientation.HORIZONTAL;
        scaleFactor = array.getFloat(R.styleable.CustomNumberPicker_scaleFactor, 1.0f);
        allowLongPress = (array.getBoolean(R.styleable.CustomNumberPicker_allowLongPress, DEFAULT_ALLOW_LONG_PRESS));
        nbDigits = allowLongPress ? LONG_PRESS_NB_DIGITS : DEFAULT_NB_DIGITS;
        inflateLayout(context);
        array.recycle();
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        scaleView(scaleFactor);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int scaledWidth = (int) Math.ceil(scaleFactor * getMeasuredWidth());
        int scaledHeight = (int) Math.ceil(scaleFactor * getMeasuredHeight());
        setMeasuredDimension(scaledWidth, scaledHeight);
    }

    @Override protected void onDetachedFromWindow() {
        if (value != null) {
            value.deleteObserver(this);
        }
        super.onDetachedFromWindow();
    }

    @Override public void update(Observable observable, Object data) {
        setText();
        if (data != null && (Boolean) data) {
            displayValueChangeAnimation(getWatchedDiff());
        }
        updateButtonsState();
    }

    public void init(IntValueHolder valueHolder, WatchType type) {
        this.value = valueHolder;
        this.type = type;
        setText();
        value.addObserver(this);
        configureComponents();
    }

    private void inflateLayout(Context context) {
        switch (orientation) {
            case VERTICAL:
                LayoutInflater.from(context).inflate(R.layout.widget_custom_number_picker_vertical, this, true);
                break;
            case HORIZONTAL:
                LayoutInflater.from(context).inflate(R.layout.widget_custom_number_picker_horizontal, this, true);
                break;
        }
    }

    private void scaleView(float scaleFactor) {
        setPivotX(0f);
        setPivotY(0f);
        setScaleX(scaleFactor);
        setScaleY(scaleFactor);
    }

    private void setText() {
        ((TextView) findViewById(R.id.customPickerValue)).setText(String.valueOf(getWatchedValue()));
    }

    private void configureComponents() {
        configureButton(R.id.customPickerPlus, CLICK_ADD, LONG_CLICK_ADD);
        configureButton(R.id.customPickerMinus, -CLICK_ADD, -LONG_CLICK_ADD);
        ((TextView) findViewById(R.id.customPickerValue)).setWidth(nbDigits * DIGIT_WIDTH);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void configureButton(int buttonResId, final int clickAdd, final int longClickAdd) {
        ImageButton button = findViewById(buttonResId);
        button.setEnabled(isButtonEnabled(buttonResId));
        button.setOnTouchListener(new OnTouchListener() {
            @Override public boolean onTouch(final View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        view.setPressed(false);
                        if (!longPress) {
                            addToWatchedValue(clickAdd);
                        }
                        longPress = false;
                        handler.removeCallbacks(longPressRunnable);
                        view.performClick();
                        break;
                    case MotionEvent.ACTION_DOWN:
                        view.setPressed(true);
                        view.playSoundEffect(SoundEffectConstants.CLICK);
                        if (allowLongPress) {
                            longPressRunnable = new Runnable() {
                                @Override public void run() {
                                    longPress = true;
                                    addToWatchedValue(longClickAdd);
                                    if (view.isEnabled()) {
                                        handler.postDelayed(longPressRunnable, LONG_CLICK_REFRESH_MILLIS);
                                    } else {
                                        longPress = false;
                                        handler.removeCallbacks(longPressRunnable);
                                    }
                                }
                            };
                            handler.postDelayed(longPressRunnable, ViewConfiguration.getLongPressTimeout());
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void updateButtonsState() {
        ImageButton plusButton = findViewById(R.id.customPickerPlus);
        plusButton.setEnabled(isButtonEnabled(R.id.customPickerPlus));
        ImageButton minusButton = findViewById(R.id.customPickerMinus);
        minusButton.setEnabled(isButtonEnabled(R.id.customPickerMinus));
    }

    private void displayValueChangeAnimation(int diff) {
        if (diff > 0) {
            switch (orientation) {
                case VERTICAL:
                    addView(new DamageAnimView(getContext(), Direction.UP, "+" + String.valueOf(diff)));
                    break;
                case HORIZONTAL:
                    addView(new DamageAnimView(getContext(), Direction.RIGHT, "+" + String.valueOf(diff)));
                    break;
            }
        } else if (diff < 0) {
            switch (orientation) {
                case VERTICAL:
                    addView(new DamageAnimView(getContext(), Direction.DOWN, String.valueOf(diff)));
                    break;
                case HORIZONTAL:
                    addView(new DamageAnimView(getContext(), Direction.LEFT, String.valueOf(diff)));
                    break;
            }
        }
    }

    private void addView(DamageAnimView animView) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(animView, params);
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

    private void addToWatchedValue(int amount) {
        switch (type) {
            case MIN:
                value.addToMin(amount);
                break;
            case MAX:
                value.addToMax(amount);
                break;
            case VALUE:
                value.add(amount);
                break;
        }
    }

    private boolean isButtonEnabled(int buttonResId) {
        boolean isPlusButton = (buttonResId == R.id.customPickerPlus);
        switch (type) {
            case MIN:
                return !isPlusButton || value.getMin() < value.getMax();
            case MAX:
                return isPlusButton || value.getMax() > value.getMin();
            case VALUE:
                return isPlusButton ? value.getValue() < value.getMax() : value.getValue() > value.getMin();
        }
        return true;
    }
}
