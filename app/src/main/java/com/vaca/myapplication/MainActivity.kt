package com.vaca.myapplication

import android.os.Bundle
import android.os.Looper
import android.os.Message
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.vaca.myapplication.calc.*

class MainActivity : AppCompatActivity(),  MessageListener {
    lateinit var textView:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView=findViewById(R.id.cc)
        MyApplication.addMessageListener(this)
    }
    private val mWeakHandler: WeakHandler = WeakHandler(Looper.getMainLooper())

    override fun handleMessage(message: Message) {
        if(message.what== MsgConstant.KEY_EVENT_MSG){
            mWeakHandler.post {
                var c: Char
                val keyBoardEvent: KeyBoardEvent = message.obj as KeyBoardEvent
                val keyValue: String = keyBoardEvent.getKeyValue()
                val keyCode: Byte = keyBoardEvent.getKeyCode()
                LogUtil.e("keyvalue=$keyValue")
                textView.text=keyValue
            }
        }
    }
}