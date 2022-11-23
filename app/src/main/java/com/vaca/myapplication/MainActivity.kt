package com.vaca.myapplication

import android.media.AsyncPlayer
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.os.Message
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.vaca.myapplication.calc.*

class MainActivity : AppCompatActivity(),  MessageListener {
    lateinit var textView:TextView
    var asyncPlayer = AsyncPlayer(null)
    var uri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView=findViewById(R.id.cc)
        MyApplication.addMessageListener(this)
    }
    private val mWeakHandler: WeakHandler = WeakHandler(Looper.getMainLooper())

    val sound= listOf<Int>(
        R.raw.g11,
        R.raw.c21,
        R.raw.d21,
        R.raw.e21,
        R.raw.f21,
        R.raw.g21,
        R.raw.a21,
        R.raw.b21,
    )
    override fun handleMessage(message: Message) {
        if(message.what== MsgConstant.KEY_EVENT_MSG){
            mWeakHandler.post {
                var c: Char
                val keyBoardEvent: KeyBoardEvent = message.obj as KeyBoardEvent
                val keyValue: String = keyBoardEvent.getKeyValue()
                val keyCode: Byte = keyBoardEvent.getKeyCode()
                LogUtil.e("keyvalue=$keyValue")
                textView.text=keyValue
                try {
                    val c=keyValue.toInt()
                    uri = Uri.parse(
                        "android.resource://" + MyApplication.application.getPackageName()
                            .toString() + "/" + sound[c]
                    )
                    asyncPlayer.play(MyApplication.application, uri, false, AudioManager.STREAM_MUSIC)
                }catch (e:Exception){

                }

            }
        }
    }
}