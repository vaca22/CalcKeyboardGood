package com.hp.primecalculator.activity

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager

abstract class BaseActivity:Activity() {
    fun hideThing(){
        window.decorView.systemUiVisibility = 4102
    }

    override fun onStart() {
        hideThing()
        super.onStart()
    }

    fun tooEarly():Boolean{
        return android.os.SystemClock.elapsedRealtime()<38000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideThing()
        val window = getWindow();
        val layoutParams = window.getAttributes();

        layoutParams.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}