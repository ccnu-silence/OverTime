package wistcat.overtime.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import wistcat.overtime.R;

/**
 * @author wistcat 2016/9/10
 */
public class OutButton extends TextView {

    private Drawable mForegroundDrawable;

    public OutButton(Context context) {
        super(context);
        init();
    }

    public OutButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OutButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mForegroundDrawable = ContextCompat.getDrawable(getContext(), R.drawable.foreground_grey);
        mForegroundDrawable.setCallback(this);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        mForegroundDrawable.setBounds(0, 0, w, h);
    }


    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        mForegroundDrawable.draw(canvas);
    }

    @Override
    public void drawableStateChanged(){
        super.drawableStateChanged();
        if(mForegroundDrawable.isStateful()){
            mForegroundDrawable.setState(getDrawableState());
        }
        invalidate();
    }

}
