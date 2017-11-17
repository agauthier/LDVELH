package com.home.ldvelh.model.combat;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.value.IntValueHolder;

import java.io.Serializable;

public abstract class Fighter implements Serializable {
    private static final long serialVersionUID = -4242063330098427868L;

    public abstract String getName();

    public abstract void setName(String name);

    public abstract IntValueHolder getSkill();

    public abstract IntValueHolder getStamina();

    public abstract IntValueHolder getBonus();

    public abstract View createView(LayoutInflater inflater, ViewGroup root);

    public abstract void kill();

    void initView(final View view, final boolean isCharacter) {
        final TextView fighterNameView = view.findViewById(R.id.fighterName);
        fighterNameView.setText(getName());
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item(getName());
                ClipData clipData = new ClipData(getName(), new String[]{isCharacter ? Constants.MIMETYPE_CHARACTER_FIGHTER : Constants.MIMETYPE_FIGHTER}, item);
                View.DragShadowBuilder dragShadow = new View.DragShadowBuilder(v);
                v.startDrag(clipData, dragShadow, null, 0);
                return false;
            }
        });
    }
}
