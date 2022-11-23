package com.vaca.myapplication

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.os.Looper
import android.os.Message
import android.os.PowerManager
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.vaca.myapplication.calc.*
import com.vaca.myapplication.calc.pop.JumpPop
import com.vaca.myapplication.calc.utils.LogUtil

class MainActivity : AppCompatActivity(), MessageListener {
    var jumpPop: JumpPop? = null
    fun hideBottomUIMenu() {
            window.decorView.systemUiVisibility = 4102
    }

    private lateinit var snakeEngine: SnakeEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideBottomUIMenu()

        val window = getWindow();
        val layoutParams = window.getAttributes();

        layoutParams.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);



        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        snakeEngine = SnakeEngine(this, size)
        setContentView(snakeEngine)
        MyApplication.addMessageListener(this)
        adminReceiver = ComponentName(this@MainActivity, ScreenOffAdminReceiver::class.java)
        mPowerManager = getSystemService(POWER_SERVICE) as PowerManager
        policyManager =
            this@MainActivity.getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        checkAndTurnOnDeviceManager()
    }

    private val mWeakHandler: WeakHandler = WeakHandler(Looper.getMainLooper())


    var scale=1f;

    override fun onStart() {
        hideBottomUIMenu()
        super.onStart()
    }
    lateinit var  mPowerManager: PowerManager
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
           Log.e("fuck","没有设备管理权限")
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

    fun checkAndTurnOnDeviceManager() {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminReceiver)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启后就可以使用锁屏功能了...") //显示位置见图二
        startActivityForResult(intent, 0)
    }

    var jumpNum=-2;

    override fun handleMessage(message: Message) {
        if (message.what == MsgConstant.KEY_EVENT_MSG) {
            mWeakHandler.post {
                var c: Char
                val keyBoardEvent: KeyBoardEvent = message.obj as KeyBoardEvent
                val keyValue: String = keyBoardEvent.getKeyValue()
                val keyCode: Byte = keyBoardEvent.getKeyCode()
                LogUtil.e("keyvalue=$keyValue")
                checkScreenOn()
                when(keyValue){
                   "1"->{
                        snakeEngine.changeHeading(SnakeEngine.Heading.LEFT)
                   }
                    "2"->{
                        snakeEngine.changeHeading(SnakeEngine.Heading.DOWN)
                    }
                    "3"->{
                        snakeEngine.changeHeading(SnakeEngine.Heading.RIGHT)
                    }
                    "5"->{
                        snakeEngine.changeHeading(SnakeEngine.Heading.UP)
                    }
                }





            }
        }
    }

    override fun onResume() {
        super.onResume()
        snakeEngine.resume()
    }

    // Stop the thread in snakeEngine
    override fun onPause() {
        super.onPause()
        snakeEngine.pause()
    }
}