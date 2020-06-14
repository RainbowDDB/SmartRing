package com.rainbow.smartring.utils;

import java.io.UnsupportedEncodingException;

/**
 * SmartRing
 * Created By Rainbow on 2020/5/2.
 */
public class BluetoothUtils {

    public static String bytesToString(byte[] bytes){
        try {
            return new String(bytes, 0, bytes.length, "GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String bytesToHexString(byte[] var0) {
        StringBuffer var1 = new StringBuffer(var0.length);

        int var2;
        for (var2 = 0; var2 < var0.length; ++var2) {
            String var3 = Integer.toHexString(var0[var2] & 255);
            if (var3.length() < 2) {
                var1.append(0);
            }

            var1.append(var3.toUpperCase());
        }

        int var4 = var1.length();
        if (var4 != 1 && var4 != 0) {
            var2 = var4;
            if (var4 % 2 == 1) {
                var2 = var4 - 1;
                var1.insert(var2, " ");
            }

            while (var2 > 0) {
                var1.insert(var2, " ");
                var2 -= 2;
            }

            return var1.toString();
        } else {
            return var1.toString();
        }
    }

}
