package com.hp.primecalculator.activity

import android.annotation.SuppressLint
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.PointF
import android.os.Bundle
import android.os.Looper
import android.os.Message
import android.os.PowerManager
import android.util.Log
import android.view.View
import com.hp.primecalculator.BleServer
import com.hp.primecalculator.CalcApplication
import com.hp.primecalculator.R
import com.hp.primecalculator.ScreenOffAdminReceiver
import com.hp.primecalculator.calc.KeyBoardEvent
import com.hp.primecalculator.calc.MessageListener
import com.hp.primecalculator.calc.MsgConstant
import com.hp.primecalculator.calc.WeakHandler
import com.hp.primecalculator.manager.NativeThreadHandler
import com.hp.primecalculator.manager.TouchHandler
import com.hp.primecalculator.manager.VirtualLcdManager
import com.hp.primecalculator.manager.setting.SettingsItemClickListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseActivity(), MessageListener {
    external fun mDnsFound(str: String?, str2: String?, i: Int, str3: String?, z: Boolean)
    external fun mDnsGone(str: String?)
    external fun nativeInit(str: String?, str2: String?)
    external fun postText(str: String?)
    external fun OnEditCopyNumber(): String?
    external fun OnEditPasteNumber(str: String?)
    external fun SaveCalcData();
    external fun FactoryReset(i: Int);
    external fun NetworkScreen();
    external fun OnLanguageChange(i: Int);


    lateinit var virtualLcdManager: VirtualLcdManager


    lateinit var mPowerManager: PowerManager
    lateinit var mWakeLock: PowerManager.WakeLock


    lateinit var policyManager: DevicePolicyManager
    lateinit var adminReceiver: ComponentName


    /**
     * 息屏
     */
    fun checkScreenOff() {
        val admin = policyManager.isAdminActive(adminReceiver)
        if (admin) {
            policyManager.lockNow()
        } else {
            Log.e("fuck", "没有设备管理权限")
        }
    }

    /**
     * @param view 检测屏幕状态
     */
    fun checkScreen(): Boolean {
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        return pm.isScreenOn
    }

    /**
     * @param view 亮屏
     */
    fun checkScreenOn() {
        mWakeLock = mPowerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "tag"
        )
        mWakeLock.acquire()
        mWakeLock.release()
    }

    private fun checkAndTurnOnDeviceManager() {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminReceiver)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启后就可以使用锁屏功能了...") //显示位置见图二
        startActivityForResult(intent, 0)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        virtualLcdManager = findViewById(R.id.vLcdManager)
        CalcApplication.addMessageListener(this)
        Thread { NativeThreadHandler.CalculationThread() }.start()
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
        adminReceiver = ComponentName(this@MainActivity, ScreenOffAdminReceiver::class.java)
        mPowerManager = getSystemService(POWER_SERVICE) as PowerManager
        policyManager =
            this@MainActivity.getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        //checkAndTurnOnDeviceManager()
    }


    override fun onResume() {
        Thread(virtualLcdManager).start()
        super.onResume()
    }


    override fun onPause() {
       SettingsItemClickListener.SaveCalcData()

        virtualLcdManager.StopScreenThread()
        super.onPause()
    }


    val keyMap = mapOf<Int, Int>(
        1 to 41,
        2 to 36,
        3 to 48,
        4 to 49,
        5 to 47,
        6 to 42,
        7 to 43,
        8 to 44,
        9 to 37,
        10 to 38,
        11 to 39,
        12 to 32,
        13 to 33,
        14 to 34,
        15 to 21,
        16 to 27,
        17 to 31,
        18 to 19,
        19 to 46,
        20 to 22,
        21 to 23,
        22 to 28,
        23 to 15,
        24 to 16,
        25 to 5,
        26 to 40,
        27 to 50,
        28 to 10,
        29 to 35,
        30 to 45,
        31 to 30


    )

    override fun onDestroy() {
        CalcApplication.removeMessageListener(this)
        super.onDestroy()
    }


    private val mWeakHandler = WeakHandler(Looper.getMainLooper())
    override fun handleMessage(message: Message) {
        if (message.what == MsgConstant.KEY_EVENT_MSG) {
            mWeakHandler.post {
                var c: Char
                val keyBoardEvent: KeyBoardEvent = message.obj as KeyBoardEvent
                val keyValue: String = keyBoardEvent.getKeyValue()
                val keyCode: Byte = keyBoardEvent.getKeyCode()

                val code = keyCode.toUInt().toInt()
                Log.e("fuck", code.toString())
                val mm: Int? = keyMap.get(code);
                mm?.let {
                    TouchHandler.GUIPressKey(it)
                    TouchHandler.GUIPressKey(-1)
                }

                if (code == 19) {
                    BleServer.dataScope.launch {
                        if (checkScreen()) {
                            delay(100)
                            checkScreenOff()
                        } else {
                            hideThing()
                        }

                    }
                }


            }
        }
    }


    init {
        System.loadLibrary("HPPrimeCalculator")
        Log.e("fuck", CalcApplication.G)
        nativeInit(CalcApplication.G, CalcApplication.H)
    }


}