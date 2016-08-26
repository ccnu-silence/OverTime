package wistcat.overtime.main.addtask;

import android.view.View;

import wistcat.overtime.R;

/**
 * @author wistcat 2016/8/25
 */
public class CreateShortFragment extends BaseCreateFragment {

    public static final String MARK = "Short";

    public static CreateShortFragment getInstance() {
        return new CreateShortFragment();
    }

    public static CreateShortFragment getInstance(int cx, int cy) {
        CreateShortFragment fragment = getInstance();
        fragment.setArguments(createArgs(cx, cy));
        return fragment;
    }

    @Override
    int getTheme() {
        return R.style.ShortTaskStyle;
    }

    @Override
    int getLayout() {
        return R.layout.fragment_create_short;
    }

    @Override
    String getMark() {
        return MARK;
    }

    @Override
    int getTitle() {
        return R.string.create_short;
    }

    @Override
    void initView(View layout) {

    }
}
