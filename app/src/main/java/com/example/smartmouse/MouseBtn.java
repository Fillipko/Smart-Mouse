/**
 * Custom button element to allow for accessibility and haptics
 */
package com.example.smartmouse;

import android.content.Context;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;

public class MouseBtn extends com.google.android.material.button.MaterialButton {
    public MouseBtn(Context context) {
        super(context);
    }

    public MouseBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                break;
            case MotionEvent.ACTION_UP:
                performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                performClick();
                break;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }
}
