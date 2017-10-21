package com.home.ldvelh.ui.widget;

import android.support.v4.app.Fragment;

import com.home.ldvelh.commons.Utils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class PageInfo<T extends Fragment> implements Serializable {
    private static final long serialVersionUID = 8282251537023818566L;

    public enum Tag {
        ALLOW_DROP, UTIL_LAST_PARAGRAPH, UTIL_DICE, UTIL_LUCK_CHECK, UTIL_ZEUS
    }

    private final int titleResId;
    private final Class<T> pageClass;
    private final Set<Tag> tags = new HashSet<>();
    private boolean enabled;

    public PageInfo(int titleResId, Class<T> pageClass, Set<Tag> tags) {
        this.titleResId = titleResId;
        this.pageClass = pageClass;
        this.tags.addAll(tags);
        this.enabled = true;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void enable(boolean enabled) {
        this.enabled = enabled;
    }
}
