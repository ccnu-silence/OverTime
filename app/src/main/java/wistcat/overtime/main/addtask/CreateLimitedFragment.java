package wistcat.overtime.main.addtask;

import android.view.View;

import wistcat.overtime.R;

/**
 * @author wistcat 2016/8/25
 */
public class CreateLimitedFragment extends BaseCreateFragment {

    public static final String MARK = "Limited";

    public static CreateLimitedFragment getInstance() {
        return new CreateLimitedFragment();
    }

    public static CreateLimitedFragment getInstance(int cx, int cy) {
        CreateLimitedFragment fragment = getInstance();
        fragment.setArguments(createArgs(cx, cy));
        return fragment;
    }

    @Override
    int getTheme() {
        return R.style.LimitedTaskStyle;
    }

    @Override
    int getLayout() {
        return R.layout.fragment_create_limited;
    }

    @Override
    String getMark() {
        return MARK;
    }

    @Override
    int getTitle() {
        return R.string.create_limited;
    }

    @Override
    void initView(View layout) {

    }
}
