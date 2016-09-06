package wistcat.overtime.main.tasksmanage;

import android.database.Cursor;
import android.support.annotation.NonNull;

import wistcat.overtime.base.BasePresenter;
import wistcat.overtime.base.BaseView;
import wistcat.overtime.model.TaskGroup;

/**
 * @author wistcat 2016/9/5
 */
public interface TasksManageContract {

    interface View extends BaseView<Presenter> {

        void showTaskGroups(Cursor data);

        void showCreateDialog();

        void showGroupManage();

        void showCompletedTasks();

        void showRecycledTasks();

        void showTaskList(int groupId);

        void clearCursor();
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadTaskGroups();

        void redirectToCompleted();

        void redirectToRecycled();

        void saveNewTaskGroup(@NonNull TaskGroup group);

        void openTaskGroup(int groupId);
    }
}
