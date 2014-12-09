package com.salili.chatheads;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by SALILI on 12/11/2014.
 */
public class ChatHeadsManager implements View.OnTouchListener {

    private static ChatHeadsManager instance;
    private boolean isfixed;
    private FixPosition positionToFixit;
    private View chatHeadView;
    private Context context;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private DisplayMetrics dm;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    /**
     * The is visible.
     */
    private boolean isVisible = false;

    public ChatHeadsManager getInstance() {
        if (instance == null) {
            instance = new ChatHeadsManager();
        }
        return instance;
    }

    public ChatHeadsManager() {

    }

    public ChatHeadsManager(final boolean isfixed, final FixPosition positionToFixit, final View chatHeadView, final Context context) {
        init(isfixed, positionToFixit, chatHeadView, context);
    }

    public void init(final boolean isfixed, final FixPosition positionToFixit, final View chatHeadView, final Context context) {
        this.isfixed = isfixed;
        this.positionToFixit = positionToFixit;
        this.chatHeadView = chatHeadView;
        this.context = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        this.chatHeadView.setOnTouchListener(this);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP;

        this.chatHeadView.setClickable(true);
        this.chatHeadView.setEnabled(true);
    }

    @Override
    public boolean onTouch(final View view, final MotionEvent event) {
        Log.w("chatHead", "event = " + event.getRawX());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("chatHead", "MotionEvent.ACTION_DOWN");
                initialX = params.x;
                initialY = params.y;
                initialTouchX = event.getRawX();
                initialTouchY = event.getRawY();
                return true;
            case MotionEvent.ACTION_UP:
                Log.e("chatHead", "MotionEvent.ACTION_UP");
                //  setPadding();

                windowManager.updateViewLayout(chatHeadView, params);
                if (event.getRawX() <= (initialTouchX * 1.05)
                        && event.getRawX() >= (initialTouchX * 0.95)
                        && event.getRawY() <= (initialTouchY * 1.05)
                        && event.getRawY() >= (initialTouchY * 0.95)) {
                    Log.e("chatHead", "MotionEvent.ACTION_UP chatHeadClicked");
                    // chatHeadClicked();
                }

                return true;
            case MotionEvent.ACTION_MOVE:
                Log.e("chatHead", "MotionEvent.ACTION_MOVE");
                //clearPadding();

                params.y = initialY
                        + (int) (event.getRawY() - initialTouchY);
                windowManager.updateViewLayout(chatHeadView, params);

                return true;
        }
        return false;
    }

    private enum FixPosition {
        FIX_TOP,
        FIX_BOTTOM,
        FIX_LEFT,
        FIX_RIGHT;
    }

    ;


    public void setInitialPosition(final float x, final float y) {
        params.y = (int) (y * dm.density);
        params.x = (int) (x * dm.density);
    }

    public void hideChatHead() {
        if (chatHeadView != null && isVisible) {
            windowManager.removeView(chatHeadView);
            isVisible = false;
        }
    }

    public void showChatHead() {

        if (!isVisible && chatHeadView != null) {
            windowManager.addView(chatHeadView, params);
            isVisible = true;
        }

    }
}
