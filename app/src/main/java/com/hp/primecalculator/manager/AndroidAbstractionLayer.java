package com.hp.primecalculator.manager;

import com.hp.primecalculator.BleServer;
import com.hp.primecalculator.CalcApplication;
import com.hp.primecalculator.utils.InonePowerSaveUtil;

public class AndroidAbstractionLayer {
    public static int batteryPctValue() {
        Integer i=BleServer.INSTANCE.getMainBattery().getValue();
        if(i!=null){
            return i;
        }

        return 100;
    }
    public static boolean isCharging() {

        return InonePowerSaveUtil.isCharging(CalcApplication.application);
    }
}
