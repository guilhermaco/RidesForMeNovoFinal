package com.ridesforme.ridesforme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class UserSessionManager {

    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "AndroidExamplePref";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_SENHA = "senha";
    public static final String KEY_IDUSER = "iduser";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LNG = "lng";

    // Constructor
    public UserSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, 0);
        editor = pref.edit();
    }

    public void createUserLoginSession(String name, String senha, String idUser) {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_SENHA, senha);
        editor.putString(KEY_IDUSER, idUser);
        editor.commit();
    }

    public void createLastLocation(String lat, String lng) {
        editor.putString(KEY_LAT, lat);
        editor.putString(KEY_LNG, lng);
        editor.commit();
    }

    public HashMap<String, String> getLastLocation() {
        HashMap<String, String> location = new HashMap<String, String>();
        location.put(KEY_LAT, pref.getString(KEY_LAT, null));
        location.put(KEY_LNG, pref.getString(KEY_LNG, null));
        return location;
    }


    public boolean checkLogin() {
        if (!this.isUserLoggedIn()) {
            Intent i = new Intent(_context, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
            return true;
        }
        return false;
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_SENHA, pref.getString(KEY_SENHA, null));
        user.put(KEY_IDUSER, pref.getString(KEY_IDUSER, null));
        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, com.ridesforme.ridesforme.LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}