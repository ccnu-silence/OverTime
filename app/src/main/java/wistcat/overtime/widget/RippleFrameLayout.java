package wistcat.overtime.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 用作根布局，展示水波展开效果
 */
public class RippleFrameLayout extends FrameLayout {

    private Path mRipplePath;
    private float mCenterX, mCenterY;
    private float mRadius;
    private boolean mClipOutlines;

    public RippleFrameLayout(Context context) {
        super(context);
        init();
    }

    public RippleFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RippleFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRipplePath = new Path();
        mClipOutlines = false;
        setWillNotDraw(false);
        setLayerToSW();
    }

    /* 关闭硬件加速 */
    private void setLayerToSW() {
        if (!isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    /* 设置动画中心点 */
    public void setCenter(float centerX, float centerY) {
        mCenterX = centerX;
        mCenterY = centerY;
    }

    public void setClipOutlines(boolean clip) {
        mClipOutlines = clip;
    }

    /* 动画接口，改变显示半径 */
    public void setRadius(float radius) {
        mRadius = radius;
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {

        if (!mClipOutlines) {
            super.draw(canvas);
            return;
        }

        final int saved = canvas.save();
        mRipplePath.reset();
        mRipplePath.addCircle(mCenterX, mCenterY, mRadius, Path.Direction.CW);
        canvas.clipPath(mRipplePath);
        super.draw(canvas);
        canvas.restoreToCount(saved);
    }

}
