package com.hp.primecalculator.calc.helper;

import android.content.Context;
import android.os.Message;
import android.util.SparseArray;

import com.hp.primecalculator.CalcApplication;
import com.kongqw.serialportlibrary.SerialPortManager;
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;


import com.hp.primecalculator.calc.KeyBoardEvent;
import com.hp.primecalculator.calc.MsgConstant;
import com.hp.primecalculator.calc.utils.LogUtil;
import com.hp.primecalculator.calc.utils.MyToast;

import java.io.File;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/* loaded from: classes.dex */
public class SerialPortHelper {
    private static final int BAUDRATE = 115200;
    private static Context mContext;
    private static SerialPortHelper mInstance;
    private SerialPortManager mSerialPortManager = new SerialPortManager();
    private static final File DEVICE_PATH = new File("dev/ttyS4");
    private static final SparseArray<String> mKeyArrays = new SparseArray<>();

    public SerialPortHelper() {
        this.mSerialPortManager.setOnOpenSerialPortListener(new OnOpenSerialPortListener() { // from class: com.saiyimcu.multimediacomputer.hepler.SerialPortHelper.1
            @Override // com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener
            public void onSuccess(File file) {
          //      MyToast.show("串口打开成功：" + file.getPath());
            }

            @Override // com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener
            public void onFail(File file, OnOpenSerialPortListener.Status status) {
                MyToast.show("串口打开失败: " + file.getPath() + " status: " + status.ordinal());
            }
        });
        this.mSerialPortManager.setOnSerialPortDataListener(new OnSerialPortDataListener() { // from class: com.saiyimcu.multimediacomputer.hepler.SerialPortHelper.2
            @Override // com.kongqw.serialportlibrary.listener.OnSerialPortDataListener
            public void onDataSent(byte[] bArr) {
            }

            @Override // com.kongqw.serialportlibrary.listener.OnSerialPortDataListener
            public void onDataReceived(byte[] bArr) {
               // LogUtil.d(">>received data: " + StringHelper.byte2Hex(bArr));
                if(bArr.length!=4){
                    return;
                }
                if(bArr[1]+bArr[2]!=-1){
                    return;
                }
                Message obtain = Message.obtain();
                obtain.what = MsgConstant.KEY_EVENT_MSG;
                obtain.obj = new KeyBoardEvent(bArr[1], (String) SerialPortHelper.mKeyArrays.get(bArr[1]));
                CalcApplication.chanageMessage(obtain);
            }
        });
        initKeyArray();
    }

    public static synchronized SerialPortHelper getInstance(Context context) {
        SerialPortHelper serialPortHelper;
        synchronized (SerialPortHelper.class) {
            mContext = context;
            if (mInstance == null) {
                synchronized (SerialPortHelper.class) {
                    if (mInstance == null) {
                        mInstance = new SerialPortHelper();
                    }
                }
            }
            serialPortHelper = mInstance;
        }
        return serialPortHelper;
    }

    public void openSerialPort() {
        this.mSerialPortManager.openSerialPort(DEVICE_PATH, BAUDRATE);
    }

    public void closeSerialPort() {
        this.mSerialPortManager.closeSerialPort();
    }

    public void sendScreenOffMsg() {
        this.mSerialPortManager.sendBytes(new byte[]{-86, KeyBoardEvent.KEYCODE_SLEEP, -33, 85});
    }

    private void initKeyArray() {
        DecimalFormatSymbols instance = DecimalFormatSymbols.getInstance(new Locale.Builder().setLocale(mContext.getResources().getConfiguration().locale).setUnicodeLocaleKeyword("nu", "latn").build());
        char zeroDigit = instance.getZeroDigit();
        mKeyArrays.put(1, "F1");
        mKeyArrays.put(2, "F2");
        mKeyArrays.put(3, String.valueOf(instance.getDecimalSeparator()));
        mKeyArrays.put(4, "00");
        mKeyArrays.put(5, String.valueOf(zeroDigit));
        mKeyArrays.put(6, String.valueOf((char) (zeroDigit + 1)));
        mKeyArrays.put(7, String.valueOf((char) (zeroDigit + 2)));
        mKeyArrays.put(8, String.valueOf((char) (zeroDigit + 3)));
        mKeyArrays.put(9, String.valueOf((char) (zeroDigit + 4)));
        mKeyArrays.put(10, String.valueOf((char) (zeroDigit + 5)));
        mKeyArrays.put(11, String.valueOf((char) (zeroDigit + 6)));
        mKeyArrays.put(12, String.valueOf((char) (zeroDigit + 7)));
        mKeyArrays.put(13, String.valueOf((char) (zeroDigit + '\b')));
        mKeyArrays.put(14, String.valueOf((char) (zeroDigit + '\t')));
        mKeyArrays.put(15, "GT");
        mKeyArrays.put(16, "pos_or_nag");
        mKeyArrays.put(17, "DEL");
        mKeyArrays.put(18, "CCE");
        mKeyArrays.put(19, "CA");
        mKeyArrays.put(20, "MU");
        mKeyArrays.put(21, "CM");
        mKeyArrays.put(22, "RM");
        mKeyArrays.put(23, "M-");
        mKeyArrays.put(24, "M+");
        mKeyArrays.put(25, "%");
        mKeyArrays.put(26, "×");
        mKeyArrays.put(27, "+");
        mKeyArrays.put(28, "√");
        mKeyArrays.put(29, "÷");
        mKeyArrays.put(30, "-");
        mKeyArrays.put(31, "=");
    }
}
