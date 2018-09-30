package com.hua.navfloatwindow;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import static android.content.Context.WINDOW_SERVICE;

/**
 * @author hua
 * @version 2018/9/29 14:54
 */

public class FloatMenuWindow implements View.OnClickListener {

    public static final String SP_NAME = "float_menu_window_location";
    public static final int DEFAULT_WIDTH = 400;
    @SuppressWarnings("SuspiciousNameCombination")
    public static final int DEFAULT_HEIGHT = DEFAULT_WIDTH;

    public static final int DEFAULT_X = 200;
    @SuppressWarnings("SuspiciousNameCombination")
    public static final int DEFAULT_Y = DEFAULT_X;
    public static final String SP_KEY_LOCATION_X = "key_location_x";
    public static final String SP_KEY_LOCATION_Y = "key_location_y";
    private final Context context;
    private ViewGroup floatMenuContainer;
    private boolean show = false;
    private View collapseView;
    private View expandView;
    private WindowManager.LayoutParams params;

    public static FloatMenuWindow get(Context context) {
        return new FloatMenuWindow(context);
    }

    private FloatMenuWindow(Context context) {
        this.context = context.getApplicationContext();
    }

    public void show() {
        if (show) {
            return;
        }

        final WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);

        if (wm != null) {
            params = new WindowManager.LayoutParams();
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            params.x = sp.getInt(SP_KEY_LOCATION_X, DEFAULT_X);
            params.y = sp.getInt(SP_KEY_LOCATION_Y, DEFAULT_Y);

            setCollapseFlag(params);

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            floatMenuContainer = (ViewGroup) layoutInflater.inflate(
                    R.layout.window_nav_float_container, null);

            collapseView = layoutInflater.inflate(R.layout.window_nav_float_collapse,
                    floatMenuContainer, false);
            GestureView gestureView = collapseView.findViewById(R.id.iv_float);
            gestureView.attachWindow(wm, params);
            gestureView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showExpand();
                }
            });


            expandView = layoutInflater.inflate(
                    R.layout.window_nav_float_expand, floatMenuContainer, false);
            LinearLayout llHome = expandView.findViewById(R.id.ll_home);
            llHome.setOnClickListener(this);
            LinearLayout llRecent = expandView.findViewById(R.id.ll_recent);
            llRecent.setOnClickListener(this);
            LinearLayout llBack = expandView.findViewById(R.id.ll_back);
            llBack.setOnClickListener(this);

            floatMenuContainer.addView(collapseView);
            wm.addView(floatMenuContainer, params);

            show = true;
        }
    }

    @Override
    public void onClick(View view) {
        showCollapse();
        switch (view.getId()) {
            case R.id.ll_home:

                break;
            case R.id.ll_recent:

                break;
            case R.id.ll_back:
                if (MyAccessService.accessService != null) {
                    MyAccessService.accessService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                }
                break;
            default:
                break;
        }

    }

    private void setCollapseFlag(WindowManager.LayoutParams params) {
        params.flags |=
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
    }

    private void setExpandFlag(WindowManager.LayoutParams params) {
        params.flags |=
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
    }

    private void showCollapse() {
        floatMenuContainer.removeAllViews();
        floatMenuContainer.addView(collapseView);
    }

    private void showExpand() {
        floatMenuContainer.removeAllViews();
        floatMenuContainer.addView(expandView);


    }

    public void dismiss() {
        if (!show) {
            return;
        }

        final WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);

        if (wm != null) {
            wm.removeView(floatMenuContainer);
            floatMenuContainer = null;
            show = false;
        }
    }

}
