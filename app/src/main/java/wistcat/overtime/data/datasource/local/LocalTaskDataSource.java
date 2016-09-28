package wistcat.overtime.data.datasource.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import wistcat.overtime.App;
import wistcat.overtime.data.TaskEngine;
import wistcat.overtime.data.datasource.TaskDataSource;
import wistcat.overtime.data.datasource.TaskRepository;
import wistcat.overtime.data.db.SelectionBuilder;
import wistcat.overtime.data.db.TaskContract;
import wistcat.overtime.data.db.TaskTableHelper;
import wistcat.overtime.interfaces.GetDataCallback;
import wistcat.overtime.interfaces.GetDataListCallback;
import wistcat.overtime.interfaces.ResultCallback;
import wistcat.overtime.model.Episode;
import wistcat.overtime.model.Record;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;
import wistcat.overtime.util.Const;

import static wistcat.overtime.data.db.TaskContract.TaskGroupEntry;

/**
 * 本地数据源，用于对数据库的直接操作，以及部分缓存处理
 *
 * @author wistcat 2016/9/1
 */
public class LocalTaskDataSource implements TaskDataSource {

    private final ContentResolver mContentResolver;
    /* 用于等待初始化默认任务组 */
    private CountDownLatch mDown;
    /* UI线程Handler，用于调用回调 */
    private Handler mHandler = new Handler(Looper.getMainLooper());

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
    public void saveTaskGroup(@NonNull TaskGroup group, ResultCallback callback) {
        try {
            saveTaskGroup(group);
            sendSuccess(callback);
        } catch (Exception e) {
            // FIXME: 需要在操作数据库时，抛出更有效的异常。关于相关的异常以后再写...
            sendError(callback);
        }
    }

    @Override
    public void deleteTaskGroup(@NonNull TaskGroup taskGroup) {
        deleteTaskGroup(taskGroup.getId());
    }

    @Override
    public void deleteTaskGroup(int taskGroupId) {
        // 转移Task到Recycled分组
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
    public void deleteTaskGroup(@NonNull TaskGroup taskGroup, ResultCallback callback) {
        try {
            deleteTaskGroup(taskGroup);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
        }
    }

    @Override
    public void deleteTaskGroups(@NonNull List<Integer> taskGroupIds) {
        // FIXME: 如果性能有问题，改进或者删除...
        for (int i : taskGroupIds) {
            // 转移Task到Recycled分组
            mContentResolver.update(
                    TaskContract.buildTasksUriWith(getAccount()),
                    TaskEngine.taskToRecycled(),
                    TaskContract.TaskEntry.COLUMN_NAME_GROUP_ID + " = ?",
                    new String[]{String.valueOf(i)}
            );
        }
        // 创建删除语句
        SelectionBuilder builder = new SelectionBuilder();
        builder.in(TaskGroupEntry._ID, null);
        for (int i : taskGroupIds) {
            builder.in(null, String.valueOf(i));
        }
        // 从TaskGroup表删除
        mContentResolver.delete(
                TaskContract.buildTaskGroupUri(getAccount()),
                builder.getSelection(),
                builder.getSelectionArgs()
        );
    }

    @Override
    public void deleteTaskGroups(@NonNull List<Integer> taskGroupIds, ResultCallback callback) {
        try {
            deleteTaskGroups(taskGroupIds);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
        }
    }

    @Override
    public void getTaskGroups(@NonNull final GetDataListCallback<TaskGroup> callback) {
        // 本方法的调用场景在主线程中进行，不会是异步的
        if (Looper.myLooper() != Looper.getMainLooper()) {
            // 若对异步有要求，考虑使用AsyncTaskLoader之类的...
            Log.e("DataSource_TAG", "+++++ Not in the UI thread!!!! FIXME!!!! +++++++");
        }
        // 异步执行
        TaskRepository.mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // 只在Account第一次创建时有效，等待初始默认条目插入完成
                if (mDown != null) {
                    try {
                        mDown.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mDown = null;
                // 获取不包含Completed和Recycled的全部分组
                SelectionBuilder builder = new SelectionBuilder();
                builder.notIn(TaskGroupEntry._ID, null)
                        .notIn(null, String.valueOf(Const.COMPLETED_GROUP_ID))
                        .notIn(null, String.valueOf(Const.RECYCLED_GROUP_ID));
                final List<TaskGroup> data = new ArrayList<>();
                // 完成任务组搜索
                final Cursor c = mContentResolver.query(
                        TaskContract.buildTaskGroupUri(getAccount()),
                        TaskTableHelper.TASK_GROUP_PROJECTION,
                        builder.getSelection(),
                        builder.getSelectionArgs(),
                        null
                );
                // 将Cursor转化为List
                if (c != null) {
                    try {
                        if (c.moveToFirst()) {
                            do {
                                TaskGroup group = TaskEngine.taskGroupFrom(c);
                                data.add(group);
                            } while (c.moveToNext());
                        }
                    } finally {
                        c.close();
                    }
                }
                // 主线程中更新
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onDataLoaded(data);
                    }
                });
            }
        });
    }

    @Override
    public void saveTask(@NonNull Task task) {
        // 插入或更新 Task 表
        mContentResolver.insert(
                TaskContract.buildTasksUriWith(getAccount()),
                TaskEngine.taskTo(task));
        // 更新TaskGroup表数据
        updateTaskGroup(true, TaskGroupEntry.COLUMN_NAME_COUNT, 1, task.getGroupId());
        // 通知Loader框架刷新
        notifyUri(getTaskGroupUri());
    }

    @Override
    public void saveTask(@NonNull Task task, ResultCallback callback) {
        try {
            saveTask(task);
            sendSuccess(callback);
        } catch (Exception e) {
            e.printStackTrace();
            sendError(callback);
        }
    }

    @Override
    public void startRunningTask(@NonNull Task task) {
        mContentResolver.update(
                TaskContract.buildTasksUriWith(getAccount(), task.getId()),
                TaskEngine.taskToRunning(1),
                null,
                null
        );
        updateTaskGroup(true, TaskGroupEntry.COLUMN_NAME_RUNNING, 1, task.getGroupId());
        notifyUri(getTaskGroupUri());
    }

    @Override
    public void startRunningTask(@NonNull Task task, ResultCallback callback) {
        try {
            startRunningTask(task);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
        }
    }

    @Override
    public void stopRunningTask(@NonNull Record record) {
        mContentResolver.update(
                TaskContract.buildTasksUriWith(getAccount(), record.getTaskId()),
                TaskEngine.taskToRunning(0),
                null,
                null
        );
        String table1 = TaskContract.TaskGroupEntry.getTableName(getAccount());
        String table2 = TaskContract.TaskEntry.getTableName(getAccount());
        String sentence = String.format(
                Locale.getDefault(),
                "UPDATE %s SET %s = %s - 1 WHERE %s = (SELECT %s FROM %s WHERE %s = %d)",
                table1,
                TaskGroupEntry.COLUMN_NAME_RUNNING,
                TaskGroupEntry.COLUMN_NAME_RUNNING,
                TaskGroupEntry._ID,
                TaskContract.TaskEntry.COLUMN_NAME_GROUP_ID,
                table2,
                TaskContract.TaskEntry._ID,
                record.getTaskId()
        );
        // 更新任务组
        mContentResolver.update(
                TaskContract.buildRawUpdateUri(getAccount()),
                null,
                sentence,
                null
        );
        notifyUri(getTaskGroupUri());
    }

    @Override
    public void stopRunningTask(@NonNull Record record, ResultCallback callback) {
        try {
            stopRunningTask(record);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
        }
    }

    @Override
    public void transformTasks(@NonNull List<Integer> taskIds, @NonNull TaskGroup from, @NonNull TaskGroup to) {
        // 移动Task到另外的分组
        SelectionBuilder builder  = new SelectionBuilder();
        builder.in(TaskContract.TaskEntry._ID, null);
        for (int i : taskIds) {
            builder.in(null, String.valueOf(i));
        }
        mContentResolver.update(
                TaskContract.buildTasksUriWith(getAccount()),
                TaskEngine.taskToActivate(to),
                builder.getSelection(),
                builder.getSelectionArgs()
        );
        updateTaskGroup(false, TaskGroupEntry.COLUMN_NAME_COUNT, taskIds.size(), from.getId());
        updateTaskGroup(true, TaskGroupEntry.COLUMN_NAME_COUNT, taskIds.size(), to.getId());
        notifyUri(getTaskGroupUri());
    }

    @Override
    public void transformTasks(@NonNull List<Integer> taskIds, @NonNull TaskGroup from,
                               @NonNull TaskGroup to, ResultCallback callback) {
        try {
            transformTasks(taskIds, from, to);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
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
    public void completeTask(@NonNull Task task, ResultCallback callback) {
        try {
            completeTask(task);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
        }
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
    public void completeTasks(@NonNull List<Integer> taskIds, int groupId, ResultCallback callback) {
        try {
            completeTasks(taskIds, groupId);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
        }
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
    public void activateTask(@NonNull Task task, @NonNull TaskGroup group, ResultCallback callback) {
        try {
            activateTask(task, group);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
        }
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
    public void activateTasks(@NonNull List<Integer> taskIds, @NonNull TaskGroup group, ResultCallback callback) {
        try {
            activateTasks(taskIds, group);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
        }
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
    public void recycleTask(@NonNull Task task, ResultCallback callback) {
        try {
            recycleTask(task);
            sendSuccess(callback);
        } catch (Exception e) {
            e.printStackTrace();
            sendError(callback);
        }
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
    public void recycleTasks(@NonNull List<Integer> taskIds, int groupId, ResultCallback callback) {
        try {
            recycleTasks(taskIds, groupId);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
        }
    }

    @Override
    public void deleteTask(@NonNull Task task) {
        deleteTask(task.getId());
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
    public void deleteTask(@NonNull Task task, ResultCallback callback) {
        try {
            deleteTask(task);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
        }
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
    public void deleteTasks(@NonNull List<Integer> taskIds, int groupId, ResultCallback callback) {
        try {
            deleteTasks(taskIds, groupId);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
        }
    }

    @Override
    public void checkRunningTasks(@NonNull GetDataCallback<Integer> callback) {
        Cursor ret = mContentResolver.query(
                TaskContract.buildRecordsUriWith(getAccount()),
                new String[]{TaskContract.RecordEntry._ID},
                TaskTableHelper.WHERE_RECORD_RUN,
                null,
                null
        );
        int count = 0;
        if (ret != null) {
            count = ret.getCount();
            ret.close();
        }
        callback.onDataLoaded(count);
    }

    @Override
    public void initAndCheckRecords(@NonNull GetDataListCallback<Record> callback) {
        // TODO 检查未能正常结束的记录
    }

    @Override
    public void beginRecord(@NonNull Task task) {
        mContentResolver.insert(
                TaskContract.buildRecordsUriWith(getAccount()),
                TaskEngine.recordBegin(task, 0)
        );
    }

    @Override
    public void beginRecord(@NonNull Task task, ResultCallback callback) {
        try {
            beginRecord(task);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
        }
    }

    @Override
    public void endRecord(@NonNull Record record, String remark) throws ParseException {
        long usedTime = TaskEngine.recordUsedTime(record);
        // 更新Record
        mContentResolver.update(
                TaskContract.buildRecordsUriWith(getAccount(), record.getId()),
                TaskEngine.recordEnd(usedTime, remark),
                null,
                null
        );
        // 更新Task
        updateTaskTime(true, record.getTaskId(), usedTime);
    }

    @Override
    public void endRecord(@NonNull Record record, String remark, ResultCallback callback) {
        try {
            endRecord(record, remark);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
        }
    }

    @Override
    public void updateRecord(@NonNull Record record, String remark) {
        ContentValues values = new ContentValues();
        values.put(TaskContract.RecordEntry.COLUMN_NAME_REMARK, remark);
        mContentResolver.update(
                TaskContract.buildRecordsUriWith(getAccount(), record.getId()),
                values,
                null,
                null
        );
    }

    @Override
    public void updateRecord(@NonNull Record record, String remark, ResultCallback callback) {
        try {
            updateRecord(record, remark);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
        }
    }

    @Override
    public void deleteRecord(@NonNull Record record) {
        mContentResolver.delete(
                TaskContract.buildRecordsUriWith(getAccount(), record.getId()),
                null,
                null
        );
    }

    @Override
    public void deleteRecord(@NonNull Record record, ResultCallback callback) {
        try {
            deleteRecord(record);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
        }
    }

    @Override
    public void deleteRecords(@NonNull Task task, long time, @NonNull List<Integer> recordIds) {
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
        updateTaskTime(false, task.getId(), time);
    }

    @Override
    public void deleteRecords(@NonNull Task task, long time, @NonNull List<Integer> recordIds, ResultCallback callback) {
        try {
            deleteRecords(task, time, recordIds);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
        }
    }

    @Override
    public void queryRecords(String selection, String[] selectionArgs, String sortOrder,
                             @NonNull final GetDataListCallback<Record> callback) {
        Cursor c = mContentResolver.query(
                TaskContract.buildRecordsUriWith(getAccount()),
                TaskTableHelper.RECORD_PROJECTION,
                selection,
                selectionArgs,
                sortOrder
        );
        final ArrayList<Record> list = new ArrayList<>();
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    do {
                        Record group = TaskEngine.recordFrom(c);
                        list.add(group);
                    } while (c.moveToNext());
                }
            } finally {
                c.close();
            }
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onDataLoaded(list);
            }
        });
    }

    @Override
    public void saveEpisode(@NonNull Episode episode) {
        mContentResolver.insert(
                TaskContract.buildEpisodesUriWith(getAccount()),
                TaskEngine.episodeTo(episode)
        );
    }

    @Override
    public void saveEpisode(@NonNull Episode episode, ResultCallback callback) {
        try {
            saveEpisode(episode);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
        }
    }

    @Override
    public void deleteEpisode(@NonNull Episode episode) {
        deleteEpisode(episode.getId());
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
    public void deleteEpisode(@NonNull Episode episode, ResultCallback callback) {
        try {
            deleteEpisode(episode);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
        }
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
    public void deleteEpisodes(@NonNull List<Integer> episodeIds, ResultCallback callback) {
        try {
            deleteEpisodes(episodeIds);
            sendSuccess(callback);
        } catch (Exception e) {
            sendError(callback);
        }
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
                    // 更新SharedPreferences
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
    }

    @Override
    public void deleteAccount(@NonNull String account) {
        // TODO
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
                TaskContract.buildRawUpdateUri(getAccount()),
                null,
                sentence,
                null
        );
    }

    private void updateTaskTime(boolean increase, int taskId, long delta) {
        String table = TaskContract.TaskEntry.getTableName(getAccount());
        String column = TaskContract.TaskEntry.COLUMN_NAME_ACCUMULATED_TIME;
        String template = increase ? TaskTableHelper.SQL_AUTO_INCREASE : TaskTableHelper.SQL_AUTO_DECREASE;
        String sentence = String.format(template, table, column, column, delta, taskId);
        mContentResolver.update(
                TaskContract.buildRawUpdateUri(getAccount()),
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

    private void sendSuccess(final ResultCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess();
            }
        });
    }

    private void sendError(final ResultCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError();
            }
        });
    }

}
