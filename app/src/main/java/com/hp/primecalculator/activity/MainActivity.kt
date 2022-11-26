package com.hp.primecalculator.activity;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.hp.primecalculator.CalcApplication;

import com.hp.primecalculator.manager.NativeThreadHandler;
import com.hp.primecalculator.manager.TouchHandler;
import com.hp.primecalculator.manager.VirtualLcdManager;
import com.vaca.myapplication.R;


import java.nio.ByteBuffer;
import java.util.Map;

public class MainActivity extends Activity {
    static {
        System.loadLibrary("HPPrimeCalculator");
        Log.e("fuck",CalcApplication.G);
        nativeInit(CalcApplication.G, CalcApplication.H);
    }

    public static native void nativeInit(String str, String str2);

    public static native void postText(String str);



    public static native String OnEditCopyNumber();

    public static native void OnEditPasteNumber(String str);

    public static native void SaveCalcData();

    public native void mDnsFound(String str, String str2, int i, String str3, boolean z);

    public native void mDnsGone(String str);




//    public native void FactoryReset(int i);

//    public native void NetworkScreen();

//    public native void OnLanguageChange(int i);




    public VirtualLcdManager virtualLcdManager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(4102);
        setContentView(R.layout.activity_main);
        virtualLcdManager = findViewById(R.id.vLcdManager);


        new Thread(new Runnable() {
            @Override
            public void run() {
                NativeThreadHandler.CalculationThread();
            }
        }).start();

        mDnsGone("112");
        virtualLcdManager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                PointF pointF;
                Map map;
                int i;
                int actionMasked = motionEvent.getActionMasked();
                float f2=motionEvent.getX();
                float f3=motionEvent.getY();
                float f4=v.getWidth();
                float f5=v.getHeight();
                float f6 = 320.0f / f4;
                TouchHandler.j = f2 * f6;
                float f7 = 240.0f / f5;
                TouchHandler.k = f3 * f7;
                Log.e("fuck",""+ TouchHandler.j+"    "+TouchHandler.k);
                int actionIndex = motionEvent.getActionIndex();
                Integer valueOf = Integer.valueOf(motionEvent.getPointerId(actionIndex));
                if (actionMasked == 0) {
                    TouchHandler.OnLCDButtonDown(TouchHandler.j, TouchHandler.k, 0);
                    map = TouchHandler.l;
                    pointF = new PointF(motionEvent.getX(actionIndex), motionEvent.getY(actionIndex));
                } else if (actionMasked == 1) {
                    TouchHandler.OnLCDButtonUp(TouchHandler.j, TouchHandler.k, 0);
                    TouchHandler.l.remove(valueOf);
                    TouchHandler.p = 0;
                    TouchHandler.o = 0;
                    TouchHandler.n = 0;
                    TouchHandler.m = 0;
                    return true;
                } else if (actionMasked == 2) {
                    for (int i2 = 0; i2 < motionEvent.getPointerCount(); i2++) {
                        PointF pointF2 = (PointF) TouchHandler.l.get(Integer.valueOf(motionEvent.getPointerId(i2)));
                        if (!(pointF2 == null || (pointF2.x == motionEvent.getX(i2) && pointF2.y == motionEvent.getY(i2)))) {
                            TouchHandler.j = motionEvent.getX(i2) * f6;
                            TouchHandler.k = motionEvent.getY(i2) * f7;
                            if (i2 == 0) {
                                int i3 = TouchHandler.m;
                                if (!(i3 != 0 && i3 == ((int) TouchHandler.j) && TouchHandler.n == ((int) TouchHandler.k))) {
                                    float f8 = TouchHandler.j;
                                    TouchHandler.m = (int) f8;
                                    float f9 = TouchHandler.k;
                                    TouchHandler.n = (int) f9;
                                    TouchHandler.OnLCDButtonMove(f8, f9, 0);
                                }
                            } else if (i2 == 1 && !((i = TouchHandler.o) != 0 && i == ((int) TouchHandler.j) && TouchHandler.p == ((int) TouchHandler.k))) {
                                float f10 = TouchHandler.j;
                                TouchHandler.o = (int) f10;
                                float f11 = TouchHandler.k;
                                TouchHandler.p = (int) f11;
                                TouchHandler.OnLCDButtonMove(f10, f11, 1);
                            }
                            pointF2.x = motionEvent.getX(i2);
                            pointF2.y = motionEvent.getX(i2);
                        }
                    }
                    return true;
                } else if (actionMasked == 5) {
                    TouchHandler.j = motionEvent.getX(actionIndex) * f6;
                    TouchHandler.k = motionEvent.getY(actionIndex) * f7;
                    TouchHandler.OnLCDButtonDown(TouchHandler.j, TouchHandler.k, 1);
                    map = TouchHandler.l;
                    pointF = new PointF(motionEvent.getX(actionIndex), motionEvent.getY(actionIndex));
                } else if (actionMasked != 6) {
                    return false;
                } else {
                    TouchHandler.OnLCDButtonUp(TouchHandler.j, TouchHandler.k, 1);
                    TouchHandler.l.remove(valueOf);
                    return true;
                }
                map.put(valueOf, pointF);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        new Thread(virtualLcdManager).start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        SaveCalcData();
        virtualLcdManager.StopScreenThread();
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("fuck", "gaga");
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        Log.e("fuck", "gaga");
        if (keyEvent.getKeyCode() == 62 || keyEvent.getKeyCode() == 66) {
            if (keyEvent.getAction() == 1) {
                return onKeyUp(keyEvent.getKeyCode(), keyEvent);
            }
            if (keyEvent.getAction() == 0) {
                return onKeyDown(keyEvent.getKeyCode(), keyEvent);
            }
        }
        return super.dispatchKeyEvent(keyEvent);
    }
}
