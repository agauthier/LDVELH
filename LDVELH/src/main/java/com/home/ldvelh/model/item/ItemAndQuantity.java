package com.home.ldvelh.model.item;

import org.apache.commons.lang3.StringUtils;

import android.util.Pair;

public class ItemAndQuantity {

	static final String QTY_SEPARATOR = ":";
	private static final String QTY_SEPARATOR_REGEX = "\\s*:\\s*";

	private final Pair<String, Integer> itemAndQty;

	public ItemAndQuantity(String name) {
		this.itemAndQty = parseStringInit(name);
	}

	public ItemAndQuantity(String name, int quantity) {
		this.itemAndQty = new Pair<>(name.trim(), quantity);
	}

	public String getName() {
		return itemAndQty.first;
	}

	public int getQuantity() {
		return itemAndQty.second;
	}

	private Pair<String, Integer> parseStringInit(String initialName) {
		String name = StringUtils.EMPTY;
		int quantity = 0;
		String trimmedName = initialName.trim();
		if (!trimmedName.isEmpty()) {
			String[] tokens = trimmedName.split(QTY_SEPARATOR_REGEX);
			if (tokens.length < 2 || !isInteger(tokens[tokens.length - 1])) {
				quantity = 1;
				name = trimmedName;
			} else {
				quantity = Integer.valueOf(tokens[tokens.length - 1]);
				name = trimmedName.substring(0, trimmedName.lastIndexOf(QTY_SEPARATOR)).trim();
			}
		}
		return new Pair<>(name, quantity);
	}

	private boolean isInteger(String s) {
		try {
			//noinspection ResultOfMethodCallIgnored
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
