package com.hua.navfloatwindow;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Instrumentation;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.input.InputManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hua
 * @version 2018/9/27 15:33
 */

public class NavWindowFloatService extends Service {

    private View floatMenuContainer;

    @Override
    public void onCreate() {
        super.onCreate();
//        showFloatMenu(300, 300);
        FloatMenuWindow.get(this).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestDrawOverLays() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "can not DrawOverlays", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + this.getPackageName()));
            //startActivityForResult(intent, 12);
        } else {
            doStart();
        }
    }


    private void showFloatMenu(int x, int y) {
        final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        if (wm != null) {
            final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            params.width = 400;
            params.height = 400;
            params.x = x;
            params.y = y;
            params.flags |=
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;

            if (floatMenuContainer == null) {
                floatMenuContainer = LayoutInflater.from(this).inflate(
                        R.layout.window_nav_float_collapse, null);
                final GestureView gestureView = floatMenuContainer.findViewById(R.id.iv_float);
//                gestureView.setOnBeingDragged(new GestureView.OnBeingDragged() {
//                    @Override
//                    public void onDragged(int deltaX, int deltaY) {
//                        params.x += deltaX;
//                        params.y += deltaY;
//                        wm.updateViewLayout(floatMenuContainer, params);
////                        ViewCompat.offsetLeftAndRight(gestureView,deltaX);
////                        ViewCompat.offsetTopAndBottom(gestureView,deltaY);
//                    }
//                });
                wm.addView(floatMenuContainer, params);
            } else {
                wm.updateViewLayout(floatMenuContainer, params);
            }
        }
    }

    private void doStart() {

    }

    private void useInstrumentation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Instrumentation inst = new Instrumentation();
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
            }
        }).start();
    }

    private void useInput() {
        InputManager im = (InputManager) getSystemService(INPUT_SERVICE);
        if (im != null) {
            Method method = null;
            try {
                method = InputManager.class.getDeclaredMethod("injectInputEvent", InputEvent.class, int.class);
                method.setAccessible(true);
                long time = System.currentTimeMillis();
                final KeyEvent ev = new KeyEvent(time, time, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK,
                        0,
                        0, KeyCharacterMap.VIRTUAL_KEYBOARD, 0,
                        KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY,
                        InputDevice.SOURCE_KEYBOARD);

//                            final KeyEvent ev = new KeyEvent(time, time,
//                                    KeyEvent.ACTION_UP, 26, 0,
//                                    0, -1, 0,
//                                    KeyEvent.FLAG_FROM_SYSTEM | KeyEvent.FLAG_VIRTUAL_HARD_KEY,
//                                    InputDevice.SOURCE_KEYBOARD);

                method.invoke(im, ev, 0);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("@@@hua", "inputManager is null");
        }
    }

}
