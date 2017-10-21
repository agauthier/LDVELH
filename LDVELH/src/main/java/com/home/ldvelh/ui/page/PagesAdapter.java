package com.home.ldvelh.ui.page;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.home.ldvelh.ui.widget.PageInfo;
import com.home.ldvelh.ui.widget.PageInfo.Tag;

import java.util.ArrayList;
import java.util.List;

import static com.home.ldvelh.ui.widget.PageInfo.Tag.ALLOW_DROP;

public class PagesAdapter extends FragmentPagerAdapter {

    private static int selectedPageTitleResId;
    private static final List<PageInfo<? extends Fragment>> pageInfos = new ArrayList<>();

    public <T extends Fragment> PagesAdapter(FragmentManager fragmentManager, List<PageInfo<T>> infos) {
        super(fragmentManager);
        pageInfos.clear();
        addEnabledPageInfos(infos);
        selectedPageTitleResId = pageInfos.get(0).getTitleResId();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment newPage;
        try {
            newPage = pageInfos.get(position).getPageClass().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return newPage;
    }

    @Override
    public int getCount() {
        return pageInfos.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageInfos.get(position).getTitle();
    }

    public static void setSelectedPage(int position) {
        selectedPageTitleResId = pageInfos.get(position).getTitleResId();
    }

    public static boolean selectedPageHasTag() {
        return pageHasTag(selectedPageTitleResId, ALLOW_DROP);
    }

    static boolean pageHasTag(int titleResId, Tag tag) {
        for (PageInfo<? extends Fragment> pageInfo : pageInfos) {
            if (pageInfo.getTitleResId() == titleResId) {
                return pageInfo.getTags().contains(tag);
            }
        }
        throw new IllegalStateException();
    }

    private <T extends Fragment> void addEnabledPageInfos(List<PageInfo<T>> pageInfos) {
        for (PageInfo<T> pageInfo : pageInfos) {
            if (pageInfo.isEnabled()) {
                PagesAdapter.pageInfos.add(pageInfo);
            }
        }
    }
}
