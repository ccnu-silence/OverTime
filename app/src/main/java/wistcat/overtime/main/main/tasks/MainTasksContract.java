package wistcat.overtime.main.main.tasks;

import android.database.Cursor;
import android.support.annotation.NonNull;

import wistcat.overtime.base.BasePresenter;
import wistcat.overtime.base.BaseView;
import wistcat.overtime.model.Task;

/**
 * @author wistcat 2016/9/2
 */
public interface MainTasksContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showTasks(Cursor tasks);

        void showNoTasks();

        void showLoadingTasksError();

        void showTaskDetails(Task task);

        void showTasksManage();

        void showTasksStatistics();

        void showAddTask();

        void showMoreTasks();

        void clearCursor();
    }

    interface Presenter extends BasePresenter {

        void loadTasks();

        void openTaskDetails(@NonNull Task task);

        void addNewTask();

        void manageTasks();

        void moreTasks();

        void statisticsTasks();
    }

}
