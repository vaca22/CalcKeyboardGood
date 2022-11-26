package com.hp.primecalculator;

import android.app.Application;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Message;
import android.util.Log;

import com.hp.primecalculator.calc.MessageListener;
import com.hp.primecalculator.calc.helper.SerialPortHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class CalcApplication extends Application {
    public static Map r;
    private static List<MessageListener> listMessageListener = new CopyOnWriteArrayList();
    public static CalcApplication application;
    public static String G;
    public static String H;
    public final void mkWorkDir() {
        File file = new File(CalcApplication.G);
        if (!file.exists()) {
            file.mkdir();
        }
    }
    void createFile(String assetsFolder,String localFolder){
        AssetManager r0 = getAssets();
        try {
            String[] r1=r0.list(assetsFolder);
            File r2 ;
            String r31=CalcApplication.G+localFolder;
            r2=new File(r31);
            boolean r32=r2.exists();
            if(r32){
                Log.e("gagaxx",r2.getAbsolutePath());
            }else{
                Log.e("gaga",r2.getAbsolutePath());
                r2.mkdirs();
                for(int k=0;k<r1.length;k++){
                    Log.e("fuck",r1[k]);
                    String dada=assetsFolder+"/"+r1[k];
                    InputStream inputStream=r0.open(dada);
                    byte[] ga=new byte[inputStream.available()];
                    int yes=inputStream.read(ga);

                    FileOutputStream fileOutputStream=new FileOutputStream(CalcApplication.G+localFolder+r1[k]);
                    fileOutputStream.write(ga);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    inputStream.close();
                }
            }



        } catch (IOException e) {
            e.printStackTrace();
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
        mkWorkDir();
        createFile("FTP Files", "/docs/");
        createFile("Data", "/data/");
        createFile("Fonts", "/fonts/");
        createFile("Resource", "/resource/");

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
