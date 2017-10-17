package com.home.ldvelh.ui.page;

import java.util.Observable;
import java.util.Observer;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.character.DF04Character;
import com.home.ldvelh.model.combat.CombatRow.Team;
import com.home.ldvelh.model.combat.DF04Combat;
import com.home.ldvelh.model.value.DF04AssetValueHolder;
import com.home.ldvelh.ui.widget.list.CombatRowList;

import android.content.ClipDescription;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class DF04CombatPage extends Fragment implements Observer {

	private DF04Combat combat;

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.fragment_page_df04_combat, container, false);
		fragmentView.setOnDragListener(new OnDragListener() {
			@Override public boolean onDrag(View v, DragEvent event) {
				switch (event.getAction()) {
				case DragEvent.ACTION_DRAG_STARTED:
					return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
					case DragEvent.ACTION_DROP:
					CharSequence dragData = event.getClipData().getItemAt(0).getText();
					Property<DF04AssetValueHolder> crewMember = Property.getPropertyByName(dragData.toString());
					if (crewMember.get().isCommander()) {
						((DF04Character) Property.CHARACTER.get()).commanderDescendsOnPlanet();
					} else if (crewMember.get().isShip()) {
						((DF04Character) Property.CHARACTER.get()).startSpaceShipFight();
					} else {
						crewMember.get().descendOnPlanet();
					}
					combat.reset();
					return true;
				case DragEvent.ACTION_DRAG_ENTERED:
				case DragEvent.ACTION_DRAG_LOCATION:
				case DragEvent.ACTION_DRAG_EXITED:
				case DragEvent.ACTION_DRAG_ENDED:
					return true;
				default:
					return false;
				}
			}
		});
		return fragmentView;
	}

	@Override public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		combat = new DF04Combat();
		combat.init();
		initButtons();
	}

	@Override public void onResume() {
		super.onResume();
		combat.addAllObservers();
		((CombatRowList) getActivity().findViewById(R.id.combatList)).addObserver(this);
	}

	@Override public void onPause() {
		super.onPause();
		combat.removeAllObservers();
		((CombatRowList) getActivity().findViewById(R.id.combatList)).deleteObserver(this);
	}

	@Override public void update(Observable observable, Object data) {
		initButtons();
	}

	private void initButtons() {
		ImageButton addMonsterButton = getView().findViewById(R.id.addMonsterButton);
		addMonsterButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				combat.addEditableFighter(Team.MONSTERS);
			}
		});
		final ImageButton phaserButton = getView().findViewById(R.id.phaserButton);
		final DF04Character character = (DF04Character) Property.CHARACTER.get();
		phaserButton.setImageResource(character.getPhaserState().getResId());
		phaserButton.setEnabled(combat.canUsePhaser());
		phaserButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				character.togglePhaserState();
				phaserButton.setImageResource(character.getPhaserState().getResId());
			}
		});
		ImageButton upButton = getView().findViewById(R.id.upButton);
		upButton.setEnabled(combat.canMoveUp());
		upButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				combat.moveUp();
			}
		});
		ImageButton downButton = getView().findViewById(R.id.downButton);
		downButton.setEnabled(combat.canMoveDown());
		downButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				combat.moveDown();
			}
		});
		ImageButton resetButton = getView().findViewById(R.id.reset);
		resetButton.setEnabled(combat.canReset());
		resetButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				combat.reset();
			}
		});
		ImageButton assaultButton = getView().findViewById(R.id.assault);
		assaultButton.setEnabled(combat.canAssault());
		assaultButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				combat.assault();
			}
		});
		ImageButton leavePlanetButton = getView().findViewById(R.id.leavePlanet);
		leavePlanetButton.setEnabled(combat.canLeavePlanet());
		leavePlanetButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				combat.leavePlanet();
			}
		});
	}
}
