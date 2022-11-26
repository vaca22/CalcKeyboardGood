package com.hp.primecalculator.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.hp.primecalculator.CalcApplication;
import com.vaca.myapplication.R;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class SplashActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        createFile("FTP Files", "/docs/");
        createFile("Data", "/data/");
        createFile("Fonts", "/fonts/");
        createFile("Resource", "/resource/");
        startActivity(new Intent(this,MainActivity.class));
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


}
