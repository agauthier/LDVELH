package com.home.ldvelh.model.map;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

import com.home.ldvelh.commons.GameObservable;
import com.home.ldvelh.model.map.MapNode.Anchor;
import com.home.ldvelh.model.map.MapNode.AnchorPair;
import com.home.ldvelh.ui.widget.map.MapContext;
import com.home.ldvelh.ui.widget.map.MapOps;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class AdventureMap extends GameObservable implements Serializable {
    private static final long serialVersionUID = 4259695474056388504L;

    private static class Connection implements Serializable {
        private static final long serialVersionUID = -4499545483948140232L;

        private final MapNode node1;
        private final MapNode node2;

        Connection(MapNode node1, MapNode node2) {
            this.node1 = node1;
            this.node2 = node2;
            if (node1 == null || node2 == null) {
                throw new IllegalArgumentException();
            }
        }

        @Override
        public int hashCode() {
            return node1.hashCode() + node2.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Connection other = (Connection) obj;
            return (node1 == other.node1 && node2 == other.node2) || (node1 == other.node2 && node2 == other.node1);
        }

        boolean hasNode(MapNode node) {
            return node == node1 || node == node2;
        }

        void draw(Canvas canvas) {
            AnchorPair anchors = node1.getAnchors(node2);
            if (anchors != null) {
                Anchor anchor1 = anchors.getAnchor1();
                Anchor anchor2 = anchors.getAnchor2();
                MapContext.drawPath(canvas, anchor1.getPoint(), anchor1.getControlPoint(), anchor2.getControlPoint(), anchor2.getPoint());
            }
        }
    }

    private final Set<MapNode> nodes = new HashSet<>();
    private final Set<Connection> connections = new HashSet<>();

    private transient boolean drawnOnce = false;
    private transient MapNode eipNode;
    private transient RectF enclosingRect;
    private transient Set<MapNode> invalidNodes;
    private transient Set<Connection> invalidConnections;
    private transient PointF mapLastP1;
    private transient PointF mapNewP1;

    public boolean hasDrawnOnce() {
        return drawnOnce;
    }

    public void setDrawnOnce() {
        drawnOnce = true;
    }

    public MapNode getEipNode() {
        return eipNode;
    }

    public void setEipNode(PointF canvasPoint) {
        eipNode = findNode(canvasPoint);
    }

    public void createNode(PointF canvasPoint) {
        MapNode newNode = new MapNode(MapContext.toMapPoint(canvasPoint));
        if (getEipNode() != null) {
            connections.add(new Connection(getEipNode(), newNode));
        }
        nodes.add(newNode);
        eipNode = newNode;
    }

    public MapNode findNode(PointF canvasPoint) {
        PointF mapPoint = MapContext.toMapPoint(canvasPoint);
        for (MapNode mapNode : nodes) {
            if (mapNode.contains(mapPoint)) {
                return mapNode;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    public void clearInvalidNodes() {
        clearInvalidNodes(false);
    }

    public void deleteEipNode() {
        removeNode(getEipNode());
    }

    public void connectToEipNode(PointF canvasPoint) {
        MapNode otherNode = findNode(canvasPoint);
        if (getEipNode() != null && otherNode != null) {
            connections.add(new Connection(getEipNode(), otherNode));
        }
        eipNode = otherNode;
    }

    public RectF getEnclosingRect() {
        if (enclosingRect == null) {
            enclosingRect = new RectF();
        }
        enclosingRect.setEmpty();
        for (MapNode node : nodes) {
            enclosingRect.union(node.getRect());
        }
        return enclosingRect;
    }

    public void dragNode(PointF canvasNewP1) {
        MapNode draggingNode = getEipNode();
        if (draggingNode != null) {
            if (mapLastP1 == null || mapNewP1 == null) {
                mapLastP1 = new PointF();
                mapNewP1 = new PointF();
            }
            mapLastP1.set(MapContext.toMapPoint(MapOps.getCanvasLastP1()));
            mapNewP1.set(MapContext.toMapPoint(canvasNewP1));
            draggingNode.offset(mapNewP1.x - mapLastP1.x, mapNewP1.y - mapLastP1.y);
        }
    }

    public void draw(Canvas canvas) {
        clearInvalidNodes(true);
        for (Connection connection : connections) {
            connection.draw(canvas);
        }
        for (MapNode mapNode : nodes) {
            if (mapNode == getEipNode()) {
                mapNode.draw(canvas, MapContext.EIP_COLOR);
            } else {
                mapNode.draw(canvas, MapContext.DEFAULT_COLOR);
            }
        }
    }

    private void clearInvalidNodes(boolean preserveEipNode) {
        if (invalidNodes == null) {
            invalidNodes = new HashSet<>();
        }
        for (MapNode mapNode : nodes) {
            if ((!preserveEipNode || mapNode != getEipNode()) && StringUtils.isEmpty(mapNode.getName().trim())) {
                invalidNodes.add(mapNode);
            }
        }
        removeNodes(invalidNodes);
        invalidNodes.clear();
    }

    private void removeNodes(Set<MapNode> nodesToRemove) {
        for (MapNode invalidNode : nodesToRemove) {
            removeNode(invalidNode);
        }
    }

    private void removeNode(MapNode nodeToRemove) {
        nodes.remove(nodeToRemove);
        eipNode = null;
        if (invalidConnections == null) {
            invalidConnections = new HashSet<>();
        }
        for (Connection connection : connections) {
            if (connection.hasNode(nodeToRemove)) {
                invalidConnections.add(connection);
            }
        }
        connections.removeAll(invalidConnections);
        invalidConnections.clear();
    }
}
