package wistcat.overtime.main.addtask.limited;

import android.view.View;

import java.util.List;

import wistcat.overtime.R;
import wistcat.overtime.main.addtask.BaseCreateFragment;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;

/**
 * @author wistcat 2016/8/25
 */
public class CreateLimitedFragment extends BaseCreateFragment {
    public static final int TYPE = Task.TYPE_LIMITED;
    public static final String MARK = "Limited";

    public static CreateLimitedFragment getInstance() {
        return new CreateLimitedFragment();
    }

    public static CreateLimitedFragment getInstance(int cx, int cy, TaskGroup group) {
        CreateLimitedFragment fragment = getInstance();
        fragment.setArguments(createArgs(cx, cy, group));
        return fragment;
    }


    @Override
    protected int getTheme() {
        return R.style.LimitedTaskStyle;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_create_limited;
    }

    @Override
    protected String getMark() {
        return MARK;
    }

    @Override
    protected int getTitle() {
        return R.string.create_limited;
    }

    @Override
    protected void initView(View layout) {
        // TODO
    }

    @Override
    public void showSpinner(List<TaskGroup> groups) {
        // TODO
    }

    @Override
    public void quit() {
        // TODO
    }
}
