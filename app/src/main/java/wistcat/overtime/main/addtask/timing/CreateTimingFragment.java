package wistcat.overtime.main.addtask.timing;

import android.view.View;

import java.util.List;

import wistcat.overtime.R;
import wistcat.overtime.main.addtask.BaseCreateFragment;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;

/**
 * @author wistcat 2016/8/25
 */
public class CreateTimingFragment extends BaseCreateFragment {

    public static final int TYPE = Task.TYPE_TIMING;
    public static final String MARK = "InfiniteTiming";

    public static CreateTimingFragment getInstance() {
        return new CreateTimingFragment();
    }

    public static CreateTimingFragment getInstance(int cx, int cy, TaskGroup group) {
        CreateTimingFragment fragment = getInstance();
        fragment.setArguments(createArgs(cx, cy, group));
        return fragment;
    }

    @Override
    public void initView(View layout) {
        // TODO
    }

    @Override
    protected int getTheme() {
        return R.style.TimingTaskStyle;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_create_timing;
    }

    @Override
    protected String getMark() {
        return MARK;
    }

    @Override
    protected int getTitle() {
        return R.string.create_timing;
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
