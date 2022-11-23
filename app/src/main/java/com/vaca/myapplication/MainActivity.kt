package com.vaca.myapplication

import android.media.AsyncPlayer
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.Message
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.vaca.myapplication.calc.*

class MainActivity : AppCompatActivity(), MessageListener {

    fun hideBottomUIMenu() {

            window.decorView.systemUiVisibility = 4102

    }


    lateinit var textView: PDFView
    var asyncPlayer = AsyncPlayer(null)
    var uri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideBottomUIMenu()

        val window = getWindow();
        val layoutParams = window.getAttributes();

        layoutParams.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.pdfView)
        MyApplication.addMessageListener(this)

        textView.fromAsset("aa.pdf").load()
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
                }





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