package com.hp.primecalculator.manager;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.nio.ByteBuffer;


@SuppressLint("AppCompatCustomView")
public class VirtualLcdManager extends ImageView implements Runnable {


    public ByteBuffer byteBuffer;


    public Bitmap bitmap;

//    public int screenTime = 33;

    public VirtualLcdManager(Context context) {
        super(context);
        initGraphBuffer();
    }

    public VirtualLcdManager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initGraphBuffer();
    }

    public VirtualLcdManager(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initGraphBuffer();
    }

    public VirtualLcdManager(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initGraphBuffer();
    }

//    public static int getLEDLeftMargin() {
//        return 0;
//    }
//
//    public static int getLEDLeftPosition() {
//        return 0;
//    }
//
//    public static int getLEDTopMargin() {
//        return 0;
//    }

    public final native int GetLedStatus();

    public final native void NativeScreenThread(ByteBuffer byteBuffer);

    public native void StopScreenThread();

    public void initGraphBuffer() {
        this.byteBuffer = ByteBuffer.allocateDirect(307200);
        this.bitmap = Bitmap.createBitmap(320, 240, Bitmap.Config.ARGB_8888);
    }


    public void update() {
        this.byteBuffer.rewind();
        this.bitmap.copyPixelsFromBuffer(this.byteBuffer);
    }
//    public int getScreenUpdateTime() {
//        return this.screenTime;
//    }
//
//    public void setScreenUpdateTime(int i) {
//        this.screenTime = i;
//    }

    public void invalidateScreen() {
        postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        update();
        GetLedStatus();
        canvas.drawBitmap(this.bitmap, (Rect) null, canvas.getClipBounds(), (Paint) null);
        super.onDraw(canvas);
    }

    @Override
    public void run() {
        NativeScreenThread(this.byteBuffer);
    }


}
