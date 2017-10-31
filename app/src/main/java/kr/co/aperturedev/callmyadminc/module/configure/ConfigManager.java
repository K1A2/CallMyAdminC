package kr.co.aperturedev.callmyadminc.module.configure;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 5252b on 2017-10-31.
 * 콘피그 메니저
 */

public class ConfigManager {
    private Context context = null;
    private SharedPreferences sharedPrep = null;    // 읽기용
    private SharedPreferences.Editor editor = null; // 쓰기용

    public ConfigManager(String repository, Context context) {
        this.context = context;
        this.sharedPrep = context.getSharedPreferences(repository, Context.MODE_PRIVATE);
        this.editor = this.sharedPrep.edit();
    }

    public void put(String key, String value) {
        this.editor.putString(key, value);
        this.editor.commit();
    }

    public void put(String key, int value) {
        this.editor.putInt(key, value);
        this.editor.commit();
    }

    public void put(String key, boolean value) {
        this.editor.putBoolean(key, value);
        this.editor.commit();
    }

    public SharedPreferences get() {
        return this.sharedPrep;
    }
}
