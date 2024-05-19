package com.example.aquabrowser;

import android.util.Log;

public class Debug {
    public static void log(String message) {
        Log.println(Log.DEBUG, "DEBUG", message);
    }
}