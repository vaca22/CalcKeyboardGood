package com.hp.primecalculator.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.PointF
import android.os.Bundle
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.view.View
import com.hp.primecalculator.CalcApplication
import com.hp.primecalculator.R
import com.hp.primecalculator.calc.MessageListener
import com.hp.primecalculator.calc.WeakHandler
import com.hp.primecalculator.manager.NativeThreadHandler
import com.hp.primecalculator.manager.TouchHandler
import com.hp.primecalculator.manager.VirtualLcdManager

class MainActivity : Activity(), MessageListener {
    external fun mDnsFound(str: String?, str2: String?, i: Int, str3: String?, z: Boolean)
    external fun mDnsGone(str: String?)
    external fun nativeInit(str: String?, str2: String?)
    external fun postText(str: String?)
    external fun OnEditCopyNumber(): String?
    external fun OnEditPasteNumber(str: String?)
    external fun SaveCalcData()
    external fun FactoryReset(i: Int);
    external fun NetworkScreen();
    external fun OnLanguageChange(i: Int);
    lateinit var virtualLcdManager: VirtualLcdManager

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = 4102
        setContentView(R.layout.activity_main)
        virtualLcdManager = findViewById(R.id.vLcdManager)
        CalcApplication.addMessageListener(this)
        Thread { NativeThreadHandler.CalculationThread() }.start()
        mDnsGone("112")
        virtualLcdManager.setOnTouchListener(View.OnTouchListener { v, motionEvent ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            val pointF: PointF
            val map: MutableMap<*, *>
            var i: Int
            val actionMasked = motionEvent.actionMasked
            val f2 = motionEvent.x
            val f3 = motionEvent.y
            val f4 = v.width.toFloat()
            val f5 = v.height.toFloat()
            val f6 = 320.0f / f4
            TouchHandler.j = f2 * f6
            val f7 = 240.0f / f5
            TouchHandler.k = f3 * f7
            Log.e("fuck", "" + TouchHandler.j + "    " + TouchHandler.k)
            val actionIndex = motionEvent.actionIndex
            val valueOf = Integer.valueOf(motionEvent.getPointerId(actionIndex))
            if (actionMasked == 0) {
                TouchHandler.OnLCDButtonDown(TouchHandler.j, TouchHandler.k, 0)
                map = TouchHandler.l
                pointF = PointF(motionEvent.getX(actionIndex), motionEvent.getY(actionIndex))
            } else if (actionMasked == 1) {
                TouchHandler.OnLCDButtonUp(TouchHandler.j, TouchHandler.k, 0)
                TouchHandler.l.remove(valueOf)
                TouchHandler.p = 0
                TouchHandler.o = 0
                TouchHandler.n = 0
                TouchHandler.m = 0
                return@OnTouchListener true
            } else if (actionMasked == 2) {
                for (i2 in 0 until motionEvent.pointerCount) {
                    val pointF2 =
                        TouchHandler.l[Integer.valueOf(motionEvent.getPointerId(i2))] as PointF?
                    if (!(pointF2 == null || pointF2.x == motionEvent.getX(i2) && pointF2.y == motionEvent.getY(
                            i2
                        ))
                    ) {
                        TouchHandler.j = motionEvent.getX(i2) * f6
                        TouchHandler.k = motionEvent.getY(i2) * f7
                        if (i2 == 0) {
                            val i3 = TouchHandler.m
                            if (!(i3 != 0 && i3 == TouchHandler.j.toInt() && TouchHandler.n == TouchHandler.k.toInt())) {
                                val f8 = TouchHandler.j
                                TouchHandler.m = f8.toInt()
                                val f9 = TouchHandler.k
                                TouchHandler.n = f9.toInt()
                                TouchHandler.OnLCDButtonMove(f8, f9, 0)
                            }
                        } else if (i2 == 1 && !(TouchHandler.o.also {
                                i = it
                            } != 0 && i == TouchHandler.j.toInt() && TouchHandler.p == TouchHandler.k.toInt())) {
                            val f10 = TouchHandler.j
                            TouchHandler.o = f10.toInt()
                            val f11 = TouchHandler.k
                            TouchHandler.p = f11.toInt()
                            TouchHandler.OnLCDButtonMove(f10, f11, 1)
                        }
                        pointF2.x = motionEvent.getX(i2)
                        pointF2.y = motionEvent.getX(i2)
                    }
                }
                return@OnTouchListener true
            } else if (actionMasked == 5) {
                TouchHandler.j = motionEvent.getX(actionIndex) * f6
                TouchHandler.k = motionEvent.getY(actionIndex) * f7
                TouchHandler.OnLCDButtonDown(TouchHandler.j, TouchHandler.k, 1)
                map = TouchHandler.l
                pointF = PointF(motionEvent.getX(actionIndex), motionEvent.getY(actionIndex))
            } else if (actionMasked != 6) {
                return@OnTouchListener false
            } else {
                TouchHandler.OnLCDButtonUp(TouchHandler.j, TouchHandler.k, 1)
                TouchHandler.l.remove(valueOf)
                return@OnTouchListener true
            }
            map[valueOf] = pointF
            true
        })
    }

    override fun onResume() {
        Thread(virtualLcdManager).start()
        super.onResume()
    }

    override fun onPause() {
        SaveCalcData()
        virtualLcdManager!!.StopScreenThread()
        super.onPause()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        Log.e("fuck", "gaga")
        return super.onKeyDown(keyCode, event)
    }

    override fun dispatchKeyEvent(keyEvent: KeyEvent): Boolean {
        Log.e("fuck", "gaga")
        if (keyEvent.keyCode == 62 || keyEvent.keyCode == 66) {
            if (keyEvent.action == 1) {
                return onKeyUp(keyEvent.keyCode, keyEvent)
            }
            if (keyEvent.action == 0) {
                return onKeyDown(keyEvent.keyCode, keyEvent)
            }
        }
        return super.dispatchKeyEvent(keyEvent)
    }

    private val mWeakHandler = WeakHandler(Looper.getMainLooper())
    override fun handleMessage(message: Message) {}


    init {
        System.loadLibrary("HPPrimeCalculator")
        Log.e("fuck", CalcApplication.G)
        nativeInit(CalcApplication.G, CalcApplication.H)
    }


}