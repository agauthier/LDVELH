package com.home.ldvelh.ui.widget;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.home.ldvelh.commons.Utils;

import android.support.v4.app.Fragment;

public class PageInfo<T extends Fragment> implements Serializable {
	private static final long serialVersionUID = 2114705063405529401L;

	public enum Tag {
		ALLOW_DROP, UTIL_LAST_PARAGRAPH, UTIL_DICE, UTIL_LUCK_CHECK, UTIL_ZEUS
	}

	private final int titleResId;
	private final Class<T> pageClass;
	private final Set<Tag> tags = new HashSet<>();

	public PageInfo(int titleResId, Class<T> pageClass, Set<Tag> tags) {
		this.titleResId = titleResId;
		this.pageClass = pageClass;
		this.tags.addAll(tags);
	}

	public int getTitleResId() {
		return titleResId;
	}

	public Class<T> getPageClass() {
		return pageClass;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public String getTitle() {
		return Utils.getString(titleResId);
	}
}
