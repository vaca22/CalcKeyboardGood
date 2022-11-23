package com.vaca.myapplication

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.media.AsyncPlayer
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.os.Message
import android.os.PowerManager
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.vaca.myapplication.calc.*
import com.vaca.myapplication.calc.pop.JumpPop
import com.vaca.myapplication.calc.utils.LogUtil
import com.vaca.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), MessageListener {
    var jumpPop: JumpPop? = null
    fun hideBottomUIMenu() {

            window.decorView.systemUiVisibility = 4102

    }
    lateinit var binding: ActivityMainBinding

    lateinit var textView: PDFView
    var asyncPlayer = AsyncPlayer(null)
    var uri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        hideBottomUIMenu()

        val window = getWindow();
        val layoutParams = window.getAttributes();

        layoutParams.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(binding.root)
        textView =binding.pdfView
        MyApplication.addMessageListener(this)

        textView.fromAsset("aa.pdf").load()


        adminReceiver = ComponentName(this@MainActivity, ScreenOffAdminReceiver::class.java)
        mPowerManager = getSystemService(POWER_SERVICE) as PowerManager
        policyManager =
            this@MainActivity.getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager

        checkAndTurnOnDeviceManager()
    }

    private val mWeakHandler: WeakHandler = WeakHandler(Looper.getMainLooper())

    val sound = listOf<Int>(
        R.raw.g11,
        R.raw.c21,
        R.raw.d21,
        R.raw.e21,
        R.raw.f21,
        R.raw.g21,
        R.raw.a21,
        R.raw.b21,
        R.raw.d11,
        R.raw.c11,
    )
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

    override fun handleMessage(message: Message) {
        if (message.what == MsgConstant.KEY_EVENT_MSG) {
            mWeakHandler.post {
                var c: Char
                val keyBoardEvent: KeyBoardEvent = message.obj as KeyBoardEvent
                val keyValue: String = keyBoardEvent.getKeyValue()
                val keyCode: Byte = keyBoardEvent.getKeyCode()
                LogUtil.e("keyvalue=$keyValue")
                if(keyValue=="+"){
                    if(textView.currentPage<textView.pageCount){
                        textView.jumpTo(textView.currentPage+1,false)
                    }

                    return@post
                }

                when(keyValue){
                    "="->{
                        BleServer.setTopApp(MyApplication.application)
                    }
                    "-"->{
                        if(textView.currentPage>0){
                            textView.jumpTo(textView.currentPage-1,false)
                        }
                    }
                    "×"->{
                        scale+=0.05f
                        textView.zoomWithAnimation(scale)
                    }
                    "÷"->{
                        if(scale>0.15f){
                            scale-=0.05f

                            textView.zoomWithAnimation(scale)
                        }
                    }
                    "."->{
                        BleServer.dataScope.launch {
                            if(checkScreen()){
                                delay(100)
                                checkScreenOff()
                            }

                        }

                    }
                    "DEL"->{
                        jumpPop =
                            JumpPop(this@MainActivity, 53,binding.pdfView.pageCount)
                       jumpPop?.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
                    }
                    else->{

                        try {
                            val c = keyValue.toInt()
                            uri = Uri.parse(
                                "android.resource://" + MyApplication.application.getPackageName()
                                    .toString() + "/" + sound[c]
                            )
                            asyncPlayer.play(
                                MyApplication.application,
                                uri,
                                false,
                                AudioManager.STREAM_MUSIC
                            )
                        } catch (e: Exception) {

                        }

                    }
                }





            }
        }
    }
}