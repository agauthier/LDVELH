package com.home.ldvelh.ui.widget.utility;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.Die;
import com.home.ldvelh.model.Property;

public class LuckCheck extends UtilityView {

	public LuckCheck(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.widget_utility_luck_check, this);
	}

	@Override public void initLayout() {
		super.initLayout();
		ImageButton button = findViewById(R.id.luckCheckButton);
		button.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View view) {
				ImageButton imageButton = (ImageButton) view;
				int luck = Property.LUCK.get().getValue();
				if (Die.SIX_FACES.roll(2) <= luck) {
					imageButton.setImageResource(R.drawable.luck_green);
				} else {
					imageButton.setImageResource(R.drawable.luck_red);
				}
				Property.LUCK.get().addWithFeedback(Property.LUCK_PENALTY.get().getValue());
				imageButton.setClickable(false);
				resetImageButton(imageButton);
			}
		});
	}

	private void resetImageButton(final ImageButton imageButton) {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override public void run() {
				imageButton.setImageResource(R.drawable.luck_gray);
				imageButton.setClickable(true);
			}
		}, Constants.RESULT_ANIM_DELAY);
	}

}
