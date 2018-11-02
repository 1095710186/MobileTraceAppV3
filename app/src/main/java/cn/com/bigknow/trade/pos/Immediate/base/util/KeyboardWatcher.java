package cn.com.bigknow.trade.pos.Immediate.base.util;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.LinkedList;
import java.util.List;

public class KeyboardWatcher implements ViewTreeObserver.OnGlobalLayoutListener {

    private boolean isFullScreen = false;

    public interface KeyboardStateListener {
        void onSoftKeyboardOpened(int keyboardHeightInPx);

        void onSoftKeyboardClosed();
    }

    private final List<KeyboardStateListener> listeners = new LinkedList<>();
    private final View activityRootView;
    private int lastSoftKeyboardHeightInPx;
    private boolean isSoftKeyboardOpened;

    public KeyboardWatcher(View activityRootView) {
        this(activityRootView, false);
    }

    public KeyboardWatcher(View activityRootView, boolean isSoftKeyboardOpened) {
        this.activityRootView = activityRootView;
        this.isSoftKeyboardOpened = isSoftKeyboardOpened;
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setFullScreen(boolean isFullScreen) {
        this.isFullScreen = isFullScreen;
    }

    @Override
    public void onGlobalLayout() {
        final Rect r = new Rect();
        // r will be populated with the coordinates of your view that area still
        // visible.
        activityRootView.getWindowVisibleDisplayFrame(r);

        int heightDiff = activityRootView.getHeight() - (r.bottom - r.top);

        if (!isFullScreen) {
            int resourceId = activityRootView.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                heightDiff -= activityRootView.getResources().getDimensionPixelSize(resourceId);
            }
        }

        if (!isSoftKeyboardOpened && heightDiff > 200) { // if more than 200
            // pixels, its
            // probably a
            // keyboard...
            isSoftKeyboardOpened = true;
            notifyOnSoftKeyboardOpened(heightDiff);
        } else if (isSoftKeyboardOpened && heightDiff < 200) {
            isSoftKeyboardOpened = false;
            notifyOnSoftKeyboardClosed();
        }
    }

    public void setIsSoftKeyboardOpened(boolean isSoftKeyboardOpened) {
        this.isSoftKeyboardOpened = isSoftKeyboardOpened;
    }

    public boolean isSoftKeyboardOpened() {
        return isSoftKeyboardOpened;
    }

    /**
     * Default value is zero (0)
     *
     * @return last saved keyboard height in px
     */
    public int getLastSoftKeyboardHeightInPx() {
        return lastSoftKeyboardHeightInPx;
    }

    public void addKeyboardStateListener(KeyboardStateListener listener) {
        listeners.add(listener);
    }

    public void removeKeyboardStateListener(KeyboardStateListener listener) {
        listeners.remove(listener);
    }

    public void destroy() {
        if (activityRootView != null)
            if (Build.VERSION.SDK_INT >= 16) {
                activityRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            } else {
                activityRootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
    }


    private void notifyOnSoftKeyboardOpened(int keyboardHeightInPx) {
        this.lastSoftKeyboardHeightInPx = keyboardHeightInPx;
        for (KeyboardStateListener listener : listeners) {
            if (listener != null) {
                listener.onSoftKeyboardOpened(keyboardHeightInPx);
            }
        }
    }

    private void notifyOnSoftKeyboardClosed() {
        for (KeyboardStateListener listener : listeners) {
            if (listener != null) {
                listener.onSoftKeyboardClosed();
            }
        }
    }
}
