package wistcat.overtime.main.taskdetail;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import javax.inject.Inject;

import wistcat.overtime.model.Task;

/**
 * @author wistcat 2016/9/22
 */
public class TaskDetailsPresenter implements TaskDetailsContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {

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
        // TODO
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // TODO
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // TODO
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // TODO
    }
}
