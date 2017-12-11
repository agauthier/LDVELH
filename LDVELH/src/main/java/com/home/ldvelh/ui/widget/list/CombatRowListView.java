package com.home.ldvelh.ui.widget.list;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.combat.CombatCore;
import com.home.ldvelh.model.combat.CombatRow;
import com.home.ldvelh.model.combat.CombatRow.Team;
import com.home.ldvelh.model.combat.Fighter;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CombatRowListView extends CustomListView<CombatRow> {

    private enum TeamState {INVISIBLE, VISIBLE, ACTIVATED}

    public CombatRowListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initRowView(View row, CustomListViewItem<CombatRow> customListViewItem) {
        populateView(row, customListViewItem.getItem(), Team.LEFT);
        populateView(row, customListViewItem.getItem(), Team.RIGHT);
    }

    @Override
    protected CombatRow createItem(String name) {
        return new CombatRow();
    }

    @Override
    protected CustomListViewItem<CombatRow> createCustomListViewItem(CombatRow item) {
        return null;
    }

    private void populateView(View row, final CombatRow combatRow, final Team team) {
        ViewGroup teamViewGroup = row.findViewById(team == Team.LEFT ? R.id.teamLeftView : R.id.teamRightView);
        teamViewGroup.removeAllViews();
        final List<Fighter> fighters = team == Team.LEFT ? combatRow.getTeamLeft() : combatRow.getTeamRight();
        for (Fighter fighter : fighters) {
            teamViewGroup.addView(fighter.createView(((Activity) row.getContext()).getLayoutInflater(), teamViewGroup));
        }
        setTeamViewBorder(teamViewGroup, fighters.isEmpty() ? TeamState.INVISIBLE : TeamState.VISIBLE);
        teamViewGroup.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (dropAreaAccepts(event)) {
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            return true;
                        case DragEvent.ACTION_DROP:
                            Fighter fighter = CombatCore.findFighterByName(event.getClipData().getItemAt(0).getText().toString());
                            CombatCore.moveFighter(fighter, combatRow, team);
                            setTeamViewBorder(v, TeamState.VISIBLE);
                            return true;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            setTeamViewBorder(v, TeamState.ACTIVATED);
                            return true;
                        case DragEvent.ACTION_DRAG_EXITED:
                            setTeamViewBorder(v, fighters.isEmpty() ? TeamState.INVISIBLE : TeamState.VISIBLE);
                            return true;
                        case DragEvent.ACTION_DRAG_LOCATION:
                        case DragEvent.ACTION_DRAG_ENDED:
                            return true;
                        default:
                            return false;
                    }
                } else {
                    return false;
                }
            }
        });
    }

    private boolean dropAreaAccepts(DragEvent event) {
        if (event.getClipDescription() != null) {
            String mimeType = event.getClipDescription().getMimeType(0);
            return Constants.MIMETYPE_FIGHTER.equals(mimeType) || Constants.MIMETYPE_CHARACTER_FIGHTER.equals(mimeType);
        } else {
            return false;
        }
    }

    private void setTeamViewBorder(View view, TeamState teamState) {
        switch (teamState) {
            case INVISIBLE:
                view.setBackground(null);
                break;
            case VISIBLE:
                view.setBackground(view.getResources().getDrawable(R.drawable.border_brown, null));
                break;
            case ACTIVATED:
                view.setBackground(view.getResources().getDrawable(R.drawable.border_green, null));
                break;
        }
    }
}
