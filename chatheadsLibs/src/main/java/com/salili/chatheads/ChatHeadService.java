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




    }

    /**
     * Get Current instance *.
     *
     * @return single instance of ChatHeadService
     */





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


}
