package com.example.moneyjeju;

import android.app.Application;

import com.google.android.gms.maps.GoogleMap;

public class JejuApp extends Application {
    public static String userId=null;
    public static String planNo=null;
    public static String selectDate=null;
    public static GoogleMap map=null;
    public static String startDate=null;


    @Override
    public void onCreate() {
        super.onCreate();
    }
}
