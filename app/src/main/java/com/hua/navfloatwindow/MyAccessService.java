package com.hua.navfloatwindow;

import android.accessibilityservice.AccessibilityService;
import android.support.annotation.Nullable;
import android.support.v4.accessibilityservice.AccessibilityServiceInfoCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;

import java.util.List;

/**
 * @author hua
 * @version 2018/9/28 18:01
 */

public class MyAccessService extends AccessibilityService {

    public static @Nullable MyAccessService accessService;

    @Override
    public void onCreate() {
        super.onCreate();
        accessService = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessService = null;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                CharSequence packageName = event.getPackageName();
                CharSequence className = event.getClassName();
                List<CharSequence> text = event.getText();
                if (getPackageName().equals(packageName) &&
                        LinearLayout.class.getName().equals(className) &&
                        text != null && text.size() > 0) {
                    CharSequence s = text.get(0);
                    if (!TextUtils.isEmpty(s)) {

                    }
                }

                break;
            default:
                break;
        }


        Log.e("@@@hua", "onAccessibilityEvent: " + event);
//        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }


    @Override
    public void onInterrupt() {
        Log.e("@@@hua", "onInterrupt: ");
    }
}
