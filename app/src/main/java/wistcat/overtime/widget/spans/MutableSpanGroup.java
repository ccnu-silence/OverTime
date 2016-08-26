package wistcat.overtime.widget.spans;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MutableSpanGroup {

    private float mProgress;
    private List<MutableForegroundColorSpan> mSpans;
    private List<Integer> mIndexes;

    public MutableSpanGroup() {
        mProgress = 0;
        mSpans = new ArrayList<>();
        mIndexes = new ArrayList<>();
    }

    public void addSpan(MutableForegroundColorSpan span) {
        span.setAlpha(0);
        mIndexes.add(mIndexes.size());
        mSpans.add(span);
    }

    /** 乱序 */
    public void shuffle() {
        Collections.shuffle(mIndexes);
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        mProgress = progress;
        final int size = mSpans.size();
        float rate = 1.0f * size * progress;
        /*
         * if size = 5
         * rate:        0 ~ 1.0 ~ 2.0 ~ 3.0 ~ 4.0 ~ 5.0
         * progress:    0 ~ 1/5 ~ 2/5 ~ 3/5 ~ 4/5 ~ 5/5
         */
        for (int i = 0; i < size; i++) {
            MutableForegroundColorSpan span = mSpans.get(mIndexes.get(i));
            if (rate >= 1.f) {
                span.setAlpha(255); // 该项之前的span都不透明
                rate += -1.f;
            } else {
                span.setAlpha((int)(255 * rate));
                rate = 0.0f; // 该项后面的span都为透明
            }
        }

    }


}
