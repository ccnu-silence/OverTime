package wistcat.overtime.main.addtask;

import java.util.List;

import wistcat.overtime.base.BasePresenter;
import wistcat.overtime.base.BaseView;
import wistcat.overtime.model.TaskGroup;

/**
 * @author wistcat 2016/9/18
 */
public interface CreateTaskContract {

    interface View extends BaseView<Presenter> {

        void showSpinner(List<TaskGroup> groups);

        void quit();
    }

    interface Presenter extends BasePresenter {

        void loadSpinner();

        void saveTask(TaskGroup group, String taskName, String taskDescription, int type);

        void quit();
    }
}
