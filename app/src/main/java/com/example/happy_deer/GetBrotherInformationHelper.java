package com.example.happy_deer;

import android.content.Context;

import java.util.Locale;

public class GetBrotherInformationHelper {
    public static String getUserLanguage(Context context) {
        // 获取当前的 Locale
        Locale current = context.getResources().getConfiguration().locale;
        // 返回语言代码，如 "en", "zh", "fr" 等
        return current.getLanguage();
    }

    public static String getAndroidVersion() {
        return android.os.Build.VERSION.RELEASE; // 如 "9.0", "10"
    }

    public static int getSdkVersion() {
        return android.os.Build.VERSION.SDK_INT; // SDK 级别，如 29
    }

    public static String getDeviceManufacturerAndModel() {
        return android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
    }

}
