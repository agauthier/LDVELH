package com.home.ldvelh.model.combat;

import android.app.Activity;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.item.Effect;
import com.home.ldvelh.model.item.Item;

import java.util.ArrayList;
import java.util.List;

public class CombatRow<T extends Fighter> extends Item {
    private static final long serialVersionUID = -775458784598086913L;

    public enum Team {
        LEFT {
            @Override
            public Team facingTeam() {
                return RIGHT;
            }
        }, RIGHT {
            @Override
            public Team facingTeam() {
                return LEFT;
            }
        };

        public abstract Team facingTeam();
    }

    private enum TeamState {INVISIBLE, VISIBLE, ACTIVATED}

    private final List<T> teamLeft = new ArrayList<>();
    private final List<T> teamRight = new ArrayList<>();

    private CombatRow() { super(); }

    @SuppressWarnings("unused")
    private CombatRow(String name, List<Effect> effects, Object data) {
        super(name, effects, data);
    }

    @Override
    public <U extends Item> U copy() {
        CombatRow combatRow = new CombatRow();
        populate(combatRow);
        //noinspection unchecked
        return (U) combatRow;
    }

    @Override
    public void initView(View row) {
        populateView(row, Team.LEFT);
        populateView(row, Team.RIGHT);
    }

    public boolean isEmpty() {
        return (teamLeft.size() == 0 && teamRight.size() == 0);
    }

    public List<T> getTeamLeft() {
        return teamLeft;
    }

    public List<T> getTeamRight() {
        return teamRight;
    }

    public boolean canAssault() {
        return hasMembers(Team.LEFT) && hasMembers(Team.RIGHT) && (hasExactlyOneMember(Team.LEFT) || hasExactlyOneMember(Team.RIGHT));
    }

    public List<T> getFighters(Team team) {
        if (team == Team.LEFT) {
            return teamLeft;
        } else {
            return teamRight;
        }
    }

    public Team getTeam(T fighter) {
        return getFighters(Team.LEFT).contains(fighter) ? Team.LEFT : getFighters(Team.RIGHT).contains(fighter) ? Team.RIGHT : null;
    }

    void clear() {
        teamLeft.clear();
        teamRight.clear();
    }

    void add(T fighter, Team team) {
        List<T> fighters = getFighters(team);
        if (!fighters.contains(fighter)) {
            fighters.add(fighter);
        }
    }

    void remove(T fighter) {
        teamLeft.remove(fighter);
        teamRight.remove(fighter);
    }

    boolean hasMembers(Team team) {
        return !getFighters(team).isEmpty();
    }

    boolean hasExactlyOneMember(Team team) {
        return getFighters(team).size() == 1;
    }

    boolean hasOpponents(Team team) {
        return !getFighters(team.facingTeam()).isEmpty();
    }

    boolean hasFighter(T fighter) {
        return teamLeft.contains(fighter) || teamRight.contains(fighter);
    }

    void updateAllObservers(boolean delete) {
        if (delete) {
            FighterObserver.removeAll();
        } else {
            FighterObserver.addAll(teamLeft);
            FighterObserver.addAll(teamRight);
        }
    }

    Team kill(T fighter) {
        if (teamLeft.remove(fighter)) {
            return Team.LEFT;
        } else if (teamRight.remove(fighter)) {
            return Team.RIGHT;
        }
        return null;
    }

    T findFighterByName(String name) {
        for (T fighter : teamLeft) {
            if (fighter.getName().equals(name)) {
                return fighter;
            }
        }
        for (T fighter : teamRight) {
            if (fighter.getName().equals(name)) {
                return fighter;
            }
        }
        return null;
    }

    @Override
    protected void populate(Item item) {
        @SuppressWarnings("unchecked") CombatRow<T> combatRow = (CombatRow<T>) item;
        super.populate(combatRow);
        teamLeft.addAll(combatRow.teamLeft);
        teamRight.addAll(combatRow.teamRight);
    }

    private void populateView(View row, final Team team) {
        ViewGroup teamViewGroup = row.findViewById(team == Team.LEFT ? R.id.teamLeftView : R.id.teamRightView);
        teamViewGroup.removeAllViews();
        final List<T> fighters = team == Team.LEFT ? teamLeft : teamRight;
        for (T fighter : fighters) {
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
                            T fighter = CombatCore.findFighterByName(event.getClipData().getItemAt(0).getText().toString());
                            CombatCore.moveFighter(fighter, getThis(), team);
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

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
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

    private CombatRow getThis() {
        return this;
    }
}
