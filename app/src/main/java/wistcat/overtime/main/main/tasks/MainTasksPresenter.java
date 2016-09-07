package wistcat.overtime.main.main.tasks;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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
import wistcat.overtime.model.Task;
import wistcat.overtime.util.Const;

/**
 * @author wistcat 2016/9/2
 */
public class MainTasksPresenter implements MainTasksContract.Presenter, LoaderManager.LoaderCallbacks<Cursor>{

    private static final int TASKS_QUERY = 0x01;
    private final MainTasksContract.View mView;
    private final LoaderManager mLoaderManager;
    private final TaskRepository mRepository;
    private boolean isFirst = true;
    private Handler mHandler = new Handler();
    private long mLastRefreshTime;

    @Inject
    public MainTasksPresenter(MainTasksContract.View view, LoaderManager loaderManager, TaskRepository repository) {
        mView = view;
        mLoaderManager = loaderManager;
        mRepository = repository;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == TASKS_QUERY) {
            mLastRefreshTime = SystemClock.uptimeMillis();
            mView.setLoadingIndicator(true);
            // 创建搜索语句
            SelectionBuilder builder = new SelectionBuilder();
            builder.whereOr(TaskTableHelper.WHERE_TASK_STATE, TaskTableHelper.WHERE_TASK_STATE_ACTIVATE)
                    .whereOr(TaskTableHelper.WHERE_TASK_STATE, TaskTableHelper.WHERE_TASK_STATE_RUNNING);
            return new CursorLoader(
                    App.getInstance(),
                    TaskContract.buildTasksUriWith(App.getInstance().getAccountName()),
                    TaskTableHelper.TASK_PROJECTION,
                    builder.getSelection(),
                    builder.getSelectionArgs(),
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final  Cursor data) {
        if (loader.getId() == TASKS_QUERY) {
            long now = SystemClock.uptimeMillis();
            // 每次刷新都至少要显示一段时间，比如500ms
            if (now < mLastRefreshTime + Const.DEFAULT_REFRESH_DURATION) {
                mHandler.postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        doLoadFinished(data);
                    }
                }, mLastRefreshTime + Const.DEFAULT_REFRESH_DURATION);
                return;
            }
            doLoadFinished(data);
        }
    }

    private void doLoadFinished(Cursor data) {
        mRepository.clearDirty();
        mView.setLoadingIndicator(false);
        if (data == null || !data.moveToLast()) {
            mView.showNoTasks();
        } else {
            mView.showTasks(data);
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
        if (isFirst) {
            isFirst = false;
            loadTasks();
            mRepository.initAccount(App.getInstance().getAccountName());
        } else if (mRepository.isDirty()) {
            loadTasks();
        }
    }
}
