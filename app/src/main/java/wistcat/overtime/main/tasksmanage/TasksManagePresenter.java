package wistcat.overtime.main.tasksmanage;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.data.datasource.TaskRepository;
import wistcat.overtime.data.db.SelectionBuilder;
import wistcat.overtime.data.db.TaskContract;
import wistcat.overtime.data.db.TaskTableHelper;
import wistcat.overtime.model.TaskGroup;
import wistcat.overtime.util.Const;

/**
 * @author wistcat 2016/9/5
 */
public class TasksManagePresenter implements TasksManageContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {

    private final static int TASK_GROUP_QUERY = 0x02;
    private final TasksManageContract.View mView;
    private final LoaderManager mLoaderManager;
    private final TaskRepository mRepository;
    private boolean isFirst = true;

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
            mView.showTaskGroups(data);
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
    public void redirectToCompleted() {
        TaskGroup group = new TaskGroup(Const.COMPLETED_GROUP_ID, 0,
                Const.DEFAULT_COMPLETED_GROUP, App.getInstance().getAccountName());
        mView.showCompletedTasks(group);
    }

    @Override
    public void redirectToRecycled() {
        TaskGroup group = new TaskGroup(Const.RECYCLED_GROUP_ID, 0,
                Const.DEFAULT_RECYCLED_GROUP, App.getInstance().getAccountName());
        mView.showRecycledTasks(group);
    }

    @Override
    public void openTaskGroup(@NonNull TaskGroup group) {
        mView.showTaskList(group);
    }

    @Override
    public void openMoreMenu(View view) {
        mView.showMoreMenu(view);
    }

    @Override
    public void openAddDialog() {
        mView.showCreateDialog();
    }

    @Override
    public void openEditList() {
        mView.showGroupManage();
    }

    @Override
    public void closeCreateDialog() {
        mView.hideCreateDialog();
    }

    @Override
    public void addNewTaskGroup(@NonNull TaskGroup taskGroup) {
        mRepository.saveTaskGroup(taskGroup);
    }
}
