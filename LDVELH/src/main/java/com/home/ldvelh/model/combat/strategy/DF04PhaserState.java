package com.home.ldvelh.model.combat.strategy;

import com.home.ldvelh.R;

public enum DF04PhaserState {
	NONE, PARALYZE, DEATH;
	
	public int getResId() {
		switch (this) {
		case NONE:
			return R.drawable.phaser_none;
		case PARALYZE:
			return R.drawable.phaser_paralyze;
		case DEATH:
			return R.drawable.phaser_death;
		default:
			return R.drawable.phaser_none;
		
		}
	}
	
	public DF04PhaserState toggle() {
		switch (this) {
		case NONE:
			return PARALYZE;
		case PARALYZE:
			return DEATH;
		case DEATH:
			return NONE;
		default:
			return NONE;
		}
	}
}
