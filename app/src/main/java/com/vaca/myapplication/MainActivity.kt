package com.vaca.myapplication

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.vaca.myapplication.calc.*

class MainActivity : AppCompatActivity(),  MessageListener {
    lateinit var textView:TextView
    lateinit var bluetoothManager: BluetoothManager
    lateinit var bluetoothLeScanner: BluetoothLeScanner
    lateinit var mBluetoothAdapter: BluetoothAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val requestVoicePermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {

        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            requestVoicePermission.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.BLUETOOTH_CONNECT))


        }
        setContentView(R.layout.activity_main)
        textView=findViewById(R.id.cc)
        MyApplication.addMessageListener(this)
        startService(Intent(this, BluetoothLeService::class.java));
        bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        mBluetoothAdapter = bluetoothManager.adapter
        bluetoothLeScanner = mBluetoothAdapter.bluetoothLeScanner
        bluetoothLeScanner.startScan(mLeScanCallback)
    }
    private val mWeakHandler: WeakHandler = WeakHandler(Looper.getMainLooper())
    private val mLeScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            val gg=result.device.name
            if(gg!=null){
                Log.e("fuck",result.device.name)
            }

        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            super.onBatchScanResults(results)
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
        }
    }
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