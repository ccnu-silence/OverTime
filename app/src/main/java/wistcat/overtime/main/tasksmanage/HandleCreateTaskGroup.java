package wistcat.overtime.main.tasksmanage;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.design.widget.TextInputLayout;

import com.android.databinding.library.baseAdapters.BR;

import wistcat.overtime.App;
import wistcat.overtime.data.TaskEngine;
import wistcat.overtime.model.TaskGroup;

/**
 * 用于DataBinding对新建TaskGroup的处理
 *
 * @author wistcat 2016/9/8
 */
public class HandleCreateTaskGroup extends BaseObservable {

    private TasksManageContract.Presenter mPresenter;
    private String mInput;

    public HandleCreateTaskGroup(TasksManageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Bindable
    public String getInput() {
        return mInput;
    }

    public void setInput(String input) {
        mInput = input;
        notifyPropertyChanged(BR.input);
    }

    public void onQuit() {
        quit();
    }

    public void onConfirm(TextInputLayout layout) {
        if (mInput == null || mInput.trim().equals("")) {
            layout.setError("名称不能为空");
        } else {
            layout.setError(null);
            TaskGroup taskGroup = new TaskGroup(0, TaskEngine.createId(), mInput, getAccount());
            mPresenter.addNewTaskGroup(taskGroup);
            quit();
        }
    }

    private void quit() {
        mPresenter.closeCreateDialog();
        mPresenter = null;
    }

    public void clearError(TextInputLayout layout) {
        layout.setError(null);
    }

    private String getAccount() {
        return App.getInstance().getAccountName();
    }
}
