package com.vaca.myapplication

import android.media.AsyncPlayer
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.os.Message
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.vaca.myapplication.calc.*

class MainActivity : AppCompatActivity(), MessageListener {
    lateinit var textView: PDFView
    var asyncPlayer = AsyncPlayer(null)
    var uri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        val window = getWindow();
        val layoutParams = window.getAttributes();

        layoutParams.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState)
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
    )
    var scale=1f;

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
                        textView.jumpTo(textView.currentPage+1,true)
                    }

                    return@post
                }

                if(keyValue=="-"){
                    if(textView.currentPage>0){
                        textView.jumpTo(textView.currentPage-1,true)
                    }

                    return@post
                }
                if(keyValue=="×"){
                    scale+=0.05f
                    textView.zoomWithAnimation(scale)


                    return@post
                }
                if(keyValue=="÷"){
                    if(scale>0.15f){
                        scale-=0.05f
                        textView.zoomWithAnimation(scale)
                    }

                    return@post
                }

                // textView.text=keyValue
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