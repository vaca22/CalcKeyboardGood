package com.vaca.myapplication;

import android.app.Activity;
import android.app.Application;
import android.os.Message;


import com.vaca.myapplication.calc.MessageListener;
import com.vaca.myapplication.calc.SerialPortHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/* loaded from: classes.dex */
public class MyApplication extends Application {
    public static final int DESTORY_APPLICATION = 202013;
    private static MyApplication application;
    private static List<MessageListener> listMessageListener = new CopyOnWriteArrayList();
    private static List<Activity> mActivitys = new ArrayList();
    private static boolean isFirst = true;
    private DecimalPointState mDecimalPointState = DecimalPointState.DEFAULT_BIT;
    private DecimalRoundMode mRoundMode = DecimalRoundMode.ROUND_UP;

    /* loaded from: classes.dex */
    public enum DecimalPointState {
        DEFAULT_BIT,
        FOUR_BIT,
        TWO_BIT,
        ONE_BIT,
        INTEGER_BIT,
        ADD2_BIT
    }

    /* loaded from: classes.dex */
    public enum DecimalRoundMode {
        ROUND_UP,
        ROUND_HALF_UP,
        ROUND_DOWN
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        application = this;
        SerialPortHelper.getInstance(this).openSerialPort();
    }

    @Override // android.app.Application
    public void onTerminate() {
        super.onTerminate();
        SerialPortHelper.getInstance(this).closeSerialPort();
    }

    public static void addActivity(Activity activity) {
        if (!mActivitys.contains(activity)) {
            mActivitys.add(activity);
        }
    }

    public static void removeActivity(Activity activity) {
        if (mActivitys.contains(activity)) {
            mActivitys.remove(activity);
        }
        if (mActivitys.size() == 0) {
            Message obtain = Message.obtain();
            obtain.what = DESTORY_APPLICATION;
            chanageMessage(obtain);
        }
    }

    public static MyApplication getInstance() {
        return application;
    }

    public static void addMessageListener(MessageListener messageListener) {
        if (!listMessageListener.contains(messageListener)) {
            listMessageListener.add(messageListener);
        }
    }

    public static boolean removeMessageListener(MessageListener messageListener) {
        return listMessageListener.remove(messageListener);
    }

    public static void chanageMessage(Message message) {
        for (MessageListener messageListener : listMessageListener) {
            messageListener.handleMessage(message);
        }
    }

    public DecimalPointState getDecimalPointState() {
        return this.mDecimalPointState;
    }

    public void setDecimalPointState(DecimalPointState decimalPointState) {
        this.mDecimalPointState = decimalPointState;
    }

    public DecimalRoundMode getRoundMode() {
        return this.mRoundMode;
    }

    public void setRoundMode(DecimalRoundMode decimalRoundMode) {
        this.mRoundMode = decimalRoundMode;
    }


}
