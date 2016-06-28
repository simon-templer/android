package ch.templer.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

import ch.templer.activities.SplashScreenActivity;

/**
 * Created by Templer on 6/25/2016.
 */
public class SettingsService {
    private static SettingsService settingsService;
    public static String PREV_SCENARIO_ID ="PREV_SCENARIO_ID";
    public static String PREV_SCENARIO_POSITION ="PREV_SCENARIO_POSITION";
    public static String PREV_SCENARIO_NOT_FINISHED ="PREV_SCENARIO_NOT_FINISHED";

    private SharedPreferences sharedPref;

    private SettingsService(){

    }
    private SettingsService(Context context ){
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SettingsService getInstance() {
        return settingsService;
    }

    public boolean getBooleanSetting(String key, boolean defaultValue){
        return sharedPref.getBoolean(key, defaultValue);
    }

    public long getLongSetting(String key, long defaultValue){
        return sharedPref.getLong(key, defaultValue);
    }

    public int getIntSetting(String key, int defaultValue){
        return sharedPref.getInt(key, defaultValue);
    }

    public void setIntegerSettting(String id, int value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(id, value);
        editor.commit();
    }

    public void setLongSettting(String id, long value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(id, value);
        editor.commit();
    }

    public void setBolleanSettting(String id, boolean value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(id, value);
        editor.commit();
    }

    public static void init(Context context) {
        if (settingsService == null){
            settingsService =  new SettingsService(context);
        }
    }
}
