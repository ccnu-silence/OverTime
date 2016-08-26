package wistcat.overtime.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import wistcat.overtime.R;

/**
 * 给CardView添加点击效果
 *
 * @author wistcat
 */
public class OutCardView extends CardView{

    private Drawable mForegroundDrawable;

    public OutCardView(Context context) {
        super(context);
        init();
    }

    public OutCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OutCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mForegroundDrawable = ContextCompat.getDrawable(getContext(), R.drawable.foreground_dim_grey);
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

