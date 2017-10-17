package com.home.ldvelh.ui.widget.utility;

import java.util.Random;

import com.home.ldvelh.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Dice extends RelativeLayout implements UtilityView {

	private static final Random RANDOM = new Random();
	private static final int ROLL_DURATION = 300;

	public Dice(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.widget_utility_dice, this);
	}

	@Override public void initLayout() {
		Button button = findViewById(R.id.diceButton);
		button.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View view) {
				final ImageView firstDieImage = findViewById(R.id.firstDieImage);
				final ImageView secondDieImage = findViewById(R.id.secondDieImage);
				final int buttonWidth = view.getWidth();
				final int buttonHeight = view.getHeight();
				int firstDieOutToX = -2 * firstDieImage.getWidth();
				int firstDieInToX = (buttonWidth / 2) - (firstDieImage.getWidth() / 2);
				int firstDieInToY = getRandomY(buttonHeight - firstDieImage.getHeight());
				removeDie(firstDieImage, firstDieOutToX, firstDieInToX, firstDieInToY, true);
				int secondDieOutToX = buttonWidth + 2 * secondDieImage.getWidth();
				removeDie(secondDieImage, secondDieOutToX, 0, 0, false);
			}

		});
		button.setOnLongClickListener(new OnLongClickListener() {
			@Override public boolean onLongClick(View view) {
				final ImageView firstDieImage = findViewById(R.id.firstDieImage);
				final ImageView secondDieImage = findViewById(R.id.secondDieImage);
				final int buttonWidth = view.getWidth();
				final int buttonHeight = view.getHeight();
				int firstDieOutToX = -2 * firstDieImage.getWidth();
				int firstDieInToX = (buttonWidth * 3 / 10) - (firstDieImage.getWidth() / 2);
				int firstDieInToY = getRandomY(buttonHeight - firstDieImage.getHeight());
				removeDie(firstDieImage, firstDieOutToX, firstDieInToX, firstDieInToY, true);
				int secondDieOutToX = buttonWidth + 2 * secondDieImage.getWidth();
				int secondDieInToX = (buttonWidth * 7 / 10) - (secondDieImage.getWidth() / 2);
				int secondDieInToY = getRandomY(buttonHeight - secondDieImage.getHeight());
				removeDie(secondDieImage, secondDieOutToX, secondDieInToX, secondDieInToY, true);
				return true;
			}
		});
	}

	private void removeDie(final ImageView dieImage, int outToX, final int inToX, final int inToY, boolean throwDie) {
		ViewPropertyAnimator animator = dieImage.animate().x(outToX).y(inToY).setDuration(ROLL_DURATION).setInterpolator(new AccelerateInterpolator());
		if (throwDie) {
			animator.withEndAction(new Runnable() {
				@Override public void run() {
					throwDie(dieImage, inToX);
				}
			});
		}
	}

	private void throwDie(final ImageView dieImage, int toX) {
		dieImage.setImageResource(getRandomDieResource());
		dieImage.setVisibility(View.VISIBLE);
		dieImage.animate().x(toX).rotationBy(getRandomRotation()).setDuration(getRandomDuration()).setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
			@Override public void run() {
				dieImage.animate().cancel();
			}
		});
	}

	private int getRandomDieResource() {
		switch (RANDOM.nextInt(6) + 1) {
		case 1:
			return R.drawable.die1;
		case 2:
			return R.drawable.die2;
		case 3:
			return R.drawable.die3;
		case 4:
			return R.drawable.die4;
		case 5:
			return R.drawable.die5;
		case 6:
			return R.drawable.die6;
		default:
			throw new IllegalStateException("Die doesn't have six faces!!!");
		}

	}

	private int getRandomY(int height) {
		return (height / 3) + RANDOM.nextInt(height / 3);
	}

	private int getRandomRotation() {
		return 180 + RANDOM.nextInt(180);
	}

	private int getRandomDuration() {
		return ROLL_DURATION + RANDOM.nextInt(ROLL_DURATION / 2);
	}
}
