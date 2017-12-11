package com.home.ldvelh.model.combat;

import com.home.ldvelh.model.item.Item;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CombatRow extends Item {
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

    private final List<Fighter> teamLeft = new ArrayList<>();
    private final List<Fighter> teamRight = new ArrayList<>();

    public CombatRow() {
        super(StringUtils.EMPTY, null);
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
}
