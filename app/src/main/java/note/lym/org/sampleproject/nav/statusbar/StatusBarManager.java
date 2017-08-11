package note.lym.org.sampleproject.nav.statusbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * 状态栏相关类
 *
 * @author yaoming.li
 * @since 2017-07-27 09:25
 */
public class StatusBarManager {

    public static void setStatusBar(Activity activity, int color, boolean topIsImage) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // SDK >= 21 (5.0)
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 21 > SDK >= 19 (4.4)
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        addStatusBar(activity, color, topIsImage);
    }

    public static void addStatusBar(Activity activity, int color, boolean topIsImage) {
        ViewGroup viewGroup = (ViewGroup) activity.findViewById(android.R.id.content);
        View statusBarView = viewGroup.getChildAt(0);
        //改变颜色时避免重复添加statusBarView
        if (statusBarView != null && statusBarView.getMeasuredHeight() == getStatusBarHeight(activity)) {
            statusBarView.setBackgroundColor(color);
            return;
        }
        statusBarView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity));
        if (!topIsImage) {
            viewGroup.setPadding(0, getStatusBarHeight(activity), 0, 0);
        }
        statusBarView.setBackgroundColor(color);
        viewGroup.addView(statusBarView, lp);
    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
