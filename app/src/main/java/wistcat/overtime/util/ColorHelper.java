package wistcat.overtime.util;

import android.graphics.Color;

/**
 * 简易的颜色变换计算工具，有待改进
 *
 * @author wistcat 2016/9/17
 */
public class ColorHelper {

    private int mFrom;
    private int mTo;
    private int MAX;
    private int MIN;
    private int LEN;
    private int[] base = new int[4];
    private int[] diff = new int[4];

    public ColorHelper(int colorFrom, int colorTo, int start, int end) {
        mFrom = colorFrom;
        mTo = colorTo;
        MAX = end;
        MIN = start;
        LEN = end - start;
        base[0] = Color.alpha(colorFrom);
        base[1] = Color.red(colorFrom);
        base[2] = Color.green(colorFrom);
        base[3] = Color.blue(colorFrom);
        diff[0] = Color.alpha(colorTo) - base[0];
        diff[1] = Color.red(colorTo) - base[1];
        diff[2] = Color.green(colorTo) - base[2];
        diff[3] = Color.blue(colorTo) - base[3];
    }

    public int getColor(int offset) {
        int color;
        if (offset > MAX) {
            color = mTo;
        } else if (offset < MIN) {
            color = mFrom;
        } else {
            float ratio = (1.0f * offset) / LEN;
            int alpha = base[0] + (int)(ratio * diff[0]);
            int red = base[1] + (int)(ratio * diff[1]);
            int green = base[2] + (int)(ratio * diff[2]);
            int blue = base[3] + (int)(ratio * diff[3]);
            color = Color.argb(alpha, red, green ,blue);
        }
        return color;
    }

}
