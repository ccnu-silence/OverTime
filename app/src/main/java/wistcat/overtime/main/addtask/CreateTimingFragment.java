package wistcat.overtime.main.addtask;

import android.view.View;

import wistcat.overtime.R;

/**
 * @author wistcat 2016/8/25
 */
public class CreateTimingFragment extends BaseCreateFragment {

    public static final String MARK = "InfiniteTiming";

    public static CreateTimingFragment getInstance() {
        return new CreateTimingFragment();
    }

    public static CreateTimingFragment getInstance(int cx, int cy) {
        CreateTimingFragment fragment = getInstance();
        fragment.setArguments(createArgs(cx, cy));
        return fragment;
    }

    @Override
    public void initView(View layout) {

    }

    @Override
    int getTheme() {
        return R.style.TimingTaskStyle;
    }

    @Override
    int getLayout() {
        return R.layout.fragment_create_timing;
    }

    @Override
    String getMark() {
        return MARK;
    }

    @Override
    int getTitle() {
        return R.string.create_timing;
    }
}
