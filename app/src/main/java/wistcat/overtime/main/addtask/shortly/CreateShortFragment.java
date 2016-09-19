package wistcat.overtime.main.addtask.shortly;

import android.view.View;

import java.util.List;

import wistcat.overtime.R;
import wistcat.overtime.main.addtask.BaseCreateFragment;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;

/**
 * @author wistcat 2016/8/25
 */
public class CreateShortFragment extends BaseCreateFragment {

    public static final int TYPE = Task.TYPE_SHORT;
    public static final String MARK = "Short";

    public static CreateShortFragment getInstance() {
        return new CreateShortFragment();
    }

    public static CreateShortFragment getInstance(int cx, int cy, TaskGroup group) {
        CreateShortFragment fragment = getInstance();
        fragment.setArguments(createArgs(cx, cy, group));
        return fragment;
    }

    @Override
    protected int getTheme() {
        return R.style.ShortTaskStyle;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_create_short;
    }

    @Override
    protected String getMark() {
        return MARK;
    }

    @Override
    protected int getTitle() {
        return R.string.create_short;
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
