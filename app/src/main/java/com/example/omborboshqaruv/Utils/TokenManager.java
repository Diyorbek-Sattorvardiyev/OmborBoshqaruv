package com.example.omborboshqaruv.Utils;
import android.content.Context;
import android.content.SharedPreferences;
public class TokenManager {
    private static final String SHARED_PREFS = "auth_prefs";
    private static final String TOKEN_KEY = "access_token";

    public static void saveToken(Context context, String token) {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
                .edit()
                .putString(TOKEN_KEY, token)
                .apply();
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
                .getString(TOKEN_KEY, null);
    }

    public static void clearToken(Context context) {
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
                .edit()
                .remove(TOKEN_KEY)
                .apply();
    }
}
