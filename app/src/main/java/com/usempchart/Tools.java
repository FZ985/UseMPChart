package com.usempchart;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by JFZ on 2017/4/7 10:49.
 */

public class Tools {

    public static final String TAG = "Tools";
    public static Toast toast;
    public static View view = null;


    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param context
     * @param pxValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param context
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 屏幕dp输入目标值
     */
    public static float destinationValue(Context context, int goalValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                goalValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 获取指定dip值
     */
    public static int getDipValue(Context context, int des) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                des, metrics);
    }

    /**
     * @description :获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        return getMetrics(context).widthPixels;
    }

    /**
     * @description :获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        return getMetrics(context).heightPixels;
    }

    /**
     * @description :获取屏幕密度
     */
    public static int getScreenDensity(Context context) {
        float density = getMetrics(context).density;
        return (int) density;
    }

    public static DisplayMetrics getMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }


    public static void showToast(String text) {
        showToast(BaseApp.getInstance(), text, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String text) {
        showToast(context, text, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String text, int duration) {
        if (TextUtils.isEmpty(text)) return;
        try {
            if (toast != null) {
                toast.cancel();
                toast = null;
                view = null;
            }
            toast = new Toast(context);
            if (view == null) {
                view = Toast.makeText(context, "", Toast.LENGTH_SHORT).getView();
            }
            toast.setView(view);
            toast.setText(text);
            toast.setDuration(duration);
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, dip2px(context, 50));
            toast.show();
        } catch (Exception e) {
        }
    }

    public static void showToastCenter(String text) {
        showToastCenter(text, Gravity.CENTER);
    }

    public static void showToastCenter(String text, int gravity) {
        showToastCenter(BaseApp.getInstance(), text, gravity);
    }

    public static void showToastCenter(Context context, String text, int gravity) {
        try {
            if (toast != null) {
                toast.cancel();
                toast = null;
                view = null;
            }
            toast = new Toast(context);
            if (view == null) {
                view = Toast.makeText(context, "", Toast.LENGTH_SHORT).getView();
            }
            toast.setView(view);
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(gravity, 0, 0);
            toast.show();
        } catch (Exception e) {
        }
    }

}
