package wistcat.overtime.widget.spans;

import android.graphics.Color;
import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;

public class MutableForegroundColorSpan extends ForegroundColorSpan{

    private int mAlpha;
    private int mColor;

    public MutableForegroundColorSpan(int alpha, int color) {
        super(color);
        mAlpha = alpha;
        mColor = color;
    }


    public MutableForegroundColorSpan(Parcel parcel) {
        super(parcel);
        mAlpha = parcel.readInt();
        mColor = parcel.readInt();
    }


    @Override
    public void  writeToParcel(Parcel dest, int flag) {
        super.writeToParcel(dest, flag);
        dest.writeInt(mAlpha);
        dest.writeInt(mColor);
    }


    @Override
    public int getForegroundColor() {
        return Color.argb(mAlpha, Color.red(mColor), Color.green(mColor), Color.blue(mColor));
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setColor(getForegroundColor());
    }

    public void setForegroundColor(int color) {
        mColor = color;
    }

    public void setAlpha(int alpha) {
        mAlpha = alpha;
    }

    public int getAlpha() {
        return mAlpha;
    }

}
