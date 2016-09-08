package wistcat.overtime.main.tasksmanage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import wistcat.overtime.databinding.FragmentAddTaskgroupBinding;

/**
 * @author wistcat 2016/9/8
 */
public class AddTaskGroupFragment extends AppCompatDialogFragment {

    private TasksManagePresenter mPresenter;

    public static AddTaskGroupFragment getInstance() {
        return new AddTaskGroupFragment();
    }

    public void setPresenter(TasksManagePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentAddTaskgroupBinding binding = FragmentAddTaskgroupBinding.inflate(inflater, container, false);
        binding.setHandler(new HandleCreateTaskGroup(mPresenter));
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        Window window = getDialog().getWindow();
        window.setLayout(dm.widthPixels, window.getAttributes().height);
    }

}
