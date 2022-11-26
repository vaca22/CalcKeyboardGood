package com.hp.primecalculator.calc;

/* loaded from: classes.dex */
public class KeyBoardEvent {
    public static final byte KEYCODE_0 = 5;
    public static final byte KEYCODE_00 = 4;
    public static final byte KEYCODE_1 = 6;
    public static final byte KEYCODE_2 = 7;
    public static final byte KEYCODE_3 = 8;
    public static final byte KEYCODE_4 = 9;
    public static final byte KEYCODE_5 = 10;
    public static final byte KEYCODE_6 = 11;
    public static final byte KEYCODE_7 = 12;
    public static final byte KEYCODE_8 = 13;
    public static final byte KEYCODE_9 = 14;
    public static final byte KEYCODE_ADD = 27;
    public static final byte KEYCODE_ARROW = 17;
    public static final byte KEYCODE_CA = 19;
    public static final byte KEYCODE_CCE = 18;
    public static final byte KEYCODE_CM = 21;
    public static final byte KEYCODE_DIVISION = 29;
    public static final byte KEYCODE_DOT = 3;
    public static final byte KEYCODE_EQUAL = 31;
    public static final byte KEYCODE_F1 = 1;
    public static final byte KEYCODE_F2 = 2;
    public static final byte KEYCODE_GT = 15;
    public static final byte KEYCODE_MU = 20;
    public static final byte KEYCODE_MULTIPLICATION = 26;
    public static final byte KEYCODE_M_ADD = 24;
    public static final byte KEYCODE_M_SUB = 23;
    public static final byte KEYCODE_PERCENT = 25;
    public static final byte KEYCODE_POS_OR_NAG = 16;
    public static final byte KEYCODE_RADICAL = 28;
    public static final byte KEYCODE_RM = 22;
    public static final byte KEYCODE_SLEEP = 32;
    public static final byte KEYCODE_SUB = 30;
    private byte keyCode;
    private String keyValue;

    public KeyBoardEvent(byte b, String str) {
        this.keyCode = b;
        this.keyValue = str;
    }

    public byte getKeyCode() {
        return this.keyCode;
    }

    public void setKeyCode(byte b) {
        this.keyCode = b;
    }

    public String getKeyValue() {
        return this.keyValue;
    }

    public void setKeyValue(String str) {
        this.keyValue = str;
    }
}
