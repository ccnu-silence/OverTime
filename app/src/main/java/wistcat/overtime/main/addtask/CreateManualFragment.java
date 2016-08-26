package wistcat.overtime.main.addtask;

import android.view.View;

import wistcat.overtime.R;

/**
 * @author wistcat 2016/8/25
 */
public class CreateManualFragment extends BaseCreateFragment {

    public static final String MARK = "InfiniteManual";

    public static CreateManualFragment getInstance() {
        return new CreateManualFragment();
    }

    public static CreateManualFragment getInstance(int cx, int cy) {
        CreateManualFragment fragment = getInstance();
        fragment.setArguments(createArgs(cx, cy));
        return fragment;
    }

    @Override
    int getTheme() {
        return R.style.ManualTaskStyle;
    }

    @Override
    int getLayout() {
        return R.layout.fragment_create_manual;
    }

    @Override
    String getMark() {
        return MARK;
    }

    @Override
    int getTitle() {
        return R.string.create_manual;
    }

    @Override
    void initView(View layout) {

    }
}
