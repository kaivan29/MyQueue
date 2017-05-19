package cs371m.myqueue;

// for reference look at
// http://stackoverflow.com/questions/10393273/using-sharedpreferences-across-multiple-classes

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.Map;

public class HelperSharedPreferences {

    //SharedPreferences, key = movie:::id. value = title
    public static final String key1_prefix="movie:::";
    public static final String key2_prefix="type_media:::";

    public static void putSharedPreferencesInt(Context context, String key, int value){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        Editor edit=preferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static void putSharedPreferencesBoolean(Context context, String key, boolean val){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        Editor edit=preferences.edit();
        edit.putBoolean(key, val);
        edit.commit();
    }

    public static void putSharedPreferencesString(Context context, String key, String val){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        Editor edit=preferences.edit();
        edit.putString(key, val);
        edit.commit();
    }

    public static void putSharedPreferencesFloat(Context context, String key, float val){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        Editor edit=preferences.edit();
        edit.putFloat(key, val);
        edit.commit();
    }

    public static void putSharedPreferencesLong(Context context, String key, long val){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        Editor edit=preferences.edit();
        edit.putLong(key, val);
        edit.commit();
    }

    public static long getSharedPreferencesLong(Context context, String key, long _default){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(key, _default);
    }

    public static float getSharedPreferencesFloat(Context context, String key, float _default){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getFloat(key, _default);
    }

    public static String getSharedPreferencesString(Context context, String key, String _default){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, _default);
    }

    public static int getSharedPreferencesInt(Context context, String key, int _default){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, _default);
    }

    public static boolean getSharedPreferencesBoolean(Context context, String key,
                                                      boolean _default){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, _default);
    }

    public static void deleteSharedPreferencesKey(Context context, String key){
        SharedPreferences mySPrefs =PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove(key);
        editor.apply();
    }

    public static Map<String,?> getAll(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Map<String, ?> prefsMap = preferences.getAll();
        return prefsMap;
    }
}