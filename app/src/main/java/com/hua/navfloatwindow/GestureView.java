package com.hua.navfloatwindow;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;

/**
 * @author hua
 * @version 2018/9/28 14:04
 */

public class GestureView extends View {

    private GestureDetector gestureDetector;
    private Context context;
    private boolean inLongPress = false;
    private float rawLastX;
    private float rawLastY;
    private float rawDownX;
    private float rawDownY;
    private int scaledTouchSlop;
    private boolean isBeingDragging = false;
    private WindowManager wm;
    private WindowManager.LayoutParams wmParams;

    public GestureView(Context context) {
        this(context, null);
    }

    public GestureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        this.context = context;
        gestureDetector = new GestureDetector(context, new GestureListener());
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                inLongPress = true;
                return true;
            }
        });
    }

    public void attachWindow(WindowManager wm, WindowManager.LayoutParams params) {
        this.wm = wm;
        this.wmParams = params;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float rawX = event.getRawX();
        float rawY = event.getRawY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                rawDownX = rawLastX = rawX;
                rawDownY = rawLastY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = (rawX - rawLastX);
                float deltaY = (rawY - rawLastY);
                if (!isBeingDragging && inLongPress) {
                    if (Math.abs(rawX - rawDownX) > scaledTouchSlop ||
                            Math.abs(rawY - rawDownY) > scaledTouchSlop) {
                        isBeingDragging = true;
                        float absX = Math.abs(rawX - rawDownX) - scaledTouchSlop;
                        deltaX = absX > 0 ? absX : 0;
                        float absY = Math.abs(rawY - rawDownY) - scaledTouchSlop;
                        deltaY = absY > 0 ? absY : 0;
                    }
                }

                if (isBeingDragging) {

                    moveWindow((int) deltaX, (int) deltaY);

                }

                rawLastX = rawX;
                rawLastY = rawY;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                inLongPress = false;
                isBeingDragging = false;
                break;
            default:
                break;
        }

//        gestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }

    private void moveWindow(int deltaX, int deltaY) {
        wmParams.x += deltaX;
        wmParams.y += deltaY;
        ViewGroup parent = (ViewGroup) getParent();
        wm.updateViewLayout(parent, wmParams);
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            return super.onSingleTapConfirmed(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) {
                vibrator.vibrate(200);
            }
            inLongPress = true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            if (mOnBeingDragged != null) {
//                mOnBeingDragged.onDragged(-(int)distanceX, -(int) distanceY);
//                Log.e("@@@hua", "rawx=" + distanceX + ",rawy=" + distanceY);
//            }
            return true;
        }
    }

}
