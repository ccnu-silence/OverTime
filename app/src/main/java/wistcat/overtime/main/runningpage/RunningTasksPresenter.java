package wistcat.overtime.main.runningpage;

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
import wistcat.overtime.data.running.RunningManager;
import wistcat.overtime.interfaces.ResultCallback;
import wistcat.overtime.model.Record;

/**
 * @author wistcat 2016/9/25
 */
public class RunningTasksPresenter
        implements RunningTasksContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {
    private final int QUERY_RECORDS = 0x10;

    private final LoaderManager mLoaderManager;
    private final RunningManager mRunningManager;
    private final TaskRepository mRepository;
    private final RunningTasksContract.View mView;
    private boolean isFirst = true;

    @Inject
    public RunningTasksPresenter(LoaderManager loaderManager, RunningManager runningManager,
                                 TaskRepository repository, RunningTasksContract.View view) {
        mLoaderManager = loaderManager;
        mRunningManager = runningManager;
        mRepository = repository;
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
            return new CursorLoader(
                    App.getInstance().getApplicationContext(),
                    TaskContract.buildRecordsUriWith(getAccount()),
                    TaskTableHelper.RECORD_PROJECTION,
                    TaskTableHelper.WHERE_RECORD_RUN,
                    null,
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

    @Override
    public void onItemSelected(@NonNull Record record) {
        mView.redirectRunningRecord(record);
    }

    @Override
    public void onRecordPaused(@NonNull Record record) {
        mRepository.stopRunningTask(record);
        mRepository.endRecord(record, null, new ResultCallback() {
            @Override
            public void onSuccess() {
                mRunningManager.stopRunning();
            }

            @Override
            public void onError() {
                //
            }
        });
    }

    private String getAccount() {
        return App.getInstance().getAccountName();
    }
}
