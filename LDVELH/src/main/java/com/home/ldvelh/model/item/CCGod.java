package com.home.ldvelh.model.item;

import java.util.Arrays;
import java.util.List;

import com.home.ldvelh.R;
import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.value.ListValueHolder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

public class CCGod extends Item {
	private static final long serialVersionUID = -1384541768691074999L;

	public enum God {
		APHRODITE(R.string.cc_aphrodite),
		APOLLON(R.string.cc_apollon), 
		ARES(R.string.cc_ares), 
		ATHENA(R.string.cc_athena), 
		HERA(R.string.cc_hera), 
		POSEIDON(R.string.cc_poseidon), 
		ASCLEPIOS(R.string.cc_asclepios), 
		DEMETER(R.string.cc_demeter), 
		DIONYSOS(R.string.cc_dionysos), 
		ERIS(R.string.cc_eris), 
		FURIES(R.string.cc_furies), 
		HECATE(R.string.cc_hecate), 
		HEPHAISTOS(R.string.cc_hephaistos), 
		NO_GOD(R.string.empty_string);

		private final int nameResId;

		God(int nameResId) {
			this.nameResId = nameResId;
		}

		public int getNameResId() {
			return nameResId;
		}

		public static void populateList(ListValueHolder<CCGod> gods) {
			for (God god : values()) {
				if (god != NO_GOD) {
					gods.add(Utils.getString(god.nameResId), god);
				}
			}
		}

		public static List<God> tutelaryGods() {
			return Arrays.asList(APHRODITE, APOLLON, ARES, ATHENA, HERA, POSEIDON);
		}
	}

	public enum Attitude {
		FAVORABLE(R.id.favorable), NEUTRAL(R.id.neutral), UNFAVORABLE(R.id.unfavorable);

		private final int buttonResId;

		Attitude(int buttonResId) {
			this.buttonResId = buttonResId;
		}

		private void initButton(final CCGod god, final Attitude selectedAttitude, final View view) {
			final Attitude thisAttitude = this;
			RadioButton button = view.findViewById(buttonResId);
			button.setChecked(selectedAttitude == this);
			button.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View v) {
					god.setAttitude(thisAttitude);
					selectAttitude(view, thisAttitude);
				}
			});
		}

		static void selectAttitude(View view, Attitude selectedAttitude) {
			for (Attitude attitude : values()) {
				RadioButton button = view.findViewById(attitude.buttonResId);
				button.setChecked(attitude == selectedAttitude);
			}
		}

		public static void initButtons(CCGod god, Attitude selectedAttitude, View view) {
			for (Attitude attitude : values()) {
				attitude.initButton(god, selectedAttitude, view);
			}
		}
	}

	private Attitude attitude = Attitude.NEUTRAL;

	private CCGod() {}

	private <T extends ListItem> CCGod(ItemAndQuantity itemAndQty, List<Effect> effects, Object data, ListValueHolder<T> list) {
		super(itemAndQty, effects, data, list);
	}

	@Override public <T extends ListItem> T create(ItemAndQuantity itemAndQty, List<Effect> effects, Object data, ListValueHolder<T> list) {
		@SuppressWarnings("unchecked") T newItem = (T) new CCGod(itemAndQty, effects, data, list);
		return newItem;
	}

	@Override public void increment() {}

	@Override public void decrement() {}

	@Override public void add(int quantity) {}

	@Override public void subtract(int amount) {}

	@Override public boolean hasName(String name) {
		return false;
	}

	@Override public void initView(View row) {
		super.initView(row);
		Attitude.initButtons(this, attitude, row);
	}

	public void setAttitude(Attitude attitude) {
		this.attitude = attitude;
	}
}
