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
import wistcat.overtime.data.TaskEngine;
import wistcat.overtime.data.datasource.TaskRepository;
import wistcat.overtime.data.db.SelectionBuilder;
import wistcat.overtime.data.db.TaskContract;
import wistcat.overtime.data.db.TaskTableHelper;
import wistcat.overtime.data.running.RunningManager;
import wistcat.overtime.interfaces.ResultCallback;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;
import wistcat.overtime.util.Const;

/**
 * @author wistcat 2016/9/2
 */
public class MainTasksPresenter implements MainTasksContract.Presenter, LoaderManager.LoaderCallbacks<Cursor>{

    private static final int TASKS_QUERY = 0x01;
    private final MainTasksContract.View mView;
    private final LoaderManager mLoaderManager;
    private final RunningManager mRunningManager;
    private final TaskRepository mRepository;

    private boolean isFirst = true;
    private Handler mHandler = new Handler();
    private Task mSelectedTask;

    @Inject
    public MainTasksPresenter(MainTasksContract.View view, LoaderManager loaderManager,
                              RunningManager runningManager, TaskRepository repository) {
        mView = view;
        mLoaderManager = loaderManager;
        mRunningManager = runningManager;
        mRepository = repository;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == TASKS_QUERY) {
            // 创建搜索语句
            SelectionBuilder builder = new SelectionBuilder();
            builder.whereOr(TaskTableHelper.WHERE_TASK_STATE, TaskTableHelper.WHERE_TASK_STATE_ACTIVATE);
            // FIXME
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
            mRepository.clearDirty();
            mView.setLoadingIndicator(false);
            mView.showList(data);
            if (data == null || data.getCount() == 0) {
                mView.showNoText(true);
            } else {
                mView.showNoText(false);
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
        mView.setLoadingIndicator(true);
        long now = SystemClock.uptimeMillis();
        // 每次刷新都至少要显示一段时间，比如500ms
        mHandler.postAtTime(new Runnable() {
            @Override
            public void run() {
                if (mLoaderManager.getLoader(TASKS_QUERY) == null) {
                    mLoaderManager.initLoader(TASKS_QUERY, null, MainTasksPresenter.this);
                } else {
                    mLoaderManager.restartLoader(TASKS_QUERY, null, MainTasksPresenter.this);
                }
            }
        }, now + Const.DEFAULT_REFRESH_DURATION);
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
    public void openTaskMenu(@NonNull Task task) {
        mSelectedTask = task;
        if (task.isRunning()) {
            mView.showRunningTaskMenu(task.getName(), task.getGroupName());
        } else {
            mView.showTaskMenu(task.getName(), task.getGroupName());
        }
    }

    @Override
    public void openDeleteDialog() {
        mView.dismissTaskMenu();
        mView.showDeleteDialog(mSelectedTask);
    }

    @Override
    public void closeDeleteDialog() {
        mView.dismissDeleteDialog();
    }

    @Override
    public void openGroup() {
        mView.dismissTaskMenu();
        TaskGroup group = TaskEngine.mockTaskGroup(mSelectedTask);
        mView.redirectGroup(group);
    }

    @Override
    public void doDelete() {
        mView.dismissDeleteDialog();
        mView.setLoadingIndicator(true);
        mRepository.recycleTask(mSelectedTask, new ResultCallback() {
            @Override
            public void onSuccess() {
                mView.setLoadingIndicator(false);
                mView.showToast("删除成功");
            }

            @Override
            public void onError() {
                mView.showToast("删除失败");
            }
        });
    }

    @Override
    public void doStart() {
        mView.dismissTaskMenu();
        mRepository.startRunningTask(mSelectedTask);
        mRepository.beginRecord(mSelectedTask);
        mRunningManager.startRunning();
        mView.redirectRunningPage(mSelectedTask);
    }

    @Override
    public void openRunningPage() {
        mView.dismissTaskMenu();
        mView.redirectRunningPage();
    }

    @Override
    public void checkRunning() {
        if (mRunningManager.getRunningTaskCount() > 0) {
            mView.showRunningBottom();
        } else {
            mView.hideRunningBottom();
        }
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
