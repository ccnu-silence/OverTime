package wistcat.overtime.main.tasksmanage;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.data.datasource.TaskRepository;
import wistcat.overtime.data.db.SelectionBuilder;
import wistcat.overtime.data.db.TaskContract;
import wistcat.overtime.data.db.TaskTableHelper;
import wistcat.overtime.interfaces.ResultCallback;
import wistcat.overtime.model.TaskGroup;
import wistcat.overtime.util.Const;

/**
 * @author wistcat 2016/9/5
 */
public class TasksManagePresenter implements TasksManageContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MENU_TYPE_GROUP = 1;
    private static final int MENU_TYPE_ITEM = 2;
    private final static int TASK_GROUP_QUERY = 0x02;
    private final TasksManageContract.View mView;
    private final LoaderManager mLoaderManager;
    private final TaskRepository mRepository;

    private boolean isFirst = true;
    private TaskGroup mDeleteGroup;
    private int mMenuType;

    @Inject
    public TasksManagePresenter(TasksManageContract.View view, LoaderManager manager, TaskRepository repository) {
        mView = view;
        mLoaderManager = manager;
        mRepository = repository;
    }

    @Inject
    public void setup() {
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if (isFirst) {
            isFirst = false;
            loadTaskGroups();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == TASK_GROUP_QUERY) {
            SelectionBuilder builder = new SelectionBuilder();
            builder.notIn(TaskContract.TaskGroupEntry._ID, null)
                    .notIn(null, String.valueOf(Const.COMPLETED_GROUP_ID))
                    .notIn(null, String.valueOf(Const.RECYCLED_GROUP_ID));
            return new CursorLoader(
                    App.getInstance(),
                    TaskContract.buildTaskGroupUri(App.getInstance().getAccountName()),
                    TaskTableHelper.TASK_GROUP_PROJECTION,
                    builder.getSelection(),
                    builder.getSelectionArgs(),
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == TASK_GROUP_QUERY) {
            mView.showList(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == TASK_GROUP_QUERY) {
            mView.clearCursor();
        }
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // TODO
    }

    @Override
    public void loadTaskGroups() {
        if (mLoaderManager.getLoader(TASK_GROUP_QUERY) == null) {
            mLoaderManager.initLoader(TASK_GROUP_QUERY, null, this);
        } else {
            mLoaderManager.restartLoader(TASK_GROUP_QUERY, null, this);
        }
    }

    @Override
    public void openCompletedList() {
        TaskGroup group = new TaskGroup(Const.COMPLETED_GROUP_ID, 0,
                Const.DEFAULT_COMPLETED_GROUP, App.getInstance().getAccountName());
        mView.redirectCompletedList(group);
    }

    @Override
    public void openRecycledList() {
        TaskGroup group = new TaskGroup(Const.RECYCLED_GROUP_ID, 0,
                Const.DEFAULT_RECYCLED_GROUP, App.getInstance().getAccountName());
        mView.redirectRecycledList(group);
    }

    @Override
    public void openTasksList(@NonNull TaskGroup group) {
        mView.redirectTasksList(group);
    }

    @Override
    public void openEditMenu() {
        mMenuType = MENU_TYPE_GROUP;
        mView.showEditMenu();
    }

    @Override
    public void openItemEditMenu(@NonNull TaskGroup group) {
        mDeleteGroup = group;
        mMenuType = MENU_TYPE_ITEM;
        mView.showItemEditMenu();
    }

    @Override
    public void openCreateDialog() {
        mView.dismissMenu();
        mView.showCreateDialog();
    }

    @Override
    public void openGroupsManage() {
        mView.dismissMenu();
        mView.redirectGroupsManage();
    }

    @Override
    public void openGroupDetails() {
        mView.dismissMenu();
        mView.showGroupDetails();
    }

    @Override
    public void closeCreateDialog() {
        mView.dismissCreateDialog();
    }

    @Override
    public void openDeleteDialog() {
        mView.dismissMenu();
        mView.showDeleteDialog();
    }

    @Override
    public void closeDeleteDialog() {
        mView.dismissDeleteDialog();
    }

    @Override
    public void onMenuSelected(int i) {
        switch (i) {
            case 0:
                if (mMenuType == MENU_TYPE_GROUP) {         //添加任务组
                    openCreateDialog();
                } else if (mMenuType == MENU_TYPE_ITEM) {   //编辑任务组
                    openGroupDetails();
                }
                break;
            case 1:
                if (mMenuType == MENU_TYPE_GROUP) {         //管理任务组
                    openGroupsManage();
                } else if (mMenuType == MENU_TYPE_ITEM) {   //删除任务组
                    openDeleteDialog();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void addNewTaskGroup(@NonNull TaskGroup taskGroup) {
        mRepository.saveTaskGroup(taskGroup, new ResultCallback() {
            @Override
            public void onSuccess() {
                mView.showToast("添加成功");
            }

            @Override
            public void onError() {
                mView.showToast("添加失败");
            }
        });
    }

    @Override
    public void deleteTaskGroup() {
        if (mDeleteGroup == null) {
            throw new NullPointerException("没有指定的TaskGroup");
        }
        mRepository.deleteTaskGroup(mDeleteGroup, new ResultCallback() {
            @Override
            public void onSuccess() {
                mView.showToast("删除成功");
            }

            @Override
            public void onError() {
                mView.showToast("删除失败");
            }
        });
        mView.dismissDeleteDialog();
    }
}
