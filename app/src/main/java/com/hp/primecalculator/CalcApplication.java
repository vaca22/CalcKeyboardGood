package com.hp.primecalculator;

import android.app.Application;
import android.os.Build;

import java.io.File;

public class CalcApplication extends Application {
    public static String G;
    public static String H;
    public final void a() {
        File file = new File(CalcApplication.G);
        if (!file.exists()) {
            file.mkdir();
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        G = getFilesDir().toString();
        a();
        H = Build.VERSION.RELEASE;
    }
}
