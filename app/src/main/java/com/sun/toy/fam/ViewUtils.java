
package com.sun.toy.fam;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class ViewUtils {
    private static int screenWidth;
    private static int screenHeight;

    public static int dp2px(Context context, int dip) {
        int px = 0;
        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
        return px;
    }

    public static int getScreenWidth(Context context) {
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        return screenWidth;
    }

    public static int getScreenHeight(Context context) {
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        return screenHeight;
    }

    public static DisplayMetrics getMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }

}