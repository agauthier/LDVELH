package com.home.ldvelh.ui.page;

import java.util.Observable;
import java.util.Observer;

import com.home.ldvelh.R;
import com.home.ldvelh.model.combat.CombatRow.Team;
import com.home.ldvelh.ui.widget.list.CombatRowList;
import com.home.ldvelh.model.combat.DFCombat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class DFCombatPage extends Fragment implements Observer {

	private DFCombat combat;

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_page_df_combat, container, false);
	}

	@Override public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		combat = new DFCombat();
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
		ImageButton addGoodguyButton = getView().findViewById(R.id.addGoodguyButton);
		addGoodguyButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				combat.addEditableFighter(Team.GOODGUYS);
			}
		});
		ImageButton addMonsterButton = getView().findViewById(R.id.addMonsterButton);
		addMonsterButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				combat.addEditableFighter(Team.MONSTERS);
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
		ImageButton escapeButton = getView().findViewById(R.id.escape);
		escapeButton.setEnabled(combat.canEscape());
		escapeButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				combat.escape();
			}
		});
		ImageButton assaultButton = getView().findViewById(R.id.assault);
		assaultButton.setEnabled(combat.canAssault());
		assaultButton.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				combat.assault();
			}
		});
	}
}
