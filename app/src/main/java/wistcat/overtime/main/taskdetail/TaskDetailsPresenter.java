package wistcat.overtime.main.taskdetail;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.data.db.TaskContract;
import wistcat.overtime.data.db.TaskTableHelper;
import wistcat.overtime.model.Task;

/**
 * @author wistcat 2016/9/22
 */
public class TaskDetailsPresenter implements TaskDetailsContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {

    private final int QUERY_RECORDS = 0x11;
    private final Task mTask;
    private final LoaderManager mLoaderManager;
    private final TaskDetailsContract.View mView;
    private boolean isFirst = true;

    @Inject
    public TaskDetailsPresenter(Task task, LoaderManager manager, TaskDetailsContract.View view) {
        mTask = task;
        mLoaderManager = manager;
        mView = view;
    }

    @Inject
    public void setup() {
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if (isFirst) {
            loadList();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == QUERY_RECORDS) {
            Uri uri = TaskContract.buildRecordsUriWith(getAccount());
            Log.i("TAG", uri.toString());
            return new CursorLoader(
                    App.getInstance().getApplicationContext(),
                    uri,
                    TaskTableHelper.RECORD_PROJECTION,
                    TaskContract.RecordEntry.COLUMN_NAME_TASK_ID + " = ?",
                    new String[]{String.valueOf(mTask.getId())},
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == QUERY_RECORDS) {
            mView.showList(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == QUERY_RECORDS) {
            mView.clearList();
        }
    }

    @Override
    public void loadList() {
        if (isFirst) {
            isFirst = false;
            mLoaderManager.initLoader(QUERY_RECORDS, null, this);
        } else {
            mLoaderManager.restartLoader(QUERY_RECORDS, null, this);
        }
    }

    private String getAccount() {
        return App.getInstance().getAccountName();
    }
}
