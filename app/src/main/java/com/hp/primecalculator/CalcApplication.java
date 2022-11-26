package com.hp.primecalculator;

import android.app.Application;
import android.os.Build;
import android.os.Message;

import com.hp.primecalculator.calc.MessageListener;
import com.hp.primecalculator.calc.helper.SerialPortHelper;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class CalcApplication extends Application {
    public static Map r;
    private static List<MessageListener> listMessageListener = new CopyOnWriteArrayList();
    public static CalcApplication application;
    public static String G;
    public static String H;
    public final void a() {
        File file = new File(CalcApplication.G);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static CalcApplication getInstance() {
        return application;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        SerialPortHelper.getInstance(this).openSerialPort();
        G = getFilesDir().toString();
        a();
        H = Build.VERSION.RELEASE;
    }

    @Override // android.app.Application
    public void onTerminate() {
        super.onTerminate();
        SerialPortHelper.getInstance(this).closeSerialPort();
    }

    public static void addMessageListener(MessageListener messageListener) {
        if (!listMessageListener.contains(messageListener)) {
            listMessageListener.add(messageListener);
        }
    }
    public static void chanageMessage(Message message) {
        for (MessageListener messageListener : listMessageListener) {
            messageListener.handleMessage(message);
        }
    }
    public static boolean removeMessageListener(MessageListener messageListener) {
        return listMessageListener.remove(messageListener);
    }

}
