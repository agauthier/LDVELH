package com.home.ldvelh.model.combat;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.OpMode;
import com.home.ldvelh.model.item.Effect;
import com.home.ldvelh.model.item.Item;
import com.home.ldvelh.model.item.ItemAndQuantity;
import com.home.ldvelh.model.item.ListItem;
import com.home.ldvelh.model.value.ListValueHolder;

import java.util.ArrayList;
import java.util.List;

public class CombatRow extends Item {
    private static final long serialVersionUID = 4488821436665768240L;

    public enum Team {
        GOODGUYS {
            @Override
            public Team facingTeam() {
                return MONSTERS;
            }
        }, MONSTERS {
            @Override
            public Team facingTeam() {
                return GOODGUYS;
            }
        };

        public abstract Team facingTeam();
    }

    private final List<Fighter> goodguys = new ArrayList<>();
    private final List<Fighter> monsters = new ArrayList<>();

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
        populateView(row, R.id.goodguysView, goodguys);
        populateView(row, R.id.monstersView, monsters);
    }

    public List<Fighter> getGoodguys() {
        return goodguys;
    }

    public List<Fighter> getMonsters() {
        return monsters;
    }

    public boolean canAssault() {
        return hasMembers(Team.GOODGUYS) && hasMembers(Team.MONSTERS) && (hasExactlyOneMember(Team.GOODGUYS) || hasExactlyOneMember(Team.MONSTERS));
    }

    public List<Fighter> getFighters(Team team) {
        if (team == Team.GOODGUYS) {
            return goodguys;
        } else {
            return monsters;
        }
    }

    void clear() {
        goodguys.clear();
        monsters.clear();
    }

    boolean canAdmitFighter(Fighter fighter) {
        return !(hasMembers(fighter.getTeam()) && hasMoreThanOneMember(fighter.getTeam().facingTeam()));
    }

    void addMemberToFront(Fighter fighter) {
        if (fighter.getTeam() == Team.GOODGUYS) {
            goodguys.add(0, fighter);
        } else {
            monsters.add(0, fighter);
        }
    }

    void addMemberToEnd(Fighter fighter) {
        if (fighter.getTeam() == Team.GOODGUYS) {
            goodguys.add(fighter);
        } else {
            monsters.add(fighter);
        }
    }

    boolean hasMembers(Team team) {
        if (team == Team.GOODGUYS) {
            return goodguys.size() > 0;
        } else {
            return monsters.size() > 0;
        }
    }

    boolean hasExactlyOneMember(Team team) {
        return getFighters(team).size() == 1;
    }

    boolean hasOpponents(Team team) {
        if (team == Team.GOODGUYS) {
            return monsters.size() > 0;
        } else {
            return goodguys.size() > 0;
        }
    }

    public boolean isEmpty() {
        return (goodguys.size() == 0 && monsters.size() == 0);
    }

    void updateAllObservers(boolean delete) {
        if (delete) {
            FighterObserver.removeAll();
        } else {
            FighterObserver.addAll(goodguys);
            FighterObserver.addAll(monsters);
        }
    }

    void deselectAll() {
        for (Fighter fighter : goodguys) {
            fighter.deselect();
        }
        for (Fighter fighter : monsters) {
            fighter.deselect();
        }
    }

    boolean moveUp(OpMode opMode) {
        Fighter selectedFighter = getSelectedFighter();
        return selectedFighter != null && moveUp(selectedFighter, opMode);
    }

    boolean moveDown(OpMode opMode) {
        Fighter selectedFighter = getSelectedFighter();
        return selectedFighter != null && moveDown(selectedFighter, opMode);
    }

    boolean canReset() {
        return canReset(Team.GOODGUYS) || canReset(Team.MONSTERS);
    }

    Team kill(Fighter fighter) {
        if (goodguys.remove(fighter)) {
            return Team.GOODGUYS;
        } else if (monsters.remove(fighter)) {
            return Team.MONSTERS;
        }
        return null;
    }

    private boolean hasMoreThanOneMember(Team team) {
        return getFighters(team).size() > 1;
    }

    private void populateView(View row, final int viewResId, List<Fighter> fighters) {
        ViewGroup viewGroup = row.findViewById(viewResId);
        viewGroup.removeAllViews();
        for (Fighter fighter : fighters) {
            viewGroup.addView(fighter.createView(((Activity) row.getContext()).getLayoutInflater(), viewGroup));
        }
    }

    private Fighter getSelectedFighter() {
        for (Fighter fighter : goodguys) {
            if (fighter.isSelected()) {
                return fighter;
            }
        }
        for (Fighter fighter : monsters) {
            if (fighter.isSelected()) {
                return fighter;
            }
        }
        return null;
    }

    private boolean moveUp(Fighter fighter, OpMode opMode) {
        List<Fighter> fighters = getFighters(fighter.getTeam());
        int idx = fighters.indexOf(fighter);
        if (idx > 0) {
            if (opMode == OpMode.PERFORM) {
                fighters.add(idx - 1, fighters.remove(idx));
            }
            return true;
        } else if (CombatCore.canMoveToRowAbove(fighter, this)) {
            if (opMode == OpMode.PERFORM) {
                CombatCore.moveToRowAbove(fighters.remove(idx), this, opMode);
            }
            return true;
        }
        return false;
    }

    private boolean moveDown(Fighter fighter, OpMode opMode) {
        List<Fighter> fighters = getFighters(fighter.getTeam());
        int idx = fighters.indexOf(fighter);
        if (idx < fighters.size() - 1) {
            if (opMode == OpMode.PERFORM) {
                fighters.add(idx + 1, fighters.remove(idx));
            }
            return true;
        } else if (CombatCore.canMoveToRowBelow(fighter, this)) {
            if (opMode == OpMode.PERFORM) {
                CombatCore.moveToRowBelow(fighters.remove(idx), this, opMode);
            }
            return true;
        }
        return false;
    }

    private boolean canReset(Team team) {
        for (Fighter fighter : getFighters(team)) {
            if (fighter.isEditable()) {
                return true;
            }
        }
        return false;
    }
}
