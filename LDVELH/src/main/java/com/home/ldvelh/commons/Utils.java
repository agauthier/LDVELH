package com.home.ldvelh.commons;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.home.ldvelh.ui.activity.AbstractGameActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Utils {

    private static boolean keyboardShowing = false;

    public static <T> Class<T> getClass(String className) {
        if (className != null) {
            try {
                @SuppressWarnings("unchecked") final Class<T> resolvedClass = (Class<T>) Class.forName(className);
                return resolvedClass;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return null;
    }

    public static String signedIntToString(int value) {
        return ((value > 0) ? "+" : "") + value;
    }

    public static String getString(int stringResId) {
        return AbstractGameActivity.getResourcesString(stringResId);
    }

    public static Object loadObject(String fileName) throws Exception {
        FileInputStream fis = AbstractGameActivity.getCurrentActivity().openFileInput(fileName);
        ObjectInputStream is = new ObjectInputStream(fis);
        Object object = is.readObject();
        is.close();
        fis.close();
        return object;
    }

    public static void saveObject(String fileName, Object object) throws IOException {
        FileOutputStream fos = AbstractGameActivity.getCurrentActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
        ObjectOutputStream os = new ObjectOutputStream(fos);
        os.writeObject(object);
        os.close();
        fos.close();
    }

    public static void hideKeyboard(View attachedView) {
        InputMethodManager inputMethodManager = (InputMethodManager) AbstractGameActivity.getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(attachedView.getWindowToken(), 0);
            attachedView.clearFocus();
        }
        keyboardShowing = false;
    }

    public static boolean isKeyboardShowing() {
        return keyboardShowing;
    }
}
