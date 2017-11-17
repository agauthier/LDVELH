package com.home.ldvelh.ui.widget;

import android.support.v4.app.Fragment;

import com.home.ldvelh.commons.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class PageInfo<T extends Fragment> implements Serializable {
    private static final long serialVersionUID = 6372350735737692242L;

    private final int titleResId;
    private final Class<T> pageClass;
    private final Collection<PageTag> tags = new ArrayList<>();
    private boolean enabled;

    public PageInfo(int titleResId, Class<T> pageClass, Collection<PageTag> tags) {
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

    public Collection<PageTag> getTags(PageTag.TagType type) {
        Collection<PageTag> tagsOfType = new ArrayList<>();
        for (PageTag tag : tags) {
            if (tag.getType() == type) {
                tagsOfType.add(tag);
            }
        }
        return tagsOfType;
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
