package wistcat.overtime.main.taskslist;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.data.db.SelectionBuilder;
import wistcat.overtime.data.db.TaskContract;
import wistcat.overtime.data.db.TaskTableHelper;
import wistcat.overtime.model.Task;

/**
 * @author wistcat 2016/9/12
 */
public class TasksListPresenter implements TasksListContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {

    private final int TASKS_QUERY = 0x04;
    private final String SEARCH = "search";
    private final TasksListContract.View mView;
    private final LoaderManager mLoaderManager;
    private int mGroupId;
    private boolean isFirst = true;

    @Inject
    public TasksListPresenter(TasksListContract.View view, LoaderManager manager) {
        mView = view;
        mLoaderManager = manager;
    }

    @Inject
    public void setup() {
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mGroupId = mView.getGroupId();
        if (isFirst) {
            isFirst = false;
            loadTasks(null);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == TASKS_QUERY) {
            String str = args.getString(SEARCH);
            return createLoader(str);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == TASKS_QUERY) {
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
            mView.clearList();
        }
    }

    @Override
    public void loadTasks(String term) {
        Bundle data = new Bundle();
        data.putString(SEARCH, term);
        if (isFirst) {
            mLoaderManager.initLoader(TASKS_QUERY, data, this);
        } else {
            mLoaderManager.restartLoader(TASKS_QUERY, data, this);
        }
    }

    @Override
    public void onItemSelected(@NonNull Task task) {
        mView.showTaskDetails(task);
    }

    @Override
    public void openMoreMenu() {
        mView.showMoreMenu();
    }

    @Override
    public void openTaskGroupDetails() {
        mView.hideMoreMenu();
        mView.showTaskGroupDetails();
    }

    @Override
    public void openEditTasksList() {
        mView.hideMoreMenu();
        mView.showEditTasksList();
    }

    @Override
    public void scrollTop() {
        mView.showScrollUp();
    }

    private CursorLoader createLoader(String str) {
        Uri uri = TaskContract.buildTasksUriWith(getAccount());
        SelectionBuilder builder = new SelectionBuilder();
        builder.whereAnd(
                TaskContract.TaskEntry.COLUMN_NAME_GROUP_ID + " = ?",
                String.valueOf(mGroupId)
        );
        if (!TextUtils.isEmpty(str)) {
            builder.whereAnd(
                    TaskContract.TaskEntry.COLUMN_NAME_TASK_NAME + " LIKE ?",
                    TaskTableHelper.createSearchSelection(str)
            );
        }
        return new CursorLoader(
                App.getInstance(),
                uri,
                TaskTableHelper.TASK_PROJECTION,
                builder.getSelection(),
                builder.getSelectionArgs(),
                null
        );
    }

    private String getAccount() {
        return App.getInstance().getAccountName();
    }

}
