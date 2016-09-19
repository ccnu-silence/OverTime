package wistcat.overtime.main.taskslist;

import android.database.Cursor;
import android.support.annotation.NonNull;

import wistcat.overtime.base.BasePresenter;
import wistcat.overtime.base.BaseView;
import wistcat.overtime.model.Task;

/**
 * @author wistcat 2016/9/12
 */
public interface TasksListContract {

    interface View extends BaseView<Presenter> {

        int getGroupId();

        void showList(Cursor cursor);

        void clearList();

        void showTaskDetails(@NonNull Task task);

        void showMoreMenu();

        void hideMoreMenu();

        void showEditTasksList();

        void showTaskGroupDetails();

        void showScrollUp();

        void showNoText(boolean isNull);

        void redirectCreateTask();
    }

    interface Presenter extends BasePresenter {

        void loadTasks(String term);

        void onItemSelected(@NonNull Task task);

        void openMoreMenu();

        void openTaskGroupDetails();

        void openEditTasksList();

        void scrollTop();

        void createNewTask();
    }
}
