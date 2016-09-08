package wistcat.overtime.main.tasksmanage;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.design.widget.TextInputLayout;

import com.android.databinding.library.baseAdapters.BR;

import wistcat.overtime.App;

/**
 * 用于DataBinding对新建TaskGroup的处理
 *
 * @author wistcat 2016/9/8
 */
public class HandleCreateTaskGroup extends BaseObservable {
    // TODO: 暂时没有添加逻辑，只是测试一下DataBinding的使用情况

    private TasksManagePresenter mPresenter;
    private String mInput;

    public HandleCreateTaskGroup(TasksManagePresenter presenter) {
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
        App.showToast("退出");
        mPresenter.closeCreateDialog();
        mPresenter = null;
    }

    public void onConfirm(TextInputLayout layout) {
        if (mInput == null || mInput.trim().equals("")) {
            layout.setError("名称不能为空");
        } else {
            App.showToast("新建 " + mInput);
            mPresenter.closeCreateDialog();
            mPresenter = null;
        }
    }

    public void clearError(TextInputLayout layout) {
        layout.setError(null);
    }
}
