package wistcat.overtime.main.main.tasks;

import android.database.Cursor;
import android.support.annotation.NonNull;

import wistcat.overtime.base.BasePresenter;
import wistcat.overtime.base.BaseView;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;

/**
 * @author wistcat 2016/9/2
 */
public interface MainTasksContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showList(Cursor tasks);

        void showLoadingTasksError();

        void showTaskDetails(Task task);

        void showTasksManage();

        void showTasksStatistics();

        void showAddTask();

        void showMoreTasks();

        void clearCursor();

        void showTaskMenu(String name, String group);

        void showRunningTaskMenu(String name, String group);

        void showToast(String msg);

        void dismissTaskMenu();

        void showDeleteDialog(@NonNull Task task);

        void dismissDeleteDialog();

        void showNoText(boolean isNull);

        void redirectGroup(@NonNull TaskGroup group);

        void redirectRunningPage();

        void redirectRunningPage(@NonNull Task task);

        void showRunningBottom();

        void hideRunningBottom();
    }

    interface Presenter extends BasePresenter {

        void loadTasks();

        void openTaskDetails(@NonNull Task task);

        void addNewTask();

        void manageTasks();

        void moreTasks();

        void statisticsTasks();

        void openTaskMenu(@NonNull Task task);

        void openDeleteDialog();

        void closeDeleteDialog();

        void openGroup();

        void doDelete();

        void doStart();

        void openRunningPage();

        void checkRunning();
    }

}
