package wistcat.overtime.main.taskslist;

import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.List;

import wistcat.overtime.base.BasePresenter;
import wistcat.overtime.base.BaseView;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;

/**
 * @author wistcat 2016/9/12
 */
public interface TasksListContract {

    interface View extends BaseView<Presenter> {

        void showList(Cursor cursor);

        void clearList();

        void showMoreMenu();

        void dismissMoreMenu();

        void redirectTaskDetails(@NonNull Task task);

        void redirectEditTasksList();

        void redirectTaskGroupDetails();

        void redirectCreateTask();

        void showNoText(boolean isNull);

        void showDeleteDialog();

        void dismissDeleteDialog();

        void showItemMenu(@NonNull Task task);

        void dismissItemMenu();

        void showMoveDialog(@NonNull List<TaskGroup> groups);

        void dismissMoveDialog();

        void showSaveDialog();

        void dismissSaveDialog();

        void showToast(String msg);
    }

    interface Presenter extends BasePresenter {

        void loadTasks(String term);

        void onItemSelected(@NonNull Task task);

        void onItemEditSelected(@NonNull Task task);

        void openMoreMenu();

        void openTaskGroupDetails();

        void openEditTasksList();

        void openMoveDialog();

        void closeMoveDialog();

        void openDeleteDialog();

        void closeDeleteDialog();

        void openSaveDialog();

        void closeSaveDialog();

        void createNewTask();

        void doMove(@NonNull TaskGroup group);

        void doSave();

        void doDelete();
    }
}
