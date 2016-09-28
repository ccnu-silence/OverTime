package wistcat.overtime.main.runningpage;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.data.CursorProvider;
import wistcat.overtime.data.TaskEngine;
import wistcat.overtime.data.datasource.TaskRepository;
import wistcat.overtime.data.db.SelectionBuilder;
import wistcat.overtime.data.db.TaskTableHelper;
import wistcat.overtime.interfaces.GetDataListCallback;
import wistcat.overtime.interfaces.ResultCallback;
import wistcat.overtime.model.Episode;
import wistcat.overtime.model.Record;
import wistcat.overtime.model.Task;
import wistcat.overtime.util.NotificationHelper;
import wistcat.overtime.util.Utils;

/**
 * @author wistcat 2016/9/25
 */
public class RunningTasksPresenter
        implements RunningTasksContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {
    private final int QUERY_RECORDS = 0x10;
    private final int QUERY_EPISODES = 0x15;

    private final LoaderManager mLoaderManager;
    private final TaskRepository mRepository;
    private final RunningTasksContract.View mView;
    private final RunningTasksContract.RunningView mMainView;

    private boolean isInit;
    private boolean isFirst = true;
    private Record mSelectedRecord;
    private Task mInitTask;
    private String mEpisodeTime;

    @Inject
    public RunningTasksPresenter(LoaderManager loaderManager, TaskRepository repository,
                                 RunningTasksContract.RunningView mainView, RunningTasksContract.View view) {
        mLoaderManager = loaderManager;
        mRepository = repository;
        mMainView = mainView;
        mView = view;
    }

    @Inject
    public void setup() {
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        isInit = true;
        // 如果进入页面时，没有指定Task，则加载运行列表的第一个Record
        if (mInitTask == null) {
            String selection = String.format(Locale.getDefault(), TaskTableHelper.WHERE_RECORD_RUN_LIMITED, 1, 0);
            mRepository.queryRecords(selection, null, null, mRecordCallback);
        } else {
            // 否则，加载指定的Record
            SelectionBuilder builder = new SelectionBuilder();
            builder.whereAnd(TaskTableHelper.WHERE_RECORD_TAKS_ID, String.valueOf(mInitTask.getId()))
                    .whereAnd(TaskTableHelper.WHERE_RECORD_RUN);
            mRepository.queryRecords(
                    builder.getSelection(),
                    builder.getSelectionArgs(),
                    null,
                    mRecordCallback
            );
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case QUERY_RECORDS: // 用于Navigation列表
                return CursorProvider.queryRunningRecords();
            case QUERY_EPISODES:
                if (mSelectedRecord != null) {
                    return CursorProvider.queryEpisodeList(mSelectedRecord.getId());
                }
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case QUERY_RECORDS:
                // 在Activity初始化时初次调用
                int count;
                Record temp;
                if (Utils.isCursorEmpty(data)) {
                    count = 0;
                    temp = null;
                } else {
                    count = data.getCount();
                    data.moveToFirst();
                    temp = TaskEngine.recordFrom(data);
                    data.moveToFirst();
                }
                if (!isInit) {
                    mSelectedRecord = temp;
                    loadRecord(mSelectedRecord);
                }
                isInit = false;
                // 侧拉菜单加载
                mMainView.showList(data);
                // 更新通知栏
                NotificationHelper.notifyNormal(App.getInstance().getApplicationContext(), count);
                break;
            case QUERY_EPISODES:
                // 加载Episode列表，①在初始化之后，②在Navigation选项选择之后
                if (Utils.isCursorEmpty(data)) {
                    mView.setStartTimeImage(true);
                } else {
                    mView.setStartTimeImage(false);
                }
                mView.showList(data);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case QUERY_RECORDS:
                mMainView.clearList();
                break;
            case QUERY_EPISODES:
                mView.clearList();
                break;
            default:
                break;
        }
    }

    @Override
    public void initState(Task task) {
        mInitTask = task;
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
        mSelectedRecord = record;
        mMainView.hideDrawer();
        loadRecord(record);
    }

    @Override
    public void onRecordPaused(@NonNull Record record) {
        mRepository.stopRunningTask(record);
        mRepository.endRecord(record, null, mPauseCallback);
    }

    @Override
    public void openDrawer() {
        mMainView.showDrawer();
    }

    @Override
    public void closeDrawer() {
        mMainView.hideDrawer();
    }

    @Override
    public void stopAll() {
        // TODO
    }

    @Override
    public void openAddDialog() {
        if (mSelectedRecord != null) {
            mEpisodeTime =  Utils.getDate(Utils.FORMAT_DATE_TEMPLATE_TIME);
            mView.showAddEpisodeDialog(mEpisodeTime);
        }
    }

    @Override
    public void doAddEpisode(String remark) {
        if (mEpisodeTime != null) {
            Episode episode = new Episode(0, TaskEngine.createId(), mSelectedRecord.getId(),
                    Episode.TYPE_NOTE, "插曲", remark, mEpisodeTime, 0);
            mRepository.saveEpisode(episode, mAddEpisodeCallback);
        }
    }

    @Override
    public void doNotAdd() {
        mEpisodeTime = null;
        mView.dismissAddEpisodeDialog();
    }

    @Override
    public void doPause() {
        onRecordPaused(mSelectedRecord);
    }

    /* 初始化入口 */
    @Override
    public void loadRecordByTask() {
        if (isFirst) {
            isFirst = false;
            mLoaderManager.initLoader(QUERY_RECORDS, null, this);
        } else {
            mLoaderManager.restartLoader(QUERY_RECORDS, null, this);
        }
    }

    @Override
    public void loadRecord(Record record) {
        if (record == null) {
            mView.loadRecordEmpty();
        } else {
            mView.loadRecordDetails(record);
            mLoaderManager.restartLoader(QUERY_EPISODES, null, RunningTasksPresenter.this);
        }
    }

    private GetDataListCallback<Record> mRecordCallback = new GetDataListCallback<Record>() {
        @Override
        public void onDataLoaded(List<Record> dataList) {
            if (dataList.size() > 0) {
                mSelectedRecord = dataList.get(0);
                loadRecord(mSelectedRecord);
            } else {
                loadRecord(null);
            }
            mInitTask = null;
        }

        @Override
        public void onError() {

        }
    };

    private ResultCallback mAddEpisodeCallback = new ResultCallback() {
        @Override
        public void onSuccess() {
            mView.dismissAddEpisodeDialog();
            //
        }

        @Override
        public void onError() {
            //
        }
    };

    private ResultCallback mPauseCallback = new ResultCallback() {
        @Override
        public void onSuccess() {
            //
        }

        @Override
        public void onError() {
            //
        }
    };
}
