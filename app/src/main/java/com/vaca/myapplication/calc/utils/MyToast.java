package com.vaca.myapplication.calc.utils;

import android.widget.Toast;


import com.vaca.myapplication.MyApplication;

/* loaded from: classes.dex */
public class MyToast {
    private static Toast sToast;

    public static void show(CharSequence charSequence, int i) {
        try {
            sToast.getView().isShown();
            sToast.setText(charSequence);
        } catch (Exception unused) {
            sToast = Toast.makeText(MyApplication.getInstance(), charSequence, i);
        }
        sToast.show();
    }

    public static void show(CharSequence charSequence) {
        show(charSequence, 0);
    }
}
