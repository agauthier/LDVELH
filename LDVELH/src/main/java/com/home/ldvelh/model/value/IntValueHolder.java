package com.home.ldvelh.model.value;

public class IntValueHolder extends ValueHolder<Integer> {
	private static final long serialVersionUID = -3149159279803812821L;

	public enum WatchType {
		MIN, MAX, VALUE
	}

	private int min;
	private int max;
	private int minDiff;
	private int maxDiff;
	private int valueDiff;

	public IntValueHolder(int min, int max, int value) {
		super(0);
		this.min = min;
		this.max = max;
		setValue(value);
		validate();
	}

	public IntValueHolder(IntValueHolder otherInt) {
		this(otherInt.getMin(), otherInt.getMax(), otherInt.getValue());
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public int getMinDiff() {
		return minDiff;
	}

	public int getMaxDiff() {
		return maxDiff;
	}

	public int getValueDiff() {
		return valueDiff;
	}

	public void addToMin(int amount) {
		setMin(min + amount, false);
	}

	@SuppressWarnings("unused") public void addToMinWithFeedback(int amount) {
		setMin(min + amount, true);
	}
	public void addToMax(int amount) {
		setMax(max + amount, false);
	}

	public void addToMaxWithFeedback(int amount) {
		setMax(max + amount, true);
	}

	public void add(int amount) {
		addWithFeedback(amount, false);
	}

	public void addWithFeedback(int amount) {
		addWithFeedback(amount, true);
	}

	private void validate() {
		if (min > max || getValue() < min || getValue() > max) {
			throw new IllegalArgumentException("min: " + min + ", max: " + max + ", value: " + getValue());
		}
	}

	private void setMin(int minToSet, boolean feedback) {
		resetDiffs();
		int newMin = minToSet;
		if (newMin > max) {
			newMin = max;
		}
		if (newMin != min) {
			minDiff = newMin - min;
			min = newMin;
			if (getValue() < min) {
				valueDiff = min - getValue();
				setValue(min);
			}
			notifyObservers(feedback);
		}
	}

	private void setMax(int maxToSet, boolean feedback) {
		resetDiffs();
		int newMax = maxToSet;
		if (newMax < min) {
			newMax = min;
		}
		if (newMax != max) {
			maxDiff = newMax - max;
			max = newMax;
			if (getValue() > max) {
				valueDiff = max - getValue();
				setValue(max);
			}
			notifyObservers(feedback);
		}
	}

	private void addWithFeedback(int amount, boolean feedback) {
		resetDiffs();
		int newValue = getValue() + amount;
		if (newValue < min) {
			newValue = min;
		}
		if (newValue > max) {
			newValue = max;
		}
		if (newValue != getValue()) {
			valueDiff = newValue - getValue();
			setValue(newValue, feedback);
		}
	}

	private void resetDiffs() {
		minDiff = 0;
		maxDiff = 0;
		valueDiff = 0;
	}
}
