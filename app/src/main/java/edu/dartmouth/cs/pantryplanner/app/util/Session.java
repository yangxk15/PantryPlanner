package edu.dartmouth.cs.pantryplanner.app.util;

import android.content.Context;
import android.content.SharedPreferences;

import edu.dartmouth.cs.pantryplanner.app.R;
import edu.dartmouth.cs.pantryplanner.app.controller.LoginActivity;
import lombok.AllArgsConstructor;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by yangxk15 on 3/3/17.
 */

public class Session {

    SharedPreferences mSharedPreferences;

    public Session(Context context) {
        mSharedPreferences = context.getSharedPreferences(
                context.getString(R.string.app_domain),
                MODE_PRIVATE
        );
    }

    public String getString(String key) {
        return mSharedPreferences.getString(key, null);
    }

    public void putString(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }
}
