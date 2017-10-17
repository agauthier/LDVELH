package com.home.ldvelh.ui.widget.map;

import static com.home.ldvelh.ui.widget.map.MapView.TouchState.MotionAction.LIFT_FINGER;
import static com.home.ldvelh.ui.widget.map.MapView.TouchState.MotionAction.LONG_TOUCH_NODE;
import static com.home.ldvelh.ui.widget.map.MapView.TouchState.MotionAction.MOVE_ONE_FINGER;
import static com.home.ldvelh.ui.widget.map.MapView.TouchState.MotionAction.MOVE_TWO_FINGERS;
import static com.home.ldvelh.ui.widget.map.MapView.TouchState.MotionAction.START_LIFT_FINGER;
import static com.home.ldvelh.ui.widget.map.MapView.TouchState.MotionAction.TOUCH_FREESPACE;
import static com.home.ldvelh.ui.widget.map.MapView.TouchState.MotionAction.TOUCH_NODE;

import java.util.HashMap;
import java.util.Map;

import com.home.ldvelh.model.Property;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Handler;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

public class MapView extends View {

    enum TouchState {
        NO_TOUCH, TOUCHED_NO_NODE, TOUCHED_NODE, PANNING, START_ZOOMING, ZOOMING, NEW_NODE, START_DRAGGING, DRAGGING, TOGGLING_SELECTION, CONNECTING;

        enum MotionAction {
            TOUCH_FREESPACE, TOUCH_NODE, LONG_TOUCH_NODE, MOVE_ONE_FINGER, MOVE_TWO_FINGERS, START_LIFT_FINGER, LIFT_FINGER
        }

        private static final Map<Pair<TouchState, MotionAction>, TouchState> m = new HashMap<>();

        static {
            m.put(Pair.create(NO_TOUCH, TOUCH_FREESPACE), TOUCHED_NO_NODE);
            m.put(Pair.create(NO_TOUCH, TOUCH_NODE), TOUCHED_NODE);
            m.put(Pair.create(TOUCHED_NO_NODE, MOVE_ONE_FINGER), PANNING);
            m.put(Pair.create(TOUCHED_NO_NODE, MOVE_TWO_FINGERS), START_ZOOMING);
            m.put(Pair.create(TOUCHED_NO_NODE, START_LIFT_FINGER), NEW_NODE);
            m.put(Pair.create(TOUCHED_NODE, MOVE_ONE_FINGER), START_DRAGGING);
            m.put(Pair.create(TOUCHED_NODE, START_LIFT_FINGER), TOGGLING_SELECTION);
            m.put(Pair.create(TOUCHED_NODE, LONG_TOUCH_NODE), CONNECTING);
            m.put(Pair.create(PANNING, MOVE_ONE_FINGER), PANNING);
            m.put(Pair.create(START_ZOOMING, MOVE_TWO_FINGERS), ZOOMING);
            m.put(Pair.create(ZOOMING, MOVE_TWO_FINGERS), ZOOMING);
            m.put(Pair.create(START_DRAGGING, MOVE_ONE_FINGER), DRAGGING);
            m.put(Pair.create(DRAGGING, MOVE_ONE_FINGER), DRAGGING);
        }

        private static TouchState state = NO_TOUCH;

        public static TouchState nextState(MotionAction action) {
            TouchState newState = m.get(Pair.create(state, action));
            state = (newState != null) ? newState : NO_TOUCH;
            return state;
        }
    }

    private static final Handler handler = new Handler();
    private static final PointF p1 = new PointF();
    private static final PointF p2 = new PointF();

    private static Runnable longPressRunnable;

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initOnTouchListener();
    }

    @Override public boolean performClick() {
        super.performClick();
        return true;
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!Property.MAP.get().getValue().hasDrawnOnce()) {
            if (MapOps.canFitMapToCanvas()) {
                MapOps.fitMapToCanvas(this);
            } else {
                MapContext.reset();
            }
            Property.MAP.get().getValue().setDrawnOnce();
        }
        Property.MAP.get().getValue().draw(canvas);
    }

    @Override public boolean onCheckIsTextEditor() {
        return true;
    }

    @Override public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.actionLabel = null;
        outAttrs.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS;
        outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;
        return new MapViewInputConnection(this);
    }

    @Override public void invalidate() {
        super.invalidate();
        Property.MAP.get().getValue().notifyObservers();
    }

    public static PointF getNewP1() { return p1; }

    public static PointF getNewP2() { return p2; }

    public void setText(String text) {
        MapOps.renameEipNode(this, text);
    }

    public void finishComposingText() {
        MapOps.clearInvalidNodes();
    }

	public void showKeyboard() {
		setOnTouchListener(null);
        requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(this, InputMethodManager.SHOW_FORCED);
    }

    public void hideKeyboard() {
        initOnTouchListener();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
    }

    private void getPointerPositions(MotionEvent event) {
        switch (event.getPointerCount()) {
            case 2:
                p1.set(Math.min(event.getX(0), event.getX(1)), Math.min(event.getY(0), event.getY(1)));
                p2.set(Math.max(event.getX(0), event.getX(1)), Math.max(event.getY(0), event.getY(1)));
                break;
            default:
                p1.set(event.getX(0), event.getY(0));
                break;
        }
    }

    private void initOnTouchListener() {
        setOnTouchListener(new OnTouchListener() {
            @Override public boolean onTouch(final View view, MotionEvent event) {
                getParent().requestDisallowInterceptTouchEvent(true);
                getPointerPositions(event);
                final MapView mapView = (MapView) view;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (Property.MAP.get().getValue().findNode(p1) != null) {
                            MapOps.perform(TouchState.nextState(TOUCH_NODE), mapView);
                            longPressRunnable = new Runnable() {
                                @Override public void run() {
                                    MapOps.perform(TouchState.nextState(LONG_TOUCH_NODE), mapView);
                                    handler.removeCallbacks(this);
                                }
                            };
                            handler.postDelayed(longPressRunnable, ViewConfiguration.getLongPressTimeout());
                        } else {
                            MapOps.perform(TouchState.nextState(TOUCH_FREESPACE), mapView);
                            handler.removeCallbacks(longPressRunnable);
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if (event.getPointerCount() == 1) {
                            MapOps.perform(TouchState.nextState(MOVE_ONE_FINGER), mapView);
                        } else if (event.getPointerCount() == 2) {
                            MapOps.perform(TouchState.nextState(MOVE_TWO_FINGERS), mapView);
                        }
                        handler.removeCallbacks(longPressRunnable);
                        return true;
                    case MotionEvent.ACTION_UP:
                        MapOps.perform(TouchState.nextState(START_LIFT_FINGER), mapView);
                        MapOps.perform(TouchState.nextState(LIFT_FINGER), mapView);
                        view.performClick();
                        handler.removeCallbacks(longPressRunnable);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
}
