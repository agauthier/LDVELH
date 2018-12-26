package com.home.ldvelh.ui.widget.map;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.map.AdventureMap;
import com.home.ldvelh.model.map.MapNode;
import com.home.ldvelh.commons.Namable;
import com.home.ldvelh.ui.dialog.NameEditor;

import java.util.HashMap;
import java.util.Map;

import static com.home.ldvelh.ui.widget.map.MapView.TouchState.Motion.DOUBLE_TAP_NODE;
import static com.home.ldvelh.ui.widget.map.MapView.TouchState.Motion.LIFT_FINGER;
import static com.home.ldvelh.ui.widget.map.MapView.TouchState.Motion.LONG_TOUCH_NODE;
import static com.home.ldvelh.ui.widget.map.MapView.TouchState.Motion.MOVE_ONE_FINGER;
import static com.home.ldvelh.ui.widget.map.MapView.TouchState.Motion.MOVE_TWO_FINGERS;
import static com.home.ldvelh.ui.widget.map.MapView.TouchState.Motion.RELEASE;
import static com.home.ldvelh.ui.widget.map.MapView.TouchState.Motion.TOUCH_FREESPACE;
import static com.home.ldvelh.ui.widget.map.MapView.TouchState.Motion.TOUCH_NODE;

public class MapView extends View {

    enum TouchState {
        NO_TOUCH,
        TOUCHED_NO_NODE,
        TOUCHED_NODE,
        PAN,
        START_ZOOMING,
        ZOOM,
        NEW_NODE,
        START_DRAGGING,
        DRAG,
        SET_SELECTION,
        CONNECT,
        EDIT_NAME;

        enum Motion {
            TOUCH_FREESPACE,
            TOUCH_NODE,
            LONG_TOUCH_NODE,
            DOUBLE_TAP_NODE,
            MOVE_ONE_FINGER,
            MOVE_TWO_FINGERS,
            LIFT_FINGER,
            RELEASE
        }

        private static final Map<Pair<TouchState, Motion>, TouchState> m = new HashMap<>();

        static {
            m.put(Pair.create(NO_TOUCH, TOUCH_FREESPACE), TOUCHED_NO_NODE);
            m.put(Pair.create(NO_TOUCH, TOUCH_NODE), TOUCHED_NODE);
            m.put(Pair.create(TOUCHED_NO_NODE, MOVE_ONE_FINGER), PAN);
            m.put(Pair.create(TOUCHED_NO_NODE, MOVE_TWO_FINGERS), START_ZOOMING);
            m.put(Pair.create(TOUCHED_NO_NODE, LIFT_FINGER), NEW_NODE);
            m.put(Pair.create(TOUCHED_NODE, MOVE_ONE_FINGER), START_DRAGGING);
            m.put(Pair.create(TOUCHED_NODE, LIFT_FINGER), SET_SELECTION);
            m.put(Pair.create(TOUCHED_NODE, LONG_TOUCH_NODE), CONNECT);
            m.put(Pair.create(SET_SELECTION, DOUBLE_TAP_NODE), EDIT_NAME);
            m.put(Pair.create(PAN, MOVE_ONE_FINGER), PAN);
            m.put(Pair.create(START_ZOOMING, MOVE_TWO_FINGERS), ZOOM);
            m.put(Pair.create(ZOOM, MOVE_TWO_FINGERS), ZOOM);
            m.put(Pair.create(START_DRAGGING, MOVE_ONE_FINGER), DRAG);
            m.put(Pair.create(DRAG, MOVE_ONE_FINGER), DRAG);
        }

        private static TouchState state = NO_TOUCH;

        public static TouchState nextState(Motion action) {
            TouchState newState = m.get(Pair.create(state, action));
            state = (newState != null) ? newState : NO_TOUCH;
            return state;
        }
    }

    private static final Handler handler = new Handler();
    private static final PointF p1 = new PointF();
    private static final PointF p2 = new PointF();

    private static boolean doubleTapWindowActive = false;

    private final MapView thisMapView = this;
    private final Runnable longPressRunnable = new Runnable() {
        @Override
        public void run() {
            MapOps.perform(TouchState.nextState(LONG_TOUCH_NODE), thisMapView);
            handler.removeCallbacks(this);
        }
    };
    private final Runnable doubleTapRunnable = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(this);
            doubleTapWindowActive = false;
        }
    };

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initOnTouchListener();
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
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

    @Override
    public void invalidate() {
        super.invalidate();
        Property.MAP.get().getValue().notifyObservers();
    }

    public static PointF getNewP1() { return p1; }

    public static PointF getNewP2() { return p2; }

    public void getNodeName(Namable namable) {
        NameEditor<Namable> nameEditor = new NameEditor<>(getContext(), namable);
        nameEditor.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Property.MAP.get().getValue().clearInvalidNodes();
                thisMapView.invalidate();
            }
        });
        nameEditor.show();
    }

    private void initOnTouchListener() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View view, MotionEvent event) {
                getParent().requestDisallowInterceptTouchEvent(true);
                getPointerPositions(event);
                final MapView mapView = (MapView) view;
                AdventureMap map = Property.MAP.get().getValue();
                MapNode tappedNode = map.findNode(p1);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        cancelLongPress();
                        if (tappedNode != map.getEipNode()) {
                            cancelDoubleTap();
                        }
                        if (tappedNode != null) {
                            MapOps.perform(TouchState.nextState(TOUCH_NODE), mapView);
                            handler.postDelayed(longPressRunnable, ViewConfiguration.getLongPressTimeout());
                        } else {
                            MapOps.perform(TouchState.nextState(TOUCH_FREESPACE), mapView);
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        cancelLongPress();
                        cancelDoubleTap();
                        if (event.getPointerCount() == 1) {
                            MapOps.perform(TouchState.nextState(MOVE_ONE_FINGER), mapView);
                        } else if (event.getPointerCount() == 2) {
                            MapOps.perform(TouchState.nextState(MOVE_TWO_FINGERS), mapView);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        cancelLongPress();
                        MapOps.perform(TouchState.nextState(LIFT_FINGER), mapView);
                        if (doubleTapWindowActive) {
                            cancelDoubleTap();
                            MapOps.perform(TouchState.nextState(DOUBLE_TAP_NODE), mapView);
                        } else if (tappedNode != null) {
                            doubleTapWindowActive = true;
                            handler.postDelayed(doubleTapRunnable, ViewConfiguration.getDoubleTapTimeout());
                        }
                        MapOps.perform(TouchState.nextState(RELEASE), mapView);
                        view.performClick();
                        return true;
                    default:
                        return false;
                }
            }

            private void cancelLongPress() {
                handler.removeCallbacks(longPressRunnable);
            }

            private void cancelDoubleTap() {
                doubleTapWindowActive = false;
                handler.removeCallbacks(doubleTapRunnable);
            }
        });
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
}
