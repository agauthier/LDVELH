package com.home.ldvelh.ui.widget.map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;

public class MapContext {

	public static final float PADDING = 10f;
	public static final float CONTROL_POINT_OFFSET = 35f;
	public static final int DEFAULT_COLOR = Color.BLACK;
	public static final int EIP_COLOR = Color.GREEN;
	public static final Paint textPaint = new Paint();

	static final float MIN_PINCH_DISTANCE = 50f;
	static final float FIT_MARGIN = 0.02f;

	private static final float TEXT_SIZE = 40f;
	private static final float STROKE_WIDTH = 5f;
	private static final Paint framePaint = new Paint();

	static {
		textPaint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.BOLD));
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setTextSize(TEXT_SIZE);
		framePaint.setColor(DEFAULT_COLOR);
		framePaint.setStyle(Paint.Style.STROKE);
	}

	private static float offsetX;
	private static float offsetY;
	private static float scale;

	private static final RectF mapRect = new RectF();
	private static final RectF canvasRect = new RectF();
	private static final PointF mapPoint = new PointF();
	private static final PointF canvasPoint = new PointF();
	private static final PointF tmpPoint1 = new PointF();
	private static final PointF tmpPoint2 = new PointF();
	private static final PointF tmpPoint3 = new PointF();
	private static final PointF tmpPoint4 = new PointF();
	private static final Paint tmpPaint = new Paint(textPaint);
	private static final Path path = new Path();

	private MapContext() {}

	public static void reset() {
		offsetX = 0f;
		offsetY = 0f;
		scale = 1f;
	}

	static void offset(PointF canvasNewP1) {
		tmpPoint1.set(toMapPoint(MapOps.getCanvasLastP1()));
		tmpPoint2.set(toMapPoint(canvasNewP1));
		offsetX -= (tmpPoint2.x - tmpPoint1.x);
		offsetY -= (tmpPoint2.y - tmpPoint1.y);
	}

	static void zoom(PointF canvasNewP1, PointF canvasNewP2) {
		tmpPoint1.set(toMapPoint(MapOps.getCanvasLastP1()));
		tmpPoint2.set(toMapPoint(MapOps.getCanvasLastP2()));
		mapRect.set(tmpPoint1.x, tmpPoint1.y, tmpPoint2.x, tmpPoint2.y);
		canvasRect.set(canvasNewP1.x, canvasNewP1.y, canvasNewP2.x, canvasNewP2.y);
		boolean scaleAlongXAxis = Math.abs(canvasNewP2.x - canvasNewP1.x) > Math.abs(canvasNewP2.y - canvasNewP1.y);
		updateTransformation(mapRect, canvasRect, scaleAlongXAxis);
	}

	public static PointF toMapPoint(PointF canvasPoint) {
		mapPoint.set(canvasPoint.x / scale + offsetX, canvasPoint.y / scale + offsetY);
		return mapPoint;
	}

	public static void drawText(Canvas canvas, String text, PointF mapPoint, int color) {
		tmpPaint.set(textPaint);
		tmpPaint.setColor(color);
		tmpPaint.setTextSize(TEXT_SIZE * scale);
		PointF canvasPoint = toCanvasPoint(mapPoint);
		canvas.drawText(text, canvasPoint.x, canvasPoint.y, tmpPaint);
	}

	public static void drawRoundRect(Canvas canvas, RectF mapRect, int color) {
		tmpPaint.set(framePaint);
		tmpPaint.setColor(color);
		tmpPaint.setStrokeWidth(STROKE_WIDTH * scale);
		canvas.drawRoundRect(toCanvasRect(mapRect), PADDING * scale, PADDING * scale, tmpPaint);
	}

	public static void drawPath(Canvas canvas, PointF mapP1, PointF mapP2, PointF mapP3, PointF mapP4) {
		tmpPaint.set(framePaint);
		tmpPaint.setStrokeWidth(STROKE_WIDTH * scale);
		tmpPoint1.set(toCanvasPoint(mapP1));
		tmpPoint2.set(toCanvasPoint(mapP2));
		tmpPoint3.set(toCanvasPoint(mapP3));
		tmpPoint4.set(toCanvasPoint(mapP4));
		path.reset();
		path.moveTo(tmpPoint1.x, tmpPoint1.y);
		path.cubicTo(tmpPoint2.x, tmpPoint2.y, tmpPoint3.x, tmpPoint3.y, tmpPoint4.x, tmpPoint4.y);
		canvas.drawPath(path, tmpPaint);
	}

	static void updateTransformation(RectF mapRect, RectF canvasRect, boolean scaleAlongXAxis) {
		float mapCompensationOffsetX = 0;
		float mapCompensationOffsetY = 0;
		float canvasDX = canvasRect.right - canvasRect.left;
		float canvasDY = canvasRect.bottom - canvasRect.top;
		float mapDX = mapRect.right - mapRect.left;
		float mapDY = mapRect.bottom - mapRect.top;
		if (scaleAlongXAxis) {
			scale = canvasDX / mapDX;
			mapCompensationOffsetY = (canvasDY / scale - mapDY) / 2;
		} else {
			scale = canvasDY / mapDY;
			mapCompensationOffsetX = (canvasDX / scale - mapDX) / 2;
		}
		offsetX = mapRect.left - mapCompensationOffsetX - canvasRect.left / scale;
		offsetY = mapRect.top - mapCompensationOffsetY - canvasRect.top / scale;
	}

	@SuppressWarnings("SameReturnValue") private static PointF toCanvasPoint(PointF mapPoint) {
		canvasPoint.set((mapPoint.x - offsetX) * scale, (mapPoint.y - offsetY) * scale);
		return canvasPoint;
	}

	@SuppressWarnings("SameReturnValue") private static RectF toCanvasRect(RectF mapRect) {
		canvasRect.set((mapRect.left - offsetX) * scale, (mapRect.top - offsetY) * scale, (mapRect.right - offsetX) * scale, (mapRect.bottom - offsetY) * scale);
		return canvasRect;
	}
}
