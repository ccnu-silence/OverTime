package wistcat.overtime.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import wistcat.overtime.R;

/**
 * 点击覆盖效果
 *
 * @author wistcat 2016/9/7
 */
public class OutLinearLayout extends LinearLayout {

    private Drawable mForegroundDrawable;

    public OutLinearLayout(Context context) {
        super(context);
        init();
    }

    public OutLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OutLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mForegroundDrawable = ContextCompat.getDrawable(getContext(), R.drawable.foreground_dim_grey_full);
        mForegroundDrawable.setCallback(this);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mForegroundDrawable.setBounds(0, 0, w, h);
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        mForegroundDrawable.draw(canvas);
    }

    @Override
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (mForegroundDrawable.isStateful()) {
            mForegroundDrawable.setState(getDrawableState());
        }
        invalidate();
    }
}
