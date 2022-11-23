package com.vaca.myapplication.calc;

import android.util.Log;

/* loaded from: classes.dex */
public class LogUtil {
    private static final String TAG = "MultimediaComputer";

    private static String createLog(String str) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[");
        stringBuffer.append(stackTraceElement.getClassName());
        stringBuffer.append(".");
        stringBuffer.append(stackTraceElement.getMethodName());
        stringBuffer.append(".");
        stringBuffer.append(stackTraceElement.getLineNumber());
        stringBuffer.append("]");
        stringBuffer.append(str);
        return stringBuffer.toString();
    }

    public static void e(String str) {
        Log.e(TAG, createLog(str));
    }

    public static void i(String str) {
        Log.i(TAG, createLog(str));
    }

    public static void d(String str) {
        Log.d(TAG, createLog(str));
    }

    public static void v(String str) {
        Log.v(TAG, createLog(str));
    }

    public static void w(String str) {
        Log.w(TAG, createLog(str));
    }
}
