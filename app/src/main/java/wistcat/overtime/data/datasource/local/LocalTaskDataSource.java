package wistcat.overtime.data.datasource.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import wistcat.overtime.App;
import wistcat.overtime.data.TaskEngine;
import wistcat.overtime.data.datasource.TaskDataSource;
import wistcat.overtime.data.datasource.TaskRepository;
import wistcat.overtime.data.db.SelectionBuilder;
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

import static wistcat.overtime.data.db.TaskContract.TaskGroupEntry;

/**
 * 本地数据源，用于对数据库的直接操作，以及部分缓存处理
 *
 * @author wistcat 2016/9/1
 */
public class LocalTaskDataSource implements TaskDataSource {

    private final ContentResolver mContentResolver;
    /* 缓存任务组，这里的TaskGroup只使用id和name，不同步task数据 */
    private volatile List<TaskGroup> mCachedTaskGroup;
    private CountDownLatch mDown;

    public LocalTaskDataSource(@NonNull Context context) {
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void saveTaskGroup(@NonNull TaskGroup taskGroup) {
        // 插入TaskGroup表
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
        // 转移Task
        mContentResolver.update(
                TaskContract.buildTasksUriWith(getAccount()),
                TaskEngine.taskToRecycled(),
                TaskContract.TaskEntry.COLUMN_NAME_GROUP_ID + " = ?",
                new String[]{String.valueOf(taskGroupId)}
        );
        // 从TaskGroup表删除
        mContentResolver.delete(
                TaskContract.buildTaskGroupUriWith(getAccount(), taskGroupId),
                null,
                null
        );
    }

    @Override
    public void deleteTaskGroups(@NonNull List<Integer> taskGroupIds) {
        // FIXME: 如果性能有问题，改进或者不这么做...
        for (int i : taskGroupIds) {
            // 转移Task
            mContentResolver.update(
                    TaskContract.buildTasksUriWith(getAccount()),
                    TaskEngine.taskToRecycled(),
                    TaskContract.TaskEntry.COLUMN_NAME_GROUP_ID + " = ?",
                    new String[]{String.valueOf(i)}
            );
        }
        // 从TaskGroup表删除
        SelectionBuilder builder = new SelectionBuilder();
        builder.in(TaskGroupEntry._ID, null);
        for (int i : taskGroupIds) {
            builder.in(null, String.valueOf(i));
        }
        mContentResolver.delete(
                TaskContract.buildTaskGroupUri(getAccount()),
                builder.getSelection(),
                builder.getSelectionArgs()
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
            Observable
                    .create(mObservable)
                    .subscribeOn(Schedulers.io())
                    .toList()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<TaskGroup>>() {
                        @Override
                        public void call(List<TaskGroup> list) {
                            mCachedTaskGroup = list;
                            callback.onDataLoaded(mCachedTaskGroup);
                        }
                    });
        } else {
            callback.onDataLoaded(mCachedTaskGroup);
        }
    }

    /* 注意不能在主线程中执行 */
    private Observable.OnSubscribe<TaskGroup> mObservable = new Observable.OnSubscribe<TaskGroup>() {
        @Override
        public void call(Subscriber<? super TaskGroup> subscriber) {
            // 只在Account第一次创建时有效
            if (mDown != null) {
                try {
                    mDown.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mDown = null;
            // 不包含 Completed和Recycled分组
            SelectionBuilder builder = new SelectionBuilder();
            builder.notIn(TaskGroupEntry._ID, null)
                    .notIn(null, String.valueOf(Const.COMPLETED_GROUP_ID))
                    .notIn(null, String.valueOf(Const.RECYCLED_GROUP_ID));
            // 单独查询TaskGroup
            Cursor c = mContentResolver.query(
                    TaskContract.buildTaskGroupUri(getAccount()),
                    TaskTableHelper.TASK_GROUP_PROJECTION,
                    builder.getSelection(),
                    builder.getSelectionArgs(),
                    null
            );
            // 遍历结果 Cursor 转化为 TaskGroup 发送
            if (c != null) {
                try {
                    if (c.moveToFirst()) {
                        do {
                            TaskEngine.taskGroupFrom(c);
                            // 发送结果
                            subscriber.onNext(TaskEngine.taskGroupFrom(c));
                        } while (c.moveToNext());
                    }
                } finally {
                    c.close();
                }
            }
            // toList()所需要
            subscriber.onCompleted();
        }
    };

    @Override
    public void setTaskGroupCache(@NonNull List<TaskGroup> data) {
        mCachedTaskGroup = data;
    }

    @Override
    public void setTaskGroupCache(Cursor cursor) {
        List<TaskGroup> list = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    list.add(TaskEngine.taskGroupFrom(cursor));
                }
            }
            cursor.moveToFirst();
        }
        mCachedTaskGroup = list;
    }

    @Override
    public boolean isGroupCacheAvailable() {
        return mCachedTaskGroup == null;
    }

    @Override
    public void saveTask(@NonNull Task task) {
        // 插入或更新 Task 表
        try {
            mContentResolver.insert(
                    TaskContract.buildTasksUriWith(getAccount()),
                    TaskEngine.taskTo(task));
        } catch (Exception e) {
            e.printStackTrace();
            App.showToast("保存任务失败！请重试！");
        }
        // 刷新TaskGroup表数据
        updateTaskGroup(true, TaskGroupEntry.COLUMN_NAME_COUNT, 1, task.getGroupId());
        // 通知Loader框架刷新
        notifyUri(getTaskGroupUri());
    }

    @Override
    public void startRunningTask(@NonNull Task task) {
        // 不需要在这一层实现，由{@link TaskRepository}完成转换
    }

    @Override
    public void startRunningTask(int taskId) {
        // 更新Task表
        mContentResolver.update(
                TaskContract.buildTasksUriWith(getAccount(), taskId),
                TaskEngine.taskToRunning(),
                null,
                null
        );
        // TODO something..
    }

    @Override
    public void stopRunningTask(@NonNull Task task, TaskState state) {
        // Task 的状态可从 Running 转换为 Activate或Completed
        ContentValues values;
        switch (state) {
            case Activate:
                TaskGroup mock = TaskEngine.mockTaskGroup(task);
                values = TaskEngine.taskToActivate(mock);
                break;
            case Completed:
                values = TaskEngine.taskToCompleted();
                break;
            default:
                throw new IllegalArgumentException("unsupported state : " + state);
        }
        mContentResolver.update(
                TaskContract.buildTasksUriWith(getAccount(), task.getId()),
                values,
                null,
                null
        );
        // 完成Task条目更新后，更新TaskGroup条目
        if (state == TaskState.Completed) {
            updateTaskGroup(false, TaskGroupEntry.COLUMN_NAME_COUNT, 1, task.getGroupId());
            updateTaskGroup(true, TaskGroupEntry.COLUMN_NAME_COUNT, 1, Const.COMPLETED_GROUP_ID);
            notifyUri(getTaskGroupUri());
        }
    }

    @Override
    public void completeTask(@NonNull Task task) {
        mContentResolver.update(
                TaskContract.buildTasksUriWith(getAccount(), task.getId()),
                TaskEngine.taskToCompleted(),
                null,
                null
        );
        updateTaskGroup(false, TaskGroupEntry.COLUMN_NAME_COUNT, 1, task.getGroupId());
        updateTaskGroup(true, TaskGroupEntry.COLUMN_NAME_COUNT, 1, Const.COMPLETED_GROUP_ID);
        notifyUri(getTaskGroupUri());
    }

    @Override
    public void completeTasks(@NonNull List<Integer> taskIds, int groupId) {
        /* groupId为当前所在的TaskGroup的ID */
        SelectionBuilder builder  = new SelectionBuilder();
        builder.in(TaskContract.TaskEntry._ID, null);
        for (int i : taskIds) {
            builder.in(null, String.valueOf(i));
        }
        mContentResolver.update(
                TaskContract.buildTasksUriWith(getAccount()),
                TaskEngine.taskToCompleted(),
                builder.getSelection(),
                builder.getSelectionArgs()
        );
        updateTaskGroup(false, TaskGroupEntry.COLUMN_NAME_COUNT, taskIds.size(), groupId);
        updateTaskGroup(true, TaskGroupEntry.COLUMN_NAME_COUNT, taskIds.size(), Const.COMPLETED_GROUP_ID);
        notifyUri(getTaskGroupUri());
    }

    @Override
    public void activateTask(@NonNull Task task, @NonNull TaskGroup group) {
        /* group 为想要转入的目的TaskGroup   */
        // 将任务从Completed或Recycled分组移出时，需要重新设置Task关联的groupName和groupId
        mContentResolver.update(
                TaskContract.buildTasksUriWith(getAccount(), task.getId()),
                TaskEngine.taskToActivate(group),
                null,
                null
        );
        // 目的TaskGroup计数加一
        updateTaskGroup(true, TaskGroupEntry.COLUMN_NAME_COUNT, 1, group.getId());
        // 旧的TaskGroup计数减一
        switch (task.getState()) {
            case Recycled:
                updateTaskGroup(false, TaskGroupEntry.COLUMN_NAME_COUNT, 1, Const.RECYCLED_GROUP_ID);
                break;
            case Completed:
                updateTaskGroup(false, TaskGroupEntry.COLUMN_NAME_COUNT, 1, Const.COMPLETED_GROUP_ID);
                break;
            default:
                throw new IllegalArgumentException("Error task state: " + task.getState());
        }
        // 通知刷新
        notifyUri(getTaskGroupUri());
    }

    @Override
    public void activateTasks(@NonNull List<Integer> taskIds, @NonNull TaskGroup group) {
        /* group 为想要转入的目的TaskGroup */
        SelectionBuilder builder  = new SelectionBuilder();
        builder.in(TaskContract.TaskEntry._ID, null);
        for (int i : taskIds) {
            builder.in(null, String.valueOf(i));
        }
        mContentResolver.update(
                TaskContract.buildTasksUriWith(getAccount()),
                TaskEngine.taskToActivate(group),
                builder.getSelection(),
                builder.getSelectionArgs()
        );
        // 目的TaskGroup计数增加
        updateTaskGroup(true, TaskGroupEntry.COLUMN_NAME_COUNT, taskIds.size(), group.getId());
        // 旧的TaskGroup计数减少
        switch (group.getName()) {
            case Const.DEFAULT_RECYCLED_GROUP:
                updateTaskGroup(false, TaskGroupEntry.COLUMN_NAME_COUNT, taskIds.size(), Const.RECYCLED_GROUP_ID);
                break;
            case Const.DEFAULT_COMPLETED_GROUP:
                updateTaskGroup(false, TaskGroupEntry.COLUMN_NAME_COUNT, taskIds.size(), Const.COMPLETED_GROUP_ID);
                break;
            default:
                throw new IllegalArgumentException("Error taskGroup: " + group.getName());
        }
        // 通知刷新
        notifyUri(getTaskGroupUri());
    }

    @Override
    public void recycleTask(@NonNull Task task) {
        mContentResolver.update(
                TaskContract.buildTasksUriWith(getAccount(), task.getId()),
                TaskEngine.taskToRecycled(),
                null,
                null
        );
        updateTaskGroup(false, TaskGroupEntry.COLUMN_NAME_COUNT, 1, task.getGroupId());
        updateTaskGroup(true, TaskGroupEntry.COLUMN_NAME_COUNT, 1, Const.RECYCLED_GROUP_ID);
        notifyUri(getTaskGroupUri());
    }

    @Override
    public void recycleTasks(@NonNull List<Integer> taskIds, int groupId) {
        /* groupId为当前所在的TaskGroup的ID */
        SelectionBuilder builder  = new SelectionBuilder();
        builder.in(TaskContract.TaskEntry._ID, null);
        for (int i : taskIds) {
            builder.in(null, String.valueOf(i));
        }
        mContentResolver.update(
                TaskContract.buildTasksUriWith(getAccount()),
                TaskEngine.taskToRecycled(),
                builder.getSelection(),
                builder.getSelectionArgs()
        );
        updateTaskGroup(false, TaskGroupEntry.COLUMN_NAME_COUNT, taskIds.size(), groupId);
        updateTaskGroup(true, TaskGroupEntry.COLUMN_NAME_COUNT, taskIds.size(), Const.RECYCLED_GROUP_ID);
        notifyUri(getTaskGroupUri());
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
        updateTaskGroup(false, TaskGroupEntry.COLUMN_NAME_COUNT, 1, Const.RECYCLED_GROUP_ID);
        notifyUri(getTaskGroupUri());
    }

    @Override
    public void deleteTasks(@NonNull List<Integer> taskIds, int groupId) {
        /* groupId为当前所在的TaskGroup的ID */
        SelectionBuilder builder = new SelectionBuilder();
        builder.in(TaskContract.TaskEntry._ID, null);
        for (int i : taskIds) {
            builder.in(null, String.valueOf(i));
        }
        mContentResolver.delete(
                TaskContract.buildTasksUriWith(getAccount()),
                builder.getSelection(),
                builder.getSelectionArgs()
        );
        updateTaskGroup(false, TaskGroupEntry.COLUMN_NAME_COUNT, taskIds.size(), Const.RECYCLED_GROUP_ID);
        notifyUri(getTaskGroupUri());
    }


    @Override
    public void initAndCheckRecords(@NonNull GetDataListCallback<Record> callback) {
        // TODO 检查未能正常结束的记录
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
    public void deleteRecords(@NonNull List<Integer> recordIds) {
        SelectionBuilder builder = new SelectionBuilder();
        builder.in(TaskContract.RecordEntry._ID, null);
        for (int i : recordIds) {
            builder.in(null, String.valueOf(i));
        }
        mContentResolver.delete(
                TaskContract.buildRecordsUriWith(getAccount()),
                builder.getSelection(),
                builder.getSelectionArgs()
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
    public void deleteEpisodes(@NonNull List<Integer> episodeIds) {
        SelectionBuilder builder = new SelectionBuilder();
        builder.in(TaskContract.EpisodeEntry._ID, null);
        for (int i : episodeIds) {
            builder.in(null, String.valueOf(i));
        }
        mContentResolver.delete(
                TaskContract.buildEpisodesUriWith(getAccount()),
                builder.getSelection(),
                builder.getSelectionArgs()
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
                    Uri groupUri = TaskContract.buildTaskGroupUri(account);
                    // 生成默认 Default
                    mContentResolver.insert(groupUri, TaskEngine.taskGroupToDefault(
                            account, Const.DEFAULT_GROUP, Const.DEFAULT_GROUP_ID));
                    // 生成默认 Completed
                    mContentResolver.insert(groupUri, TaskEngine.taskGroupToDefault(
                            account, Const.DEFAULT_COMPLETED_GROUP, Const.COMPLETED_GROUP_ID));
                    // 生成默认 Recycled
                    mContentResolver.insert(groupUri, TaskEngine.taskGroupToDefault(
                            account, Const.DEFAULT_RECYCLED_GROUP, Const.RECYCLED_GROUP_ID));
                    // 保持至SharedPreferences
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

    private void updateTaskGroup(boolean increase, String column, int delta, int groupId) {
        String table = TaskGroupEntry.getTableName(getAccount());
        String template = increase ? TaskTableHelper.SQL_AUTO_INCREASE : TaskTableHelper.SQL_AUTO_DECREASE;
        String sentence = String.format(Locale.getDefault(), template, table, column, column, delta, groupId);
        mContentResolver.update(
                TaskContract.buildAutoTaskGroupUriWith(getAccount(), groupId),
                null,
                sentence,
                null
        );
    }

    private void notifyUri(Uri uri) {
        mContentResolver.notifyChange(uri, null, false);
    }

    private Uri getTaskGroupUri() {
        return TaskContract.buildTaskGroupUri(getAccount());
    }

}
