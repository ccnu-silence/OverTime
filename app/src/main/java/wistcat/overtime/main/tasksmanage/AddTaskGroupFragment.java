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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import wistcat.overtime.databinding.FragmentAddTaskgroupBinding;

/**
 * 新建任务分组
 *
 * @author wistcat 2016/9/8
 */
public class AddTaskGroupFragment extends AppCompatDialogFragment {

    private TasksManageContract.Presenter mPresenter;

    public static AddTaskGroupFragment getInstance() {
        return new AddTaskGroupFragment();
    }

    public void setPresenter(TasksManageContract.Presenter presenter) {
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
        // DataBinding
        FragmentAddTaskgroupBinding binding = FragmentAddTaskgroupBinding.inflate(inflater, container, false);
        HandleCreateTaskGroup handle = new HandleCreateTaskGroup(mPresenter);
        binding.setHandler(handle);
        popupKeyboard(binding.edit);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // 用于设置Dialog的长宽
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        Window window = getDialog().getWindow();
        window.setLayout((int) (dm.widthPixels * 0.8), window.getAttributes().height);
    }

    /** 显示软键盘 */
    private void popupKeyboard(final EditText edit) {
        edit.requestFocus();
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        edit.post(new Runnable() {
            @Override
            public void run() {
                imm.showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

}
