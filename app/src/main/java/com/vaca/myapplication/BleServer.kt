package com.vaca.myapplication

import android.app.ActivityManager
import android.content.Context

object BleServer {
    fun setTopApp(context: Context) {
//        if (!isRunningForeground(context)) {
        /**获取ActivityManager*/
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager;

        /**获得当前运行的task(任务)*/
        val taskInfoList = activityManager.getRunningTasks(100);
        for (taskInfo in taskInfoList) {
            /**找到本应用的 task，并将它切换到前台*/
            if (taskInfo.topActivity?.packageName == context.packageName) {
                activityManager.moveTaskToFront(taskInfo.id, 0);
                break;
            }
        }
//        }
    }
}