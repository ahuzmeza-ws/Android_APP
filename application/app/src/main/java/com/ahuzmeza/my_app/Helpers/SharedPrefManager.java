package com.ahuzmeza.my_app.Helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ahuzmeza.my_app.LoginActivity;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "sharedpref";
    private static final String KEY_ID = "keyid";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    /*******
     *
     * LOG IN USER
     *       stores the user data in shared preferences
     */
    public void userLogin(Users_profile u_profile) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_ID,           u_profile.getId());
        editor.putString(KEY_USERNAME,  u_profile.getUsername());
        editor.putString(KEY_EMAIL,     u_profile.getEmail());

        editor.apply();
    }

    // check if user is already logged in
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    // get the logged in user profile
    public Users_profile getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Users_profile(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null)
        );
    }

    //  logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();

        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
}
