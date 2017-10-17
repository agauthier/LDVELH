package com.home.ldvelh.ui.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class SpreadedGridLayout extends LinearLayout {

	private final List<LinearLayout> buttons = new ArrayList<>();

	public SpreadedGridLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		createGridLayout(width, height);
		setMeasuredDimension(width, height);
	}

	private void createGridLayout(int width, int height) {
		transferButtonsToList();
		int buttonWidth = buttons.get(0).getMeasuredWidth();
		int buttonHeight = buttons.get(0).getMeasuredHeight();
		int idealNbColumns = width / buttonWidth;
		int idealColWidth = width / idealNbColumns;
		int scaledHeight = idealColWidth * buttonHeight / buttonWidth;
		int availableHeightPerRow = height / (int) Math.ceil(buttons.size() / idealNbColumns);
		int idealRowHeight = Math.min(scaledHeight, availableHeightPerRow);
		LinearLayout newLinearLayout = null;
		for (int i = 0; i < buttons.size(); i++) {
			if (i % idealNbColumns == 0) {
				newLinearLayout = new LinearLayout(getContext());
				measureLayout(newLinearLayout, width, idealRowHeight);
				addView(newLinearLayout);
			}
			measureLayout(buttons.get(i), idealColWidth, idealRowHeight);
			newLinearLayout.addView(buttons.get(i));
		}
	}

	private void transferButtonsToList() {
		if (buttons.size() == 0) {
			for (int i = 0; i < getChildCount(); i++) {
				buttons.add((LinearLayout) getChildAt(i));
			}
		} else {
			for (int i = 0; i < getChildCount(); i++) {
				((LinearLayout) getChildAt(i)).removeAllViews();
			}
		}
		removeAllViews();
	}

	private void measureLayout(LinearLayout layout, int width, int height) {
		layout.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
	}
}
