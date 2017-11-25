package com.home.ldvelh.model.combat;

import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.commons.GameObservable;
import com.home.ldvelh.model.value.EnumValueHolder;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.ui.widget.CCAttribute;

import org.apache.commons.lang3.StringUtils;

import static com.home.ldvelh.commons.Constants.WOUND_ANIM_DELAY;
import static com.home.ldvelh.model.combat.CCCondition.DEAD;
import static com.home.ldvelh.model.combat.CCCondition.UNSCATHED;

public class CCEditableFighter extends Fighter implements EditableFighter {
    private static final long serialVersionUID = -6679053233711079272L;

    private static final int DEFAULT_STRENGTH = 5;
    private static final int DEFAULT_PROTECTION = 10;

    private final IntValueHolder strength;
    private final IntValueHolder protection;
    private final IntValueHolder bonus;
    private final EnumValueHolder<CCCondition> condition;
    private String name = StringUtils.EMPTY;

    CCEditableFighter() {
        super();
        this.strength = new IntValueHolder(0, Constants.BIG_POSITIVE, DEFAULT_STRENGTH);
        this.protection = new IntValueHolder(0, Constants.BIG_POSITIVE, DEFAULT_PROTECTION);
        this.bonus = new IntValueHolder(Constants.BIG_NEGATIVE, Constants.BIG_POSITIVE, 0);
        this.condition = new EnumValueHolder<>(UNSCATHED);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public GameObservable getLifeObservable() {
        return condition;
    }

    @Override
    public boolean isDead() {
        return condition.getValue() == DEAD;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup root) {
        final View newView = inflater.inflate(R.layout.list_item_cc_combat_fighter, root, false);
        initView(newView, false);
        initAttribute(newView, R.id.strengthAttribute, strength, bonus);
        initAttribute(newView, R.id.protectionAttribute, protection, new IntValueHolder(0, 0, 0));
        Button testButton = newView.findViewById(R.id.testButton);
        testButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CCCondition oldCondition = condition.getValue();
                condition.setValue(oldCondition.worsen());
                ObjectAnimator animation = ObjectAnimator.ofArgb(newView, "backgroundColor", oldCondition.getColor(), condition.getValue().getColor());
                animation.setDuration(WOUND_ANIM_DELAY);
                animation.start();
            }
        });
        return newView;
    }

    @Override
    public void kill() {}

    @Override
    void initView(View view, boolean isCharacter) {
        super.initView(view, isCharacter);
        view.setBackgroundColor(condition.getValue().getColor());
    }

    @Override
    public void setEditableName(String name) {
        this.name = name;
    }

    @Override
    public int getEditableValue1NameResId() {
        return R.string.cc_strength;
    }

    @Override
    public IntValueHolder getEditableValue1() {
        return getStrength();
    }

    @Override
    public int getEditableValue2NameResId() {
        return R.string.cc_protection;
    }

    @Override
    public IntValueHolder getEditableValue2() {
        return getProtection();
    }

    public IntValueHolder getStrength() {
        return strength;
    }

    public IntValueHolder getProtection() {
        return protection;
    }

    private void initAttribute(View newView, int attributeResId, IntValueHolder value, IntValueHolder bonus) {
        CCAttribute attribute = newView.findViewById(attributeResId);
        attribute.initEditableFighter(value, bonus);
    }
}
