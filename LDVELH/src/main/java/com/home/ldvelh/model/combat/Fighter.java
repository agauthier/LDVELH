package com.home.ldvelh.model.combat;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.commons.GameObservable;

import java.io.Serializable;

public abstract class Fighter implements Serializable {
    private static final long serialVersionUID = 6303373136051393415L;

    public abstract String getName();

    public abstract GameObservable getLifeObservable();

    public abstract boolean isDead();

    public abstract void kill();

    public abstract View createView(LayoutInflater inflater, ViewGroup root);

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
