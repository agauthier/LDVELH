package com.home.ldvelh.model.character;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.Die;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.combat.DF04CrewMemberFighter;
import com.home.ldvelh.model.combat.Fighter;
import com.home.ldvelh.model.combat.strategy.CombatStrategy;
import com.home.ldvelh.model.combat.strategy.DF04CombatStrategy;
import com.home.ldvelh.model.combat.strategy.DF04PhaserState;
import com.home.ldvelh.model.value.DF04AssetValueHolder;
import com.home.ldvelh.model.value.DF04CommanderValueHolder;
import com.home.ldvelh.model.value.DF04CrewMemberValueHolder;
import com.home.ldvelh.model.value.DF04ShipValueHolder;
import com.home.ldvelh.model.value.IntValueHolder;
import com.home.ldvelh.ui.activity.AdventureActivity;

import java.util.ArrayList;
import java.util.List;

public class DF04Character extends Character {
    private static final long serialVersionUID = -2691346573290954894L;

    private static final int FIGHTING_MALUS = -3;

    private final DF04AssetValueHolder commander;
    private final DF04AssetValueHolder scienceOfficer;
    private final DF04AssetValueHolder medicalOfficer;
    private final DF04AssetValueHolder engineeringOfficer;
    private final DF04AssetValueHolder ship;
    private final DF04AssetValueHolder securityOfficer;
    private final DF04AssetValueHolder securityAgent1;
    private final DF04AssetValueHolder securityAgent2;
    private final IntValueHolder luck;
    private final IntValueHolder luckPenalty;

    private DF04PhaserState phaserState;

    public DF04Character() {
        commander = new DF04CommanderValueHolder(getName(R.string.df04_commander));
        scienceOfficer = new DF04CrewMemberValueHolder(getName(R.string.df04_scienceOfficer), FIGHTING_MALUS);
        medicalOfficer = new DF04CrewMemberValueHolder(getName(R.string.df04_medicalOfficer), FIGHTING_MALUS);
        engineeringOfficer = new DF04CrewMemberValueHolder(getName(R.string.df04_engineeringOfficer), FIGHTING_MALUS);
        ship = new DF04ShipValueHolder(getName(R.string.df04_ship), 6 + Die.SIX_FACES.roll(), 12 + Die.SIX_FACES.roll());
        securityOfficer = new DF04CrewMemberValueHolder(getName(R.string.df04_securityOfficer));
        securityAgent1 = new DF04CrewMemberValueHolder(getName(R.string.df04_securityAgent1));
        securityAgent2 = new DF04CrewMemberValueHolder(getName(R.string.df04_securityAgent2));
        int maxLuck = 6 + Die.SIX_FACES.roll();
        luck = new IntValueHolder(0, maxLuck, maxLuck);
        luckPenalty = new IntValueHolder(Constants.BIG_NEGATIVE, Constants.BIG_POSITIVE, 0);
        phaserState = DF04PhaserState.NONE;
    }

    @Override public void addLifeObserver(AdventureActivity activity) {
        commander.getStamina().addObserver(this);
        ship.getStamina().addObserver(this);
        addObserver(activity);
    }

    @Override public void deleteLifeObserver(AdventureActivity activity) {
        deleteObserver(activity);
        commander.getStamina().deleteObserver(this);
        ship.getStamina().deleteObserver(this);
    }

    @Override public void killWithoutUpdate() {
        commander.getStamina().deleteObservers();
        commander.getStamina().add(Constants.BIG_NEGATIVE);
    }

    @Override public boolean isDead() {
        return commander.getStamina().getValue() <= 0 || ship.getStamina().getValue() <= 0;
    }

    @Override public CombatStrategy getCombatStrategy() {
        return DF04CombatStrategy.INSTANCE;
    }

    @Override public void initCharacterValues() {
        super.initCharacterValues();
        CharacterValues.put(Property.COMMANDER, commander);
        CharacterValues.put(Property.SCIENCE_OFFICER, scienceOfficer);
        CharacterValues.put(Property.MEDICAL_OFFICER, medicalOfficer);
        CharacterValues.put(Property.ENGINEERING_OFFICER, engineeringOfficer);
        CharacterValues.put(Property.SHIP, ship);
        CharacterValues.put(Property.SECURITY_OFFICER, securityOfficer);
        CharacterValues.put(Property.SECURITY_AGENT_1, securityAgent1);
        CharacterValues.put(Property.SECURITY_AGENT_2, securityAgent2);
        CharacterValues.put(Property.LUCK, luck);
        CharacterValues.put(Property.LUCK_PENALTY, luckPenalty);
    }

    @Override public List<Fighter> getFighters() {
        List<Fighter> fighters = new ArrayList<>();
        addFighter(fighters, Property.COMMANDER);
        addFighter(fighters, Property.SCIENCE_OFFICER);
        addFighter(fighters, Property.MEDICAL_OFFICER);
        addFighter(fighters, Property.ENGINEERING_OFFICER);
        addFighter(fighters, Property.SHIP);
        addFighter(fighters, Property.SECURITY_OFFICER);
        addFighter(fighters, Property.SECURITY_AGENT_1);
        addFighter(fighters, Property.SECURITY_AGENT_2);
        return fighters;
    }

    public void leavePlanet() {
        Property.SCIENCE_OFFICER.get().leavePlanet();
        Property.MEDICAL_OFFICER.get().leavePlanet();
        Property.ENGINEERING_OFFICER.get().leavePlanet();
        Property.SECURITY_OFFICER.get().leavePlanet();
        Property.SECURITY_AGENT_1.get().leavePlanet();
        Property.SECURITY_AGENT_2.get().leavePlanet();
        int healAmount = Property.MEDICAL_OFFICER.get().canBeDragged() ? 2 : 1;
        Property.COMMANDER.get().getStamina().addWithFeedback(healAmount);
        Property.SCIENCE_OFFICER.get().getStamina().addWithFeedback(healAmount);
        Property.MEDICAL_OFFICER.get().getStamina().addWithFeedback(healAmount);
        Property.ENGINEERING_OFFICER.get().getStamina().addWithFeedback(healAmount);
        Property.SECURITY_OFFICER.get().getStamina().addWithFeedback(healAmount);
        Property.SECURITY_AGENT_1.get().getStamina().addWithFeedback(healAmount);
        Property.SECURITY_AGENT_2.get().getStamina().addWithFeedback(healAmount);
    }

    public void startSpaceShipFight() {
        Property.COMMANDER.get().startSpaceFight();
        Property.SCIENCE_OFFICER.get().startSpaceFight();
        Property.MEDICAL_OFFICER.get().startSpaceFight();
        Property.ENGINEERING_OFFICER.get().startSpaceFight();
        Property.SHIP.get().startSpaceFight();
        Property.SECURITY_OFFICER.get().startSpaceFight();
        Property.SECURITY_AGENT_1.get().startSpaceFight();
        Property.SECURITY_AGENT_2.get().startSpaceFight();
    }

    public void commanderDescendsOnPlanet() {
        Property.COMMANDER.get().endSpaceFight();
        Property.SCIENCE_OFFICER.get().endSpaceFight();
        Property.MEDICAL_OFFICER.get().endSpaceFight();
        Property.ENGINEERING_OFFICER.get().endSpaceFight();
        Property.SHIP.get().endSpaceFight();
        Property.SECURITY_OFFICER.get().endSpaceFight();
        Property.SECURITY_AGENT_1.get().endSpaceFight();
        Property.SECURITY_AGENT_2.get().endSpaceFight();
    }

    public DF04PhaserState getPhaserState() {
        return phaserState;
    }

    public void togglePhaserState() {
        phaserState = phaserState.toggle();
    }

    public boolean isInSpaceFight() {
        return Property.SHIP.get().isInSpaceFight();
    }

    private String getName(int resId) {
        return Utils.getString(resId);
    }

    private void addFighter(List<Fighter> fighters, Property<DF04AssetValueHolder> crewMemberProp) {
        if (crewMemberProp.get().isOnPlanet() || crewMemberProp.get().isInSpaceFight()) {
            fighters.add(new DF04CrewMemberFighter(crewMemberProp));
        }
    }
}
