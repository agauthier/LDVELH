package com.home.ldvelh.ui.page;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.home.ldvelh.R;
import com.home.ldvelh.ui.widget.PageTag;
import com.home.ldvelh.ui.widget.utility.UtilityView;

import java.lang.reflect.Constructor;

import static com.home.ldvelh.ui.widget.PageTag.TagType.UTILITY;

public class UtilitiesPage extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_page_utilities, container, false);
        addUtilities((ViewGroup) layout.findViewById(R.id.utilities));
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ResizeUtilities(view);
            }
        });
    }

    private <T extends UtilityView> void addUtilities(ViewGroup utilityBar) {
        for (PageTag tag : PagesAdapter.getPageTags(R.string.tab_title_utilities, UTILITY)) {
            try {
                @SuppressWarnings("unchecked") Constructor<T> constructor = ((Class<T>) tag.getData()).getDeclaredConstructor(Context.class, AttributeSet.class);
                T utility = constructor.newInstance(getContext(), null);
                utility.initLayout();
                utilityBar.addView(utility);
            } catch (Exception e) {
                throw new IllegalStateException();
            }
        }
    }

    private void ResizeUtilities(View view) {
        ViewGroup utilitiesView = view.findViewById(R.id.utilities);
        int nbUtilities = utilitiesView.getChildCount();
        int utilitySize = utilitiesView.getChildAt(0).getHeight();
        int utilityPadding = utilitiesView.getChildAt(0).getPaddingTop();
        int availableWidth = utilitiesView.getWidth();
        if (nbUtilities * (utilitySize + 2 * utilityPadding) > availableWidth) {
            utilitySize = availableWidth / nbUtilities - 2 * utilityPadding;
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
