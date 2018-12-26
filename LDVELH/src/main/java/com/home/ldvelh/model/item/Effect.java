package com.home.ldvelh.model.item;

import java.io.Serializable;

import com.home.ldvelh.commons.Constants;
import com.home.ldvelh.commons.Utils;

public class Effect implements Serializable {
	private static final long serialVersionUID = -4965256408880833466L;

	public final static int MAX = Constants.BIG_POSITIVE;
	public final static int HALF_OF_MAX_ROUNDED_UP = Constants.BIG_NEGATIVE - 1000;
	public final static int HALF_OF_MAX_ROUNDED_DOWN = Constants.BIG_NEGATIVE - 1001;

	static class EffectAmount implements Serializable {
		private static final long serialVersionUID = -2377501984499607461L;

		private final int amount;

		EffectAmount(int amount) {
			this.amount = amount;
		}

		@Override public String toString() {
			switch (amount) {
			case MAX:
				return "Max";
			case HALF_OF_MAX_ROUNDED_UP:
				return "Half of Max Rounded Up";
			case HALF_OF_MAX_ROUNDED_DOWN:
				return "Half of Max Rounded Down";
			default:
				return Utils.signedIntToString(amount);
			}
		}

		void apply(EffectTarget target, boolean twice) {
			switch (amount) {
			case HALF_OF_MAX_ROUNDED_UP:
				if (twice) {
					target.gain(MAX);
				} else {
					target.gainHalfOfMax(true);
				}
				break;
			case HALF_OF_MAX_ROUNDED_DOWN:
				if (twice) {
					target.gain(MAX);
				} else {
					target.gainHalfOfMax(false);
				}
				break;
			default:
				if (twice) {
					target.gain(2 * amount);
				} else {
					target.gain(amount);
				}
			}
		}
	}

	private final EffectTarget target;
	private final EffectAmount amount;

	public Effect(EffectTarget target, int amount) {
		this.target = target;
		this.amount = new EffectAmount(amount);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Effect))
			return false;
		Effect effect = (Effect) o;
		return target == effect.target && amount.amount == effect.amount.amount;
	}

	@Override
	public int hashCode() {
		int result = target.hashCode();
		result = 31 * result + amount.amount;
		return result;
	}

	@Override public String toString() {
		return Utils.getString(target.getResId()) + ": " + amount;
	}

	void apply() {
		amount.apply(target, false);
	}

	void applyTwice() {
		amount.apply(target, true);
	}
}
