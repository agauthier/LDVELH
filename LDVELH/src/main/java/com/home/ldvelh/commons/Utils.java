package com.home.ldvelh.commons;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.home.ldvelh.ui.activity.AbstractGameActivity;

import android.content.Context;

public class Utils {

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
}
