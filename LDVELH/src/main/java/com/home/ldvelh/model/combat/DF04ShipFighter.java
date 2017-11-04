package com.home.ldvelh.model.combat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.home.ldvelh.R;
import com.home.ldvelh.model.combat.CombatRow.Team;

class DF04ShipFighter extends DFEditableFighter {
    private static final long serialVersionUID = -7232644395791504982L;

    DF04ShipFighter(Team team) {
        super(team);
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup root) {
        View newView = super.createView(inflater, root);
        TextView skillLabel = newView.findViewById(R.id.skillLabel);
        skillLabel.setText(R.string.df04_shipAttackForce);
        TextView staminaLabel = newView.findViewById(R.id.staminaLabel);
        staminaLabel.setText(R.string.df04_shipShields);
        return newView;
    }
}
