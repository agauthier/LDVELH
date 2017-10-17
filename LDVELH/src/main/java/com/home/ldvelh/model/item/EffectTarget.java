package com.home.ldvelh.model.item;

import com.home.ldvelh.R;
import com.home.ldvelh.model.Property;

public enum EffectTarget {

	SKILL(R.string.skill) {
		@Override public void gain(int amount) {
			Property.SKILL.get().addWithFeedback(amount);
		}

		@Override public void gainHalfOfMax(boolean roundUp) {
			gain((Property.SKILL.get().getMax() + (roundUp ? 1 : 0)) / 2);
		}
	},
	MAX_SKILL(R.string.max_skill) {
		@Override public void gain(int amount) {
			Property.SKILL.get().addToMaxWithFeedback(amount);
		}
	},
	STAMINA(R.string.stamina) {
		@Override public void gain(int amount) {
			Property.STAMINA.get().addWithFeedback(amount);
		}

		@Override public void gainHalfOfMax(boolean roundUp) {
			gain((Property.STAMINA.get().getMax() + (roundUp ? 1 : 0)) / 2);
		}
	},
	MAX_STAMINA(R.string.max_stamina) {
		@Override public void gain(int amount) {
			Property.STAMINA.get().addToMaxWithFeedback(amount);
		}
	},
	LUCK(R.string.luck) {
		@Override public void gain(int amount) {
			Property.LUCK.get().addWithFeedback(amount);
		}

		@Override public void gainHalfOfMax(boolean roundUp) {
			gain((Property.LUCK.get().getMax() + (roundUp ? 1 : 0)) / 2);
		}
	},
	MAX_LUCK(R.string.max_luck) {
		@Override public void gain(int amount) {
			Property.LUCK.get().addToMaxWithFeedback(amount);
		}
	};

	private final int nameResId;

	EffectTarget(int nameResId) {
		this.nameResId = nameResId;
	}

	public int getResId() {
		return nameResId;
	}

	public abstract void gain(int amount);

	public void gainHalfOfMax(boolean roundUp) {
		throw new UnsupportedOperationException("Not implemented");
	}
}
