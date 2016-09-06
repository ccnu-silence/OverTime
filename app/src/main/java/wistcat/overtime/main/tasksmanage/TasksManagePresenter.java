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
import wistcat.overtime.data.db.TaskContract;
import wistcat.overtime.data.db.TaskTableHelper;
import wistcat.overtime.model.TaskGroup;

/**
 * @author wistcat 2016/9/5
 */
public class TasksManagePresenter implements TasksManageContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {

    private final static int TASK_GROUP_QUERY = 0x02;
    private final TasksManageContract.View mView;
    private final LoaderManager mLoaderManager;
    private final TaskRepository mRepository;
    private boolean isFirst;

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
            loadTaskGroups();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == TASK_GROUP_QUERY) {
            return new CursorLoader(
                    App.getInstance(),
                    TaskContract.buildTaskGroupUri(App.getInstance().getAccountName()),
                    TaskTableHelper.TASK_GROUP_PROJECTION,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == TASK_GROUP_QUERY) {
            mView.showTaskGroups(data);
            mRepository.setTaskGroupCache(data);
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
        mView.showCompletedTasks();
    }

    @Override
    public void redirectToRecycled() {
        mView.showRecycledTasks();
    }

    @Override
    public void saveNewTaskGroup(@NonNull TaskGroup group) {
        mRepository.saveTaskGroup(group);
    }

    @Override
    public void openTaskGroup(int groupId) {
        mView.showTaskList(groupId);
    }
}
