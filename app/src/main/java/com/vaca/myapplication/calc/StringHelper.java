package com.vaca.myapplication.calc;

import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class StringHelper {
    public static boolean isBlank(String str) {
        return str == null || str.isEmpty();
    }

    public static String byte2Hex(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(hexString + " ");
        }
        return stringBuffer.toString();
    }

    public static boolean isNumeric(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        boolean matches = Pattern.compile("[+-]*\\d+\\.?\\d*[Ee]*[+-]*\\d+").matcher(str).matches();
        if (matches) {
            return matches;
        }
        return Pattern.compile("^[-\\+]?[.\\d]*$").matcher(str).matches();
    }

    public static StringBuilder addComma(String str) {
        boolean z = true;
        if (str.startsWith("-")) {
            str = str.substring(1);
        } else {
            z = false;
        }
        String str2 = null;
        if (str.indexOf(46) != -1) {
            str2 = str.substring(str.indexOf(46));
            str = str.substring(0, str.indexOf(46));
        }
        StringBuilder sb = new StringBuilder(str);
        sb.reverse();
        for (int i = 3; i < sb.length(); i += 4) {
            sb.insert(i, '\'');
        }
        sb.reverse();
        if (z) {
            sb.insert(0, '-');
        }
        if (str2 != null) {
            sb.append(str2);
        }
        return sb;
    }

    public static String subZeroAndDot(String str) {
        return str.indexOf(".") > 0 ? str.replaceAll("0+?$", "").replaceAll("[.]$", "") : str;
    }
}
