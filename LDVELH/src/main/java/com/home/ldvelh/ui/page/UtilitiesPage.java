package com.home.ldvelh.ui.page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.home.ldvelh.R;
import com.home.ldvelh.ui.widget.PageInfo.Tag;
import com.home.ldvelh.ui.widget.utility.UtilityView;

public class UtilitiesPage extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_page_utilities, container, false);
        removeUnconfiguredUtilities((ViewGroup) layout.findViewById(R.id.utilities));
        return layout;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ResizeUtilities(view);
            }
        });
    }

    private void removeUnconfiguredUtilities(ViewGroup parentView) {
        for (Tag tag : Tag.values()) {
            Object tagData = tag.getData();
            if (tagData != null) {
                View utilityView = parentView.findViewById((Integer) tagData);
                if (utilityView != null) {
                    if (PagesAdapter.pageHasTag(R.string.tab_title_utilities, tag)) {
                        ((UtilityView) utilityView).initLayout();
                    } else {
                        parentView.removeView(utilityView);
                    }
                }
            }
        }
    }

    private void ResizeUtilities(View view) {
        ViewGroup utilitiesView = view.findViewById(R.id.utilities);
        int nbUtilities = utilitiesView.getChildCount();
        int utilitySize = utilitiesView.getChildAt(0).getHeight();
        int utilityPadding = utilitiesView.getChildAt(0).getPaddingTop();
        int availableWidth = utilitiesView.getWidth();
        if (nbUtilities * (utilitySize + 2*utilityPadding) > availableWidth) {
            utilitySize = availableWidth / nbUtilities - 2*utilityPadding;
            for (int i = 0; i < nbUtilities; i++) {
                resizeUtility(utilitiesView.getChildAt(i), utilitySize);
            }
        }
    }

    private void resizeUtility(View utilityView, int utilitySize) {
        ViewGroup.LayoutParams layoutParams = utilityView.getLayoutParams();
        layoutParams.width = utilitySize;
        layoutParams.height = utilitySize;
        utilityView.setLayoutParams(layoutParams);
    }
}
