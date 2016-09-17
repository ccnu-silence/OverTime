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

        /** 显示TaskGroup列表 */
        void showList(Cursor data);

        /** 显示创建TaskGroup窗口 */
        void showCreateDialog();

        /** 跳转到列表管理 */
        void redirectGroupsManage();

        /** 跳转到已完成的任务页 */
        void redirectCompletedList(@NonNull TaskGroup group);

        /** 跳转到回收站 */
        void redirectRecycledList(@NonNull TaskGroup group);

        /** 打开一个TaskGroup项 */
        void redirectTasksList(@NonNull TaskGroup group);

        /** 显示Group管理menu */
        void showEditMenu();

        void showItemEditMenu();

        /** 清空CursorAdapter的数据 */
        void clearCursor();

        /** 关闭新建Dialog */
        void dismissCreateDialog();

        void showGroupDetails();

        void showDeleteDialog();

        void dismissDeleteDialog();

        void dismissMenu();

        void showToast(String msg);
    }

    interface Presenter extends BasePresenter {

        /** 接受从Activity返回的结果 */
        void result(int requestCode, int resultCode);

        /** 加载TaskGroup列表 */
        void loadTaskGroups();

        /** 打开完成任务列表 */
        void openCompletedList();

        /** 打开任务回收站列表 */
        void openRecycledList();

        /** 打开一个TaskGroup项 */
        void openTasksList(@NonNull TaskGroup group);

        /** 打开Popup菜单  */
        void openEditMenu();

        void openItemEditMenu(@NonNull TaskGroup group);

        /** 打开添加TaskGroup窗口 */
        void openCreateDialog();

        /** 打开列表管理 */
        void openGroupsManage();

        void openGroupDetails();

        /** 关闭新建Dialog */
        void closeCreateDialog();

        void openDeleteDialog();

        void closeDeleteDialog();

        void onMenuSelected(int i);

        /** 添加新的任务组 */
        void addNewTaskGroup(@NonNull TaskGroup group);

        void deleteTaskGroup();
    }
}
