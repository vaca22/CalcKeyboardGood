package com.hp.primecalculator.manager;

import android.graphics.RectF;
import android.os.Handler;

import java.util.HashMap;
import java.util.Map;



public class TouchHandler {
    public static int d;
    public static int e;
    public static int f;
    public static int g;
    public static int h;
    public static int i;
    public static float j;
    public static float k;
    public static Map l = new HashMap();
    public static int m;
    public static int n;
    public static int o;
    public static int p;

    public static native void GUIPressKey(int i2);

    public static native void OnLCDButtonDown(float f2, float f3, int i2);

    public static native void OnLCDButtonMove(float f2, float f3, int i2);

    public static native void OnLCDButtonUp(float f2, float f3, int i2);


}
