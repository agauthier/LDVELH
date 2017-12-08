package com.home.ldvelh.model.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.home.ldvelh.model.Namable;
import com.home.ldvelh.ui.widget.map.MapContext;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public class MapNode implements Namable, Serializable {
    private static final long serialVersionUID = -4717119492007126382L;

    private enum AnchorType {
        LEFT, TOP, RIGHT, BOTTOM
    }

    static class Anchor {

        private final AnchorType type;
        private final PointF point = new PointF();
        private final PointF controlPoint = new PointF();

        Anchor(AnchorType type) {
            this.type = type;
        }

        PointF getPoint() {
            return point;
        }

        PointF getControlPoint() {
            return controlPoint;
        }

        void set(float x, float y) {
            point.set(x, y);
            switch (this.type) {
                case LEFT:
                    controlPoint.set(x - MapContext.CONTROL_POINT_OFFSET, y);
                    break;
                case TOP:
                    controlPoint.set(x, y - MapContext.CONTROL_POINT_OFFSET);
                    break;
                case RIGHT:
                    controlPoint.set(x + MapContext.CONTROL_POINT_OFFSET, y);
                    break;
                case BOTTOM:
                    controlPoint.set(x, y + MapContext.CONTROL_POINT_OFFSET);
                    break;
            }
        }

        boolean allows(Anchor otherAnchor) {
            switch (type) {
                case LEFT:
                    return otherAnchor.point.x < point.x;
                case TOP:
                    return otherAnchor.point.y < point.y;
                case RIGHT:
                    return otherAnchor.point.x > point.x;
                case BOTTOM:
                    return otherAnchor.point.y > point.y;
            }
            throw new IllegalStateException();
        }
    }

    static class AnchorPair {

        private Anchor anchor1;
        private Anchor anchor2;

        AnchorPair set(Anchor anchor1, Anchor anchor2) {
            this.anchor1 = anchor1;
            this.anchor2 = anchor2;
            return this;
        }

        Anchor getAnchor1() {
            return anchor1;
        }

        Anchor getAnchor2() {
            return anchor2;
        }
    }

    private float centerX;
    private float centerY;
    private String name = StringUtils.EMPTY;

    private transient PointF center;
    private transient RectF rect;
    private transient Rect tmpRect;
    private transient Map<AnchorType, Anchor> anchors;
    private transient List<AnchorPair> anchorPairs;
    private transient Map<Float, AnchorPair> anchorCandidates;
    private transient boolean rectValid;

    MapNode(PointF center) {
        this.centerX = center.x;
        this.centerY = center.y;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name.trim();
        rectValid = false;
    }

    RectF getRect() {
        if (!rectValid) {
            calculateRect();
            rectValid = true;
        }
        return rect;
    }

    AnchorPair getAnchors(MapNode otherNode) {
        getRect();
        otherNode.getRect();
        int pairCounter = 0;
        anchorCandidates.clear();
        for (Anchor anchor : getAnchors()) {
            for (Anchor otherAnchor : otherNode.getAnchors()) {
                if (anchor.allows(otherAnchor) && otherAnchor.allows(anchor)) {
                    float distance = PointF.length(anchor.point.x - otherAnchor.point.x, anchor.point.y - otherAnchor.point.y);
                    anchorCandidates.put(distance, anchorPairs.get(pairCounter++).set(anchor, otherAnchor));
                }
            }
        }
        float nearest = Float.MAX_VALUE;
        for (Float distance : anchorCandidates.keySet()) {
            nearest = (distance < nearest) ? distance : nearest;
        }
        return anchorCandidates.get(nearest);
    }

    void offset(float dx, float dy) {
        centerX += dx;
        centerY += dy;
        rectValid = false;
    }

    boolean contains(PointF point) {
        return getRect().contains(point.x, point.y);
    }

    void draw(Canvas canvas, int color) {
        MapContext.drawText(canvas, name, getCenter(), color);
        MapContext.drawRoundRect(canvas, getRect(), color);
    }

    private PointF getCenter() {
        if (center == null) {
            initTransientMembers();
        }
        center.set(centerX, centerY);
        return center;
    }

    private Collection<Anchor> getAnchors() {
        if (!rectValid) {
            calculateRect();
            rectValid = true;
        }
        return anchors.values();
    }

    private void calculateRect() {
        if (rect == null) {
            initTransientMembers();
        }
        MapContext.textPaint.getTextBounds(name, 0, name.length(), tmpRect);
        float halfWidth = MapContext.PADDING + (tmpRect.right - tmpRect.left) / 2;
        float topHeight = -MapContext.textPaint.ascent() + MapContext.PADDING;
        float bottomHeight = MapContext.textPaint.descent() + MapContext.PADDING;
        float left = centerX - halfWidth;
        float top = centerY - topHeight;
        float right = centerX + halfWidth;
        float bottom = centerY + bottomHeight;
        rect.set(left, top, right, bottom);
        anchors.get(AnchorType.LEFT).set(left, (top + bottom) / 2);
        anchors.get(AnchorType.TOP).set((left + right) / 2, top);
        anchors.get(AnchorType.RIGHT).set(right, (top + bottom) / 2);
        anchors.get(AnchorType.BOTTOM).set((left + right) / 2, bottom);
    }

    private void initTransientMembers() {
        center = new PointF();
        rect = new RectF();
        tmpRect = new Rect();
        anchors = new HashMap<>();
        anchors.put(AnchorType.LEFT, new Anchor(AnchorType.LEFT));
        anchors.put(AnchorType.TOP, new Anchor(AnchorType.TOP));
        anchors.put(AnchorType.RIGHT, new Anchor(AnchorType.RIGHT));
        anchors.put(AnchorType.BOTTOM, new Anchor(AnchorType.BOTTOM));
        anchorPairs = new ArrayList<>();
        anchorPairs.add(new AnchorPair());
        anchorPairs.add(new AnchorPair());
        anchorPairs.add(new AnchorPair());
        anchorPairs.add(new AnchorPair());
        anchorCandidates = new HashMap<>();
    }
}
