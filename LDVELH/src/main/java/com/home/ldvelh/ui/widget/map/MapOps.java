package com.home.ldvelh.ui.widget.map;

import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.map.AdventureMap;
import com.home.ldvelh.ui.widget.map.MapView.TouchState;

import android.graphics.PointF;
import android.graphics.RectF;
import android.view.View;

public abstract class MapOps {

    private static final RectF canvasRect = new RectF();

    private static final PointF lastP1 = new PointF();
    private static final PointF lastP2 = new PointF();

    static void perform(TouchState state, final MapView mapView) {
        PointF newP1 = MapView.getNewP1();
        PointF newP2 = MapView.getNewP2();
        AdventureMap map = Property.MAP.get().getValue();
        switch (state) {
            case NO_TOUCH:
                break;
            case TOUCHED_NO_NODE:
                break;
            case TOUCHED_NODE:
                break;
            case PANNING:
                MapContext.offset(newP1);
                mapView.invalidate();
                break;
            case START_ZOOMING:
                break;
            case ZOOMING:
                if (PointF.length(newP2.x - newP1.x, newP2.y - newP1.y) >= MapContext.MIN_PINCH_DISTANCE) {
                    MapContext.zoom(newP1, newP2);
                    mapView.invalidate();
                }
                break;
            case NEW_NODE:
                map.createNode(newP1);
                mapView.invalidate();
                mapView.showKeyboard();
                break;
            case START_DRAGGING:
                map.setEipNode(newP1);
                mapView.invalidate();
                break;
            case DRAGGING:
                map.dragNode(newP1);
                mapView.invalidate();
                break;
            case TOGGLING_SELECTION:
                map.toggleEipNode(newP1);
                mapView.invalidate();
                break;
            case CONNECTING:
                map.connectToEipNode(newP1);
                mapView.invalidate();
                break;
        }
        lastP1.set(newP1);
        lastP2.set(newP2);
    }

    static void renameEipNode(View view, String newName) {
        Property.MAP.get().getValue().renameEipNode(newName);
        view.invalidate();
    }

    static void clearInvalidNodes() {
        Property.MAP.get().getValue().clearInvalidNodes();
    }

    public static boolean canFitMapToCanvas() {
        return !Property.MAP.get().getValue().isEmpty();
    }

    public static void fitMapToCanvas(View view) {
        float m = MapContext.FIT_MARGIN;
        canvasRect.set(view.getWidth() * m, view.getHeight() * m, view.getWidth() * (1 - m), view.getHeight() * (1 - m));
        RectF mapRect = Property.MAP.get().getValue().getEnclosingRect();
        float scaleX = (canvasRect.right - canvasRect.left) / (mapRect.right - mapRect.left);
        float scaleY = (canvasRect.bottom - canvasRect.top) / (mapRect.bottom - mapRect.top);
        MapContext.updateTransformation(mapRect, canvasRect, scaleX < scaleY);
        view.invalidate();
    }

    public static boolean canDeleteNode() {
        return Property.MAP.get().getValue().getEipNode() != null;
    }

    public static void deleteNode(View view) {
        Property.MAP.get().getValue().deleteEipNode();
        view.invalidate();
    }

    public static PointF getCanvasLastP1() {
        return lastP1;
    }

    public static PointF getCanvasLastP2() {
        return lastP2;
    }
}
