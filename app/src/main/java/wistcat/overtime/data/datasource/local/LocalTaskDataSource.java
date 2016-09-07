package wistcat.overtime.data.datasource.local;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import wistcat.overtime.App;
import wistcat.overtime.data.TaskEngine;
import wistcat.overtime.data.datasource.TaskDataSource;
import wistcat.overtime.data.datasource.TaskRepository;
import wistcat.overtime.data.db.TaskContract;
import wistcat.overtime.data.db.TaskTableHelper;
import wistcat.overtime.interfaces.GetDataListCallback;
import wistcat.overtime.model.Episode;
import wistcat.overtime.model.Record;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;
import wistcat.overtime.model.TaskState;
import wistcat.overtime.util.Const;
import wistcat.overtime.util.GetDataListCallbackAdapter;

/**
 * 本地数据源，用于对数据库的直接操作，以及部分缓存处理
 *
 * @author wistcat 2016/9/1
 */
public class LocalTaskDataSource implements TaskDataSource {

    private final ContentResolver mContentResolver;
    private volatile List<TaskGroup> mCachedTaskGroup;
    private CountDownLatch mDown;

    public LocalTaskDataSource(@NonNull Context context) {
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void saveTaskGroup(@NonNull TaskGroup taskGroup) {
        mContentResolver.insert(
                TaskContract.buildTaskGroupUri(getAccount()),
                TaskEngine.taskGroupTo(taskGroup)
        );
    }

    @Override
    public void deleteTaskGroup(@NonNull TaskGroup taskGroup) {
        // 不需要在这一层实现，由{@link TaskRepository}完成转换
    }

    @Override
    public void deleteTaskGroup(int taskGroupId) {
        mContentResolver.delete(
                TaskContract.buildTaskGroupUriWith(getAccount(), taskGroupId),
                null,
                null
        );
    }

    @Override
    public void getCachedTaskGroup(@NonNull final GetDataListCallback<TaskGroup> callback) {
        // 不需要在这一层实现，由{@link TaskRepository}完成转换
    }

    @Override
    public void getCachedTaskGroup(@NonNull final GetDataListCallback<TaskGroup> callback, boolean forceRefresh) {
        // 本方法的调用场景在主线程中进行，不会是异步的
        if (Looper.myLooper() != Looper.getMainLooper()) {
            // 若对异步有要求，考虑使用AsyncTaskLoader之类的...
            Log.e("DataSource_TAG", "+++++ Not in the UI thread!!!! FIXME!!!! +++++++");
        }

        if (mCachedTaskGroup == null || forceRefresh) {
            TaskRepository.mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    if (mDown != null) {
                        try {
                            mDown.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mDown = null;
                    // 单独查询TaskGroup
                    Cursor c = mContentResolver.query(
                            TaskContract.buildTaskGroupUri(getAccount()),
                            TaskTableHelper.TASK_GROUP_PROJECTION,
                            null,
                            null,
                            null
                    );
                    List<TaskGroup> list = new ArrayList<>();
                    if (c != null) {
                        try {
                            if (c.moveToFirst()) {
                                while (c.moveToNext()) {
                                    list.add(TaskEngine.taskGroupFrom(c));
                                }
                            }
                        } finally {
                            c.close();
                        }
                    }
                    mCachedTaskGroup = list;
                    // 在主线程中更新
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onDataLoaded(mCachedTaskGroup);
                        }
                    });
                }
            });
        } else {
            callback.onDataLoaded(mCachedTaskGroup);
        }
    }

    @Override
    public void setTaskGroupCache(@NonNull List<TaskGroup> data) {
        mCachedTaskGroup = data;
    }

    @Override
    public void setTaskGroupCache(Cursor cursor) {
        List<TaskGroup> list = new ArrayList<>();
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    while (cursor.moveToNext()) {
                        list.add(TaskEngine.taskGroupFrom(cursor));
                    }
                }
            } finally {
                cursor.close();
            }
        }
        mCachedTaskGroup = list;
    }

    @Override
    public boolean isGroupCacheAvailable() {
        return mCachedTaskGroup == null;
    }

    @Override
    public void saveTask(@NonNull Task task) {
        mContentResolver.insert(
                TaskContract.buildTasksUriWith(getAccount()),
                TaskEngine.taskTo(task));
    }

    @Override
    public void startRunningTask(@NonNull Task task) {
        // 不需要在这一层实现，由{@link TaskRepository}完成转换
    }

    @Override
    public void startRunningTask(int taskId) {
        mContentResolver.update(
                TaskContract.buildTasksUriWith(getAccount(), taskId),
                TaskEngine.taskToState(taskId, TaskState.Running.name()),
                null,
                null
        );
    }

    @Override
    public void stopRunningTask(@NonNull Task task, String toState) {
        // 不需要在这一层实现，由{@link TaskRepository}完成转换
    }

    @Override
    public void stopRunningTask(int taskId, String toState) {
        mContentResolver.update(
                TaskContract.buildTasksUriWith(getAccount(), taskId),
                TaskEngine.taskToState(taskId, toState),
                null,
                null
        );
    }

    @Override
    public void completeTask(@NonNull Task task) {
        // 不需要在这一层实现，由{@link TaskRepository}完成转换
    }

    @Override
    public void completeTask(int taskId) {
        mContentResolver.update(
                TaskContract.buildTasksUriWith(getAccount(), taskId),
                TaskEngine.taskToState(taskId, TaskState.Completed.name()),
                null,
                null
        );
    }

    @Override
    public void activateTask(@NonNull Task task) {
        // 不需要在这一层实现，由{@link TaskRepository}完成转换
    }

    @Override
    public void activateTask(int taskId) {
        mContentResolver.update(
                TaskContract.buildTasksUriWith(getAccount(), taskId),
                TaskEngine.taskToState(taskId, TaskState.Activate.name()),
                null,
                null
        );
    }

    @Override
    public void deleteTask(@NonNull Task task) {
        // 不需要在这一层实现，由{@link TaskRepository}完成转换
    }

    @Override
    public void deleteTask(int taskId) {
        mContentResolver.delete(
                TaskContract.buildTasksUriWith(getAccount(), taskId),
                null,
                null
        );
    }

    @Override
    public void deleteAllTasks(int groupId) {
        mContentResolver.delete(
                TaskContract.buildTasksUriWith(getAccount()),
                TaskContract.TaskEntry.COLUMN_NAME_GROUP_ID + " = ?",
                new String[]{String.valueOf(groupId)}
        );
    }

    @Override
    public void saveRecord(@NonNull Record record) {
        mContentResolver.insert(
                TaskContract.buildRecordsUriWith(getAccount()),
                TaskEngine.recordTo(record)
        );
    }

    @Override
    public void deleteRecord(@NonNull Record record) {
        // 不需要在这一层实现，由{@link TaskRepository}完成转换
    }

    @Override
    public void deleteRecord(int recordId) {
        mContentResolver.delete(
                TaskContract.buildRecordsUriWith(getAccount(), recordId),
                null,
                null
        );
    }

    @Override
    public void deleteAllRecords(@NonNull Task task) {
        // 不需要在这一层实现，由{@link TaskRepository}完成转换
    }

    @Override
    public void deleteAllRecords(int taskId) {
        mContentResolver.delete(
                TaskContract.buildRecordsUriWith(getAccount()),
                TaskContract.RecordEntry.COLUMN_NAME_TASK_ID + " = ?",
                new String[]{String.valueOf(taskId)}
        );
    }

    @Override
    public void saveEpisode(@NonNull Episode episode) {
        mContentResolver.insert(
                TaskContract.buildEpisodesUriWith(getAccount()),
                TaskEngine.episodeTo(episode)
        );
    }

    @Override
    public void deleteEpisode(@NonNull Episode episode) {
        // 不需要在这一层实现，由{@link TaskRepository}完成转换
    }

    @Override
    public void deleteEpisode(int episodeId) {
        mContentResolver.delete(
                TaskContract.buildEpisodesUriWith(getAccount(), episodeId),
                null,
                null
        );
    }

    @Override
    public void initAccount(@NonNull final String account) {
        mDown = new CountDownLatch(1);
        TaskRepository.mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // 账户初次加载时，插入默认的TaskGroup
                SharedPreferences sp = App.getDefaultSharedPreferences();
                String acountFirst = String.format(Const.ACCOUNT_IS_FIRST, account);
                boolean isFirst = sp.getBoolean(acountFirst, false);
                if (!isFirst) {
                    Uri test = mContentResolver.insert(
                            TaskContract.buildTaskGroupUri(account),
                            TaskEngine.taskGroupToDefault(account)
                    );
                    Log.i("TAG", "insert uri : " + test);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean(acountFirst, true);
                    editor.apply();
                }
                // TODO other something...

                // over
                if (mDown != null) {
                    mDown.countDown();
                }
            }
        });
        getCachedTaskGroup(new GetDataListCallbackAdapter<TaskGroup>(), true);
    }

    @Override
    public void deleteAccount(@NonNull String account) {

    }

    @Override
    public void deleteTables() {
        // TODO
    }

    private String getAccount() {
        return App.getInstance().getAccountName();
    }
}
