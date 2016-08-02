package Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import org.json.JSONObject;

import Constants.SharedPreferencesKeys;

/**
 * Created by shivangshah on 5/30/16.
 */
public class SharedPreferencesUtilities {

    public static void updateSharedPrefSessionCookie(Context context, String cookie){
        SharedPreferences preferences = context.getSharedPreferences(SharedPreferencesKeys.SESSION_COOKIE_SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SharedPreferencesKeys.SESSION_COOKIE_KEY, cookie);
        editor.commit();

    }

    public static String getSharedPrefSessionCookie(Context context){
        String sessionCookie = "";
        SharedPreferences prfs = context.getSharedPreferences(SharedPreferencesKeys.SESSION_COOKIE_SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        if(prfs != null){
            sessionCookie = prfs.getString(SharedPreferencesKeys.SESSION_COOKIE_KEY, "");
            sessionCookie = sessionCookie.replaceAll("\"", "");
        }
        return sessionCookie;
    }

    public static void deleteSharedPreferenceSessionCookie(Context context){
        SharedPreferences preferences = context.getSharedPreferences(SharedPreferencesKeys.SESSION_COOKIE_SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SharedPreferencesKeys.SESSION_COOKIE_KEY,"");
        editor.commit();
    }
}
