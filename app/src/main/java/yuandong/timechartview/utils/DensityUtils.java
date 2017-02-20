package yuandong.timechartview.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by yuandong on 2017/2/18.
 */

public class DensityUtils {
    /**
     * dp转px
     *
     * @param context  上下文
     * @param dpVal    dp值
     * @return    像素值
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context  上下文
     * @param spVal    sp值
     * @return    像素值
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context  上下文
     * @param pxVal    像素值
     * @return   dp值
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param context  上下文
     * @param pxVal    像素值
     * @return   sp值
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }
}
