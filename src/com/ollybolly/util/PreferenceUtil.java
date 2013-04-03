package com.ollybolly.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtil {

	/**
	 * @see String Data Save.
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putPreference(Context context, String key, String value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();

		editor.putString(key, value);
		editor.commit();
		editor.clear();
	}

	/**
	 * @see Boolean Data Save.
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putPreference(Context context, String key, boolean value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();

		editor.putBoolean(key, value);
		editor.commit();
		editor.clear();
	}

	/**
	 * @see Integer Data Save.
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putPreference(Context context, String key, int value) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();

		editor.putInt(key, value);
		editor.commit();
		editor.clear();
	}

	/**
	 * @see String Data read.
	 * @param context
	 * @param key
	 * @return read values, null.
	 */
	public static String getPreference(Context context, String key) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		return prefs.getString(key, "");
	}

	/**
	 * @see Boolean Data read.
	 * @param context
	 * @param key
	 * @return read values, false.
	 */
	public static boolean getBooleanPreference(Context context, String key) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		return prefs.getBoolean(key, false);
	}

	/**
	 * @see Int Data read.
	 * @param context
	 * @param key
	 * @return read values, 0
	 */
	public static int getIntPreference(Context context, String key) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		return prefs.getInt(key, 0);
	}

}// end of class
