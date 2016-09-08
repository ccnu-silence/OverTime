package wistcat.overtime.main.tasksmanage;

import android.database.Cursor;

import wistcat.overtime.base.BasePresenter;
import wistcat.overtime.base.BaseView;

/**
 * @author wistcat 2016/9/5
 */
public interface TasksManageContract {

    interface View extends BaseView<Presenter> {

        /** 显示TaskGroup列表 */
        void showTaskGroups(Cursor data);

        /** 显示创建TaskGroup窗口 */
        void showCreateDialog();

        /** 跳转到列表管理 */
        void showGroupManage();

        /** 跳转到已完成的任务页 */
        void showCompletedTasks();

        /** 跳转到回收站 */
        void showRecycledTasks();

        /** 打开一个TaskGroup项 */
        void showTaskList(int groupId);

        /** 弹出管理menu */
        void showMoreMenu(android.view.View view);

        /** 清空CursorAdapter的数据 */
        void clearCursor();

        /** 关闭新建*/
        void hideCreateDialog();
    }

    interface Presenter extends BasePresenter {

        /** 接受从Activity返回的结果 */
        void result(int requestCode, int resultCode);

        /** 加载TaskGroup列表 */
        void loadTaskGroups();

        /** 打开完成任务列表 */
        void redirectToCompleted();

        /** 打开任务回收站列表 */
        void redirectToRecycled();

        /** 打开一个TaskGroup项 */
        void openTaskGroup(int groupId);

        /** 打开Popup菜单  */
        void openMoreMenu(android.view.View view);

        /** 打开添加TaskGroup窗口 */
        void openAddDialog();

        /** 打开列表管理 */
        void openEditList();

        /** 关闭新建Dialog */
        void closeCreateDialog();
    }
}
