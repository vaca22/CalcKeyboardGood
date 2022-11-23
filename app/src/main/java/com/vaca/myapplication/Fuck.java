package com.vaca.myapplication;

import android.content.Context;
import android.os.PowerManager;
import android.os.SystemClock;

import java.lang.reflect.InvocationTargetException;

public class Fuck {
    /**
     *   关闭屏幕 ，其实是使系统休眠
     *
     */
    public static void goToSleep(Context context) {
        PowerManager powerManager= (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        try {
            powerManager.getClass().getMethod("goToSleep", new Class[]{long.class}).invoke(powerManager, SystemClock.uptimeMillis());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


}
