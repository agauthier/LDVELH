package com.home.ldvelh;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.home.ldvelh.commons.Utils;
import com.home.ldvelh.model.Property;
import com.home.ldvelh.model.character.Character;
import com.home.ldvelh.model.character.CharacterValues;
import com.home.ldvelh.model.map.AdventureMap;
import com.home.ldvelh.model.value.MapValueHolder;
import com.home.ldvelh.ui.activity.AbstractGameActivity;
import com.home.ldvelh.ui.activity.AdventureActivity;
import com.home.ldvelh.ui.dialog.AdventureDialog;
import com.home.ldvelh.ui.dialog.NewCharacterConfirmation;
import com.home.ldvelh.ui.inflater.FreeAreaInflater;
import com.home.ldvelh.ui.inflater.FreeAreaInflaterDefault;
import com.home.ldvelh.ui.widget.PageInfo;
import com.home.ldvelh.ui.widget.PageTag;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;

public class AdventureConfig implements Serializable {
    private static final long serialVersionUID = 3331323114140703489L;

    private enum LoadingMode {
        NEW_CHARACTER, EXISTING_CHARACTER
    }

    private static class CustomSetter implements Serializable {
        private static final long serialVersionUID = -1824540496741673860L;

        private final String methodName;
        private final Object[] objects;

        CustomSetter(String methodName, Object... objects) {
            this.methodName = methodName;
            this.objects = objects;
        }

        void perform(Character character) {
            try {
                Class<?>[] classes = new Class<?>[objects.length];
                for (int i = 0; i < objects.length; i++) {
                    classes[i] = objects[i].getClass();
                }
                character.getClass().getMethod(methodName, classes).invoke(character, objects);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static final String ADVENTURE_CONFIG = "com.home.ldvelh.AdventureConfig";
    private static final String MAP_FILE_SUFFIX = "_map";
    private static final int END_OF_LIST = -1;

    private static final Handler handler = new Handler();

    private final int bookResId;
    private final int titleResId;
    private final Class<? extends AdventureActivity> activityClass;
    private final Class<? extends Character> characterClass;
    private final int layoutResId;
    private final int previousBookResId;
    private final Set<CustomSetter> customCharacterValues = new HashSet<>();
    private final List<Class<? extends AdventureDialog>> dialogs = new ArrayList<>();
    private final Map<Class<? extends AdventureDialog>, Object> dialogData = new HashMap<>();
    private final List<PageInfo<? extends Fragment>> pages = new ArrayList<>();
    private FreeAreaInflater freeAreaInflater = FreeAreaInflaterDefault.INSTANCE;
    private LoadingMode loadingMode;
    private int selectedBookResId;

    AdventureConfig(int bookResId, int titleResId, Class<? extends AdventureActivity> activityClass, Class<? extends Character> characterClass, int layoutResId) {
        this(bookResId, titleResId, activityClass, characterClass, layoutResId, 0);
    }

    AdventureConfig(int bookResId, int titleResId, Class<? extends AdventureActivity> activityClass, Class<? extends Character> characterClass, int layoutResId, int previousBookResId) {
        this.bookResId = bookResId;
        this.titleResId = titleResId;
        this.activityClass = activityClass;
        this.characterClass = characterClass;
        this.layoutResId = layoutResId;
        this.previousBookResId = previousBookResId;
    }

    public int getTitleResId() {
        return titleResId;
    }

    private <T extends AdventureActivity> Class<T> getActivityClass() {
        @SuppressWarnings("unchecked") Class<T> castClass = (Class<T>) activityClass;
        return castClass;
    }

    public int getLayoutResId() {
        return layoutResId;
    }

    void addCustomCharacterValues(String methodName, Object... objects) {
        customCharacterValues.add(new CustomSetter(methodName, objects));
    }

    <T extends Fragment> void addPage(int pageTitleResId, Class<T> pageClass) {
        addPage(pageTitleResId, pageClass, Collections.<PageTag>emptySet());
    }

    <T extends Fragment> void addPage(int pageTitleResId, Class<T> pageClass, Collection<PageTag> tags) {
        insertPage(END_OF_LIST, pageTitleResId, pageClass, tags);
    }

    @SuppressWarnings("SameParameterValue")
    <T extends Fragment> void insertPage(int nextPageTitleResId, int pageTitleResId, Class<T> pageClass) {
        insertPage(nextPageTitleResId, pageTitleResId, pageClass, Collections.<PageTag>emptySet());
    }

    private <T extends Fragment> void insertPage(int nextPageTitleResId, int pageTitleResId, Class<T> pageClass, Collection<PageTag> tags) {
        if (nextPageTitleResId == END_OF_LIST) {
            pages.add(new PageInfo<>(pageTitleResId, pageClass, tags));
        } else {
            for (int i = 0; i < pages.size(); i++) {
                if (pages.get(i).getTitleResId() == nextPageTitleResId) {
                    pages.add(i, new PageInfo<>(pageTitleResId, pageClass, tags));
                    break;
                }
            }
        }
    }

    public <T extends Fragment> void enablePage(Class<T> pageClass, boolean enabled) {
        for (PageInfo pageInfo : pages) {
            if (pageInfo.getPageClass() == pageClass) {
                pageInfo.enable(enabled);
                break;
            }
        }
    }

    <T extends AdventureDialog> void addDialog(Class<T> dialog) {
        addDialog(dialog, null);
    }

    <T extends AdventureDialog> void addDialog(Class<T> dialog, Object data) {
        this.dialogs.add(dialog);
        this.dialogData.put(dialog, data);
    }

    public <T extends AdventureDialog> List<Class<T>> getUnfulfilledDialogs() {
        List<Class<T>> unfulfilledDialogs = new ArrayList<>();
        for (Class<? extends AdventureDialog> dialogClass : dialogs) {
            //noinspection SuspiciousMethodCalls
            if (!Property.CHARACTER.get().getFulfilledDialogs().contains(dialogClass)) {
                @SuppressWarnings("unchecked") final Class<T> dialogClassCast = (Class<T>) dialogClass;
                unfulfilledDialogs.add(dialogClassCast);
            }
        }
        return unfulfilledDialogs;
    }

    public <T extends AdventureDialog> Object getDialogData(Class<T> dialogClass) {
        return dialogData.get(dialogClass);
    }

    public <T extends Fragment> List<PageInfo<T>> getPages() {
        List<PageInfo<T>> list = new ArrayList<>();
        for (PageInfo<? extends Fragment> item : pages) {
            @SuppressWarnings("unchecked") PageInfo<T> castItem = (PageInfo<T>) item;
            list.add(castItem);
        }
        return list;
    }

    void setFreeAreaInflater(FreeAreaInflater freeAreaInflater) {
        this.freeAreaInflater = freeAreaInflater;
    }

    public FreeAreaInflater getFreeAreaInflater() {
        return freeAreaInflater;
    }

    public void launch() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacks(this);
                beginLaunch();
            }
        });
    }

    public void launchNewDefaultCharacter() {
        completeLaunch(LoadingMode.NEW_CHARACTER, bookResId);
    }

    public void launchExistingPreviousCharacter() {
        completeLaunch(LoadingMode.EXISTING_CHARACTER, previousBookResId);
    }

    public void loadGame() {
        switch (loadingMode) {
            case NEW_CHARACTER:
                createDefaultCharacter();
                loadingMode = LoadingMode.EXISTING_CHARACTER;
                break;
            case EXISTING_CHARACTER:
                loadCharacter(selectedBookResId);
                if (selectedBookResId == previousBookResId) {
                    Property.CHARACTER.get().startFromPreviousBook();
                }
                break;
        }
        loadMap();
    }

    public void saveGame(Character character) {
        String bookName = Utils.getString(bookResId);
        try {
            Utils.saveObject(bookName, character);
            Utils.saveObject(bookName + MAP_FILE_SUFFIX, Property.MAP.get());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void beginLaunch() {
        Character defaultCharacter = loadCharacter(bookResId);
        if (defaultCharacter == null || defaultCharacter.isDead()) {
            Character previousCharacter = loadCharacter(previousBookResId);
            if (previousCharacter == null || previousCharacter.isDead()) {
                launchNewDefaultCharacter();
            } else {
                new NewCharacterConfirmation(AbstractGameActivity.getCurrentActivity(), this).show();
            }
        } else {
            completeLaunch(LoadingMode.EXISTING_CHARACTER, bookResId);
        }
    }

    private void completeLaunch(LoadingMode loadingMode, int selectedBookResId) {
        this.loadingMode = loadingMode;
        this.selectedBookResId = selectedBookResId;
        Context context = AbstractGameActivity.getCurrentActivity();
        Intent intent = new Intent(context, getActivityClass());
        intent.putExtra(AdventureConfig.ADVENTURE_CONFIG, this);
        context.startActivity(intent);
    }

    private Character loadCharacter(int selectedBookResId) {
        try {
            String bookName = Utils.getString(selectedBookResId);
            Character character = characterClass.cast(Utils.loadObject(bookName));
            character.initCharacterValues();
            return character;
        } catch (Exception loadingError) {
            return null;
        }
    }

    private void loadMap() {
        MapValueHolder map = null;
        try {
            String fileName = Utils.getString(bookResId) + MAP_FILE_SUFFIX;
            map = MapValueHolder.class.cast(Utils.loadObject(fileName));
        } catch (Exception ignored) {}
        CharacterValues.put(Property.MAP, (map != null) ? map : new MapValueHolder(new AdventureMap()));
    }

    private void createDefaultCharacter() {
        try {
            Character character = characterClass.newInstance();
            character.initCharacterValues();
            for (CustomSetter setter : customCharacterValues) {
                setter.perform(character);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
