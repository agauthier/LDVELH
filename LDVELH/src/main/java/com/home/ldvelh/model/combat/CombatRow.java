package com.home.ldvelh.model.combat;

import android.app.Activity;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.model.item.Effect;
import com.home.ldvelh.model.item.Item;
import com.home.ldvelh.model.item.ItemAndQuantity;
import com.home.ldvelh.model.item.ListItem;
import com.home.ldvelh.model.value.ListValueHolder;

import java.util.ArrayList;
import java.util.List;

public class CombatRow extends Item {
    private static final long serialVersionUID = 2553340940340277284L;

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

    private final List<Fighter> teamLeft = new ArrayList<>();
    private final List<Fighter> teamRight = new ArrayList<>();

    @SuppressWarnings("unused")
    private CombatRow() {}

    private <T extends ListItem> CombatRow(ItemAndQuantity itemAndQty, List<Effect> effects, Object data, ListValueHolder<T> list) {
        super(itemAndQty, effects, data, list);
    }

    @Override
    public <T extends ListItem> T create(ItemAndQuantity itemAndQty, List<Effect> effects, Object data, ListValueHolder<T> list) {
        @SuppressWarnings("unchecked") T newItem = (T) new CombatRow(itemAndQty, effects, data, list);
        return newItem;
    }

    @Override
    public void increment() {}

    @Override
    public void decrement() {}

    @Override
    public void add(int quantity) {}

    @Override
    public void subtract(int amount) {}

    @Override
    public boolean hasName(String name) {
        return false;
    }

    @Override
    public void initView(View row) {
        populateView(row, Team.LEFT);
        populateView(row, Team.RIGHT);
    }

    public boolean isEmpty() {
        return (teamLeft.size() == 0 && teamRight.size() == 0);
    }

    public List<Fighter> getTeamLeft() {
        return teamLeft;
    }

    public List<Fighter> getTeamRight() {
        return teamRight;
    }

    public boolean canAssault() {
        return hasMembers(Team.LEFT) && hasMembers(Team.RIGHT) && (hasExactlyOneMember(Team.LEFT) || hasExactlyOneMember(Team.RIGHT));
    }

    public List<Fighter> getFighters(Team team) {
        if (team == Team.LEFT) {
            return teamLeft;
        } else {
            return teamRight;
        }
    }

    public Team getTeam(Fighter fighter) {
        return getFighters(Team.LEFT).contains(fighter) ? Team.LEFT : getFighters(Team.RIGHT).contains(fighter) ? Team.RIGHT : null;
    }

    void clear() {
        teamLeft.clear();
        teamRight.clear();
    }

    void add(Fighter fighter, Team team) {
        List<Fighter> fighters = getFighters(team);
        if (!fighters.contains(fighter)) {
            fighters.add(fighter);
        }
    }

    void remove(Fighter fighter) {
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

    boolean hasFighter(Fighter fighter) {
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

    Team kill(Fighter fighter) {
        if (teamLeft.remove(fighter)) {
            return Team.LEFT;
        } else if (teamRight.remove(fighter)) {
            return Team.RIGHT;
        }
        return null;
    }

    Fighter findFighterByName(String name) {
        for (Fighter fighter : teamLeft) {
            if (fighter.getName().equals(name)) {
                return fighter;
            }
        }
        for (Fighter fighter : teamRight) {
            if (fighter.getName().equals(name)) {
                return fighter;
            }
        }
        return null;
    }

    private void populateView(View row, final Team team) {
        ViewGroup teamViewGroup = row.findViewById(team == Team.LEFT ? R.id.teamLeftView : R.id.teamRightView);
        teamViewGroup.removeAllViews();
        final List<Fighter> fighters = team == Team.LEFT ? teamLeft : teamRight;
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
