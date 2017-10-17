package com.home.ldvelh.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;

class DamageAnimView extends AppCompatTextView {

    enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private static final long SCALE_DURATION = 1;
    private static final float ANIM_SCALE = 2.5f;
    private static final float TRANSLATION = 50f;
    private static final float ALPHA = 0.4f;

    public DamageAnimView(Context context) { super(context); }

    DamageAnimView(Context context, final Direction direction, String text) {
        super(context);
        setTextAppearance(R.style.ldvelhSmallest);
        setBackgroundResource(getBgResId(direction));
        setGravity(Gravity.CENTER);
        setTextColor(Color.WHITE);
        setText(text);
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                getThis().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                final ViewPropertyAnimator animator = getAnimator(direction);
                animator.withStartAction(new Runnable() {
                    @Override public void run() {
                        animator.scaleX(ANIM_SCALE).scaleY(ANIM_SCALE).setDuration(SCALE_DURATION);
                    }
                });
                animator.withEndAction(new Runnable() {
                    @Override public void run() {
                        ViewGroup parent = (ViewGroup) getParent();
                        if (parent != null) {
                            parent.removeView(getThis());
                        }
                    }
                });
            }
        });
    }

    private int getBgResId(Direction direction) {
        switch (direction) {
            case UP:
            case RIGHT:
                return R.drawable.green_star_small;
            case DOWN:
            case LEFT:
                return R.drawable.red_star_small;
            default:
                throw new IllegalStateException("Unreachable code");
        }
    }

    private ViewPropertyAnimator getAnimator(Direction direction) {
        switch (direction) {
            case UP:
                return animate().alpha(ALPHA).yBy(-TRANSLATION).setDuration(Constants.WOUND_ANIM_DELAY);
            case DOWN:
                return animate().alpha(ALPHA).yBy(TRANSLATION).setDuration(Constants.WOUND_ANIM_DELAY);
            case LEFT:
                return animate().alpha(ALPHA).xBy(-TRANSLATION).setDuration(Constants.WOUND_ANIM_DELAY);
            case RIGHT:
                return animate().alpha(ALPHA).xBy(TRANSLATION).setDuration(Constants.WOUND_ANIM_DELAY);
            default:
                throw new IllegalStateException("Unreachable code");
        }
    }

    private AppCompatTextView getThis() {
        return this;
    }
}
