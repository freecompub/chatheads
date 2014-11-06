/**
 *
 */
package com.salili.chatheads;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.fnac.fnacdirect.common.log.Log;
import com.fnac.fnacdirect.common.manager.RightPaneFragmentManager;
import com.fnac.fnacdirect.common.util.UIUtil;
import com.fnac.fnacdirect.ui.activity.customer.CardViewActivity;
import com.fnac.fnacdirect.ui.activity.customer.CardViewFragment;

/**
 * The Class ChatHeadService.
 *
 * @author SALILI
 */
public class ChatHeadService {

    /**
     * Singleton property.
     */
    private static ChatHeadService instance;

    /**
     * The window manager.
     */
    private WindowManager windowManager;

    /**
     * The chat head.
     */
    private ImageView chatHead;

    /**
     * The params.
     */
    private WindowManager.LayoutParams params;

    /**
     * The width.
     */
    private int width;

    /**
     * The is visible.
     */
    private boolean isVisible = false;

    /**
     * The current activity.
     */
    private Activity currentActivity;

    /**
     * The right padding.
     */
    private int rightPadding = 0;


    /**
     * Instantiates a new chat head service.
     */
    /**
     *
     */
    public ChatHeadService(Context context) {

        windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;

        if (dm.density <= 1) {
            rightPadding = -40;
        } else {
            rightPadding = -60;
        }

        chatHead = new ImageView(context);

        setAdhCard();
        setPadding();

        chatHead.setClickable(true);
        chatHead.setEnabled(true);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP;

        setInitialPosition(dm);
        // params.x = width;
        //
        // if (dm.density <= 1) {
        // params.y = 100;
        // } else
        // params.y = 200;

        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
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
                        setPadding();

                        if (UIUtil.isTablet()) {
                            params.y = 0;
                        } else {
                            params.x = width;
                        }

                        windowManager.updateViewLayout(chatHead, params);
                        if (event.getRawX() <= (initialTouchX * 1.05)
                                && event.getRawX() >= (initialTouchX * 0.95)
                                && event.getRawY() <= (initialTouchY * 1.05)
                                && event.getRawY() >= (initialTouchY * 0.95)) {
                            chatHeadClicked();
                        }
                        // else {
                        // decalage=1;
                        // }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        Log.e("chatHead", "MotionEvent.ACTION_MOVE");
                        clearPadding();

                        if (UIUtil.isTablet()) {
                            params.x = initialX
                                    + (int) (event.getRawX() - initialTouchX);
                        } else {
                            params.x = initialX
                                    + (int) (event.getRawX() - initialTouchX * 1.5);
                        }

                        params.y = initialY
                                + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(chatHead, params);

                        return true;
                }
                return false;
            }
        });

    }

    /**
     * Get Current instance *.
     *
     * @return single instance of ChatHeadService
     */
    /**
     * @return
     */
    public static ChatHeadService getInstance() {
        if (instance == null) {
            instance = new ChatHeadService();
        }
        return instance;
    }

    /**
     * Show chat head.
     *
     * @param activity the activity
     */
    public void showChatHead(final Activity activity) {

        if (!isVisible && CustomerService.getInstance().isConnected()
                && CustomerService.getInstance().isAdherent()) {

            if (CustomerService.getInstance().isOneMember()) {
                setAdhOneCard();
            } else {
                setAdhCard();
            }

            windowManager.addView(chatHead, params);
            isVisible = true;
            currentActivity = activity;
        }

    }


    public void updateChatHead(final Activity activity) {
        currentActivity = activity;
    }

    /**
     * Hide chat head.
     */
    /**
     *
     */
    public void hideChatHead() {
        if (chatHead != null && isVisible) {
            windowManager.removeView(chatHead);
            isVisible = false;
            currentActivity = null;
        }
    }

	/*
     * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */

    /**
     * Chat head clicked.
     */
    public void chatHeadClicked() {

        if (UIUtil.isTablet()) {
            RightPaneFragmentManager.showFragmentInRightPane(
                    (FragmentActivity) currentActivity, new CardViewFragment(),
                    false);
        } else {

            Intent intent = new Intent(currentActivity, CardViewActivity.class);
            currentActivity.startActivity(intent);
        }

    }

    /**
     * Sets the adh card.
     */
    public void setAdhCard() {

        if (UIUtil.isTablet()) {
            chatHead.setImageResource(R.drawable.common_card_adh_tab);
        } else {
            chatHead.setImageResource(R.drawable.common_card_adh);
        }

    }

    /**
     * Sets the adh one card.
     */
    public void setAdhOneCard() {
        if (UIUtil.isTablet()) {
            chatHead.setImageResource(R.drawable.common_card_adhone_tab);
        } else {
            chatHead.setImageResource(R.drawable.common_card_adhone);
        }

    }

    /**
     * Sets the padding.
     */
    public void setPadding() {
        if (UIUtil.isTablet()) {
            chatHead.setPadding(0, rightPadding, 0, 0);
        } else {
            chatHead.setPadding(0, 0, rightPadding, 0);
        }
    }

    /**
     * Clear padding.
     */
    public void clearPadding() {
        chatHead.setPadding(0, 0, 0, 0);
    }

    /**
     * Sets the initial position.
     *
     * @param dm the new initial position
     */
    public void setInitialPosition(final DisplayMetrics dm) {
        if (UIUtil.isTablet()) {
            params.y = 0;
            params.x = 0;

        } else {
            params.x = width;

            params.y = (int) (45 * dm.density);
        }
    }
}
