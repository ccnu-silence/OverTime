package wistcat.overtime.main.main.tasks;

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
import wistcat.overtime.model.Task;

/**
 * @author wistcat 2016/9/2
 */
public class MainTasksPresenter implements MainTasksContract.Presenter, LoaderManager.LoaderCallbacks<Cursor>{

    private final static int TASKS_QUERY = 1;
    private final MainTasksContract.View mView;
    private final LoaderManager mLoaderManager;
    private final TaskRepository mRepository;
    private boolean isFirst = true;

    @Inject
    public MainTasksPresenter(MainTasksContract.View view, LoaderManager loaderManager, TaskRepository repository) {
        mView = view;
        mLoaderManager = loaderManager;
        mRepository = repository;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == TASKS_QUERY) {
            mView.setLoadingIndicator(true);
            return new CursorLoader(
                    App.getInstance(),
                    TaskContract.buildTasksUriWith(App.getInstance().getAccountName()),
                    TaskTableHelper.TASK_PROJECTION,
                    TaskTableHelper.WHERE_TASK_STATE,
                    TaskTableHelper.WHERE_TASK_STATE_ACTIVATE,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == TASKS_QUERY) {
            mRepository.clearDirty();
            mView.setLoadingIndicator(false);
            if (data != null) {
                mView.showTasks(data);
            } else {
                mView.showNoTasks();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == TASKS_QUERY) {
            mView.clearCursor();
        }
    }

    @Override
    public void loadTasks() {
        if (mLoaderManager.getLoader(TASKS_QUERY) == null) {
            mLoaderManager.initLoader(TASKS_QUERY, null, this);
        } else {
            mLoaderManager.restartLoader(TASKS_QUERY, null, this);
        }
    }

    @Override
    public void openTaskDetails(@NonNull Task task) {
        mView.showTaskDetails(task);
    }

    @Override
    public void addNewTask() {
        mView.showAddTask();
    }

    @Override
    public void manageTasks() {
        mView.showTasksManage();
    }

    @Override
    public void statisticsTasks() {
        mView.showTasksStatistics();
    }

    @Override
    public void moreTasks() {
        mView.showMoreTasks();
    }

    @Override
    public void start() {
        if (isFirst || mRepository.isDirty()) {
            isFirst = false;
            loadTasks();
        }
    }
}
