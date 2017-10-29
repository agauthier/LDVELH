package com.home.ldvelh.ui.widget;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.value.DF04AssetValueHolder;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.ui.activity.AbstractGameActivity;
import com.home.ldvelh.ui.activity.DF04AdventureActivity;
import com.home.ldvelh.ui.page.PagesAdapter;
import com.home.ldvelh.ui.widget.CustomNumberPicker.WatchType;

import java.util.Observable;
import java.util.Observer;

public class DF04CrewMember extends LinearLayout implements Observer {

    private final Property<DF04AssetValueHolder> crewMember;

    public DF04CrewMember(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.widget_crew_member, this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DF04CrewMember, 0, 0);
        final String propertyName = array.getString(R.styleable.DF04CrewMember_crewMemberProperty);
        crewMember = Property.getPropertyByName(propertyName);
        array.recycle();
    }

    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setTitleText();
        TextView luckTextView = findViewById(R.id.luck);
        if (crewMember.get().isCommander()) {
            luckTextView.setText(String.format(Utils.getString(R.string.df04_luckEquals), Property.LUCK.get().getValue()));
            luckTextView.setVisibility(View.VISIBLE);
        } else if (luckTextView != null) {
            ((ViewGroup) luckTextView.getParent()).removeView(luckTextView);
        }
        if (crewMember.get().isShip()) {
            setText(R.id.skillLabel, R.string.df04_shipAttackForce);
            setText(R.id.staminaLabel, R.string.df04_shipShields);
        }
        initPickers(crewMember.get().getSkill(), crewMember.get().getStamina());
        crewMember.get().addLocationObserver(this);
        crewMember.get().updateStaminaObserver();
        AbstractGameActivity.getCurrentActivity().addObserver(this);
        updateDragAndDrop();
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        crewMember.get().deleteLocationObserver(this);
        crewMember.get().updateStaminaObserver();
        DF04AdventureActivity.getCurrentActivity().deleteObserver(this);
    }

    @Override public void update(Observable observable, Object data) {
        setTitleText();
        updateDragAndDrop();
    }

    private void initPickers(IntValueHolder skill, IntValueHolder stamina) {
        CustomNumberPicker skillPicker = findViewById(R.id.numberPickerSkill);
        skillPicker.init(skill, WatchType.VALUE);
        CustomNumberPicker staminaPicker = findViewById(R.id.numberPickerStamina);
        staminaPicker.init(stamina, WatchType.VALUE);
    }

    private void updateDragAndDrop() {
        boolean pageAllowsDrop = PagesAdapter.selectedPageHasTag();
        boolean dragAndDropActive = pageAllowsDrop && crewMember.get().canBeDragged();
        setOnLongClickListener(!dragAndDropActive ? null : new OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item(crewMember.getName());
                ClipData clipData = new ClipData(crewMember.getName(), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                DragShadowBuilder dragShadow = new DragShadowBuilder(getThis());
                startDragAndDrop(clipData, dragShadow, null, 0);
                return false;
            }
        });
    }

    private void setText(int textViewId, int textResId) {
        ((TextView) findViewById(textViewId)).setText(textResId);
    }

    private void setTitleText() {
        TextView textView = findViewById(R.id.crewMemberName);
        textView.setText(crewMember.get().getName());
        textView.setTextColor(crewMember.get().canBeDragged() ? Color.BLACK : Color.GRAY);
    }

    private DF04CrewMember getThis() {
        return this;
    }
}
