package wistcat.overtime.data.datasource;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

import wistcat.overtime.data.datasource.local.Local;
import wistcat.overtime.interfaces.GetDataCallback;
import wistcat.overtime.interfaces.GetDataListCallback;
import wistcat.overtime.interfaces.ResultCallback;
import wistcat.overtime.model.Episode;
import wistcat.overtime.model.Record;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;

/**
 * 数据接口，向上为Presenter层提供统一服务接口， 向下直接管理全部的数据源
 *
 * @author wistcat 2016/9/1
 */
@Singleton
public class TaskRepository implements TaskDataSource {
    private static final String TAG = "TaskRepository_TAG";

    /* 创建线程池 */
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 128;
    private static final int KEEP_ALIVE = 1;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable, "Repository #" + mCount.getAndIncrement());
        }
    };
    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<>(10);
    public static final Executor mExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
            KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

    /* 本地数据源 */
    private final TaskDataSource mLocalDataSource;
    private boolean isDirty;

    @Inject
    public TaskRepository(@NonNull @Local TaskDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    @Override
    public void saveTaskGroup(@NonNull final TaskGroup taskGroup) {
        mExecutor.execute(new Runnable(){
            @Override
            public void run() {
                mLocalDataSource.saveTaskGroup(taskGroup);
            }
        });
    }

    @Override
    public void saveTaskGroup(@NonNull final TaskGroup group, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.saveTaskGroup(group, callback);
            }
        });
    }

    @Override
    public void deleteTaskGroup(@NonNull final TaskGroup taskGroup) {
        deleteTaskGroup(taskGroup.getId());
    }

    @Override
    public void deleteTaskGroup(final int taskGroupId) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.deleteTaskGroup(taskGroupId);
            }
        });
    }

    @Override
    public void deleteTaskGroup(@NonNull final TaskGroup taskGroup, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.deleteTaskGroup(taskGroup, callback);
            }
        });
    }

    @Override
    public void deleteTaskGroups(@NonNull final List<Integer> taskGroupIds) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.deleteTaskGroups(taskGroupIds);
            }
        });
    }

    @Override
    public void deleteTaskGroups(@NonNull final List<Integer> taskGroupIds, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.deleteTaskGroups(taskGroupIds, callback);
            }
        });
    }

    @Override
    public void getTaskGroups(@NonNull GetDataListCallback<TaskGroup> callback) {
        mLocalDataSource.getTaskGroups(callback);
    }

    @Override
    public void saveTask(@NonNull final Task task) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.saveTask(task);
            }
        });
    }

    @Override
    public void saveTask(@NonNull final Task task, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.saveTask(task, callback);
            }
        });
    }

    @Override
    public void startRunningTask(@NonNull final Task task) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.startRunningTask(task);
            }
        });
    }

    @Override
    public void startRunningTask(@NonNull final Task task, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.startRunningTask(task, callback);
            }
        });
    }

    @Override
    public void stopRunningTask(@NonNull final Record record) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.stopRunningTask(record);
            }
        });
    }

    @Override
    public void stopRunningTask(@NonNull final Record record, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.stopRunningTask(record, callback);
            }
        });
    }

    @Override
    public void transformTasks(@NonNull final List<Integer> taskIds, @NonNull final TaskGroup from,
                               @NonNull final TaskGroup to) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.transformTasks(taskIds, from, to);
            }
        });
    }

    @Override
    public void transformTasks(@NonNull final List<Integer> taskIds, @NonNull final TaskGroup from,
                               @NonNull final TaskGroup to, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.transformTasks(taskIds, from, to, callback);
            }
        });
    }

    @Override
    public void completeTask(@NonNull final Task task) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.completeTask(task);
            }
        });
    }

    @Override
    public void completeTask(@NonNull final Task task, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.completeTask(task, callback);
            }
        });
    }

    @Override
    public void completeTasks(@NonNull final List<Integer> taskIds, final int groupId) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.completeTasks(taskIds, groupId);
            }
        });
    }

    @Override
    public void completeTasks(@NonNull final List<Integer> taskIds, final int groupId, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.completeTasks(taskIds, groupId, callback);
            }
        });
    }

    @Override
    public void activateTask(@NonNull final Task task, final @NonNull TaskGroup group) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.activateTask(task, group);
            }
        });
    }

    @Override
    public void activateTask(@NonNull final Task task, @NonNull final TaskGroup group, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.activateTask(task, group, callback);
            }
        });
    }

    @Override
    public void activateTasks(@NonNull final List<Integer> taskIds, @NonNull final TaskGroup group) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.activateTasks(taskIds, group);
            }
        });
    }

    @Override
    public void activateTasks(@NonNull final List<Integer> taskIds, @NonNull final TaskGroup group,
                              final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.activateTasks(taskIds, group, callback);
            }
        });
    }

    @Override
    public void recycleTask(@NonNull final Task task) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.recycleTask(task);
            }
        });
    }

    @Override
    public void recycleTask(@NonNull final Task task, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.recycleTask(task, callback);
            }
        });
    }

    @Override
    public void recycleTasks(@NonNull final List<Integer> taskIds, final int groupId) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.recycleTasks(taskIds, groupId);
            }
        });
    }

    @Override
    public void recycleTasks(@NonNull final List<Integer> taskIds, final int groupId, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.recycleTasks(taskIds, groupId, callback);
            }
        });
    }

    /* NOTE: 已在固定场景使用，请勿调用，请使用recycleTask */
    @Override
    public void deleteTask(@NonNull final Task task) {
        deleteTask(task.getId());
    }

    /* NOTE: 已在固定场景使用，请勿调用，请使用recycleTask */
    @Override
    public void deleteTask(final int taskId) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.deleteTask(taskId);
            }
        });
    }

    /* NOTE: 已在固定场景使用，请勿调用，请使用recycleTask */
    @Override
    public void deleteTask(@NonNull final Task task, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.deleteTask(task, callback);
            }
        });
    }

    /* NOTE: 已在固定场景使用，请勿调用，请使用recycleTasks */
    @Override
    public void deleteTasks(@NonNull final List<Integer> taskIds, final int groupId) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.deleteTasks(taskIds, groupId);
            }
        });
    }

    /* NOTE: 已在固定场景使用，请勿调用，请使用recycleTasks */
    @Override
    public void deleteTasks(@NonNull final List<Integer> taskIds, final int groupId, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.deleteTasks(taskIds, groupId, callback);
            }
        });
    }

    @Override
    public void checkRunningTasks(@NonNull final GetDataCallback<Integer> callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.checkRunningTasks(callback);
            }
        });
    }

    @Override
    public void initAndCheckRecords(@NonNull final GetDataListCallback<Record> callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.initAndCheckRecords(callback);
            }
        });
    }

    @Override
    public void beginRecord(@NonNull final Task task) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.beginRecord(task);
            }
        });
    }

    @Override
    public void beginRecord(@NonNull final Task task, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.beginRecord(task, callback);
            }
        });
    }

    @Override
    public void endRecord(@NonNull final Record record, final String remark) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mLocalDataSource.endRecord(record, remark);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void endRecord(@NonNull final Record record, final String remark, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.endRecord(record, remark, callback);
            }
        });
    }

    @Override
    public void updateRecord(@NonNull final Record record, final String remark) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.updateRecord(record, remark);
            }
        });
    }

    @Override
    public void updateRecord(@NonNull final Record record, final String remark, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.updateRecord(record, remark, callback);
            }
        });
    }

    @Override
    public void deleteRecord(@NonNull final Record record) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.deleteRecord(record);
            }
        });
    }

    @Override
    public void deleteRecord(@NonNull final Record record, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.deleteRecord(record, callback);
            }
        });
    }

    @Override
    public void deleteRecords(@NonNull final Task task, final long time, @NonNull final List<Integer> recordIds) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.deleteRecords(task, time, recordIds);
            }
        });
    }

    @Override
    public void deleteRecords(@NonNull final Task task, final long time,
                              @NonNull final List<Integer> recordIds, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.deleteRecords(task, time, recordIds, callback);
            }
        });
    }

    @Override
    public void queryRecords(final String selection, final String[] selectionArgs, final String orderBy,
                             @NonNull final GetDataListCallback<Record> callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.queryRecords(selection, selectionArgs, orderBy, callback);
            }
        });
    }

    @Override
    public void saveEpisode(@NonNull final Episode episode) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.saveEpisode(episode);
            }
        });
    }

    @Override
    public void saveEpisode(@NonNull final Episode episode, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.saveEpisode(episode, callback);
            }
        });
    }

    @Override
    public void deleteEpisode(@NonNull final Episode episode) {
        deleteEpisode(episode.getId());
    }

    @Override
    public void deleteEpisode(final int episodeId) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.deleteEpisode(episodeId);
            }
        });
    }

    @Override
    public void deleteEpisode(@NonNull final Episode episode, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.deleteEpisode(episode, callback);
            }
        });
    }

    @Override
    public void deleteEpisodes(@NonNull final List<Integer> episodeIds) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.deleteEpisodes(episodeIds);
            }
        });
    }

    @Override
    public void deleteEpisodes(@NonNull final List<Integer> episodeIds, final ResultCallback callback) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mLocalDataSource.deleteEpisodes(episodeIds, callback);
            }
        });
    }

    @Override
    public void initAccount(@NonNull final String account) {
        Log.i(TAG, "---initAccount---");
        mLocalDataSource.initAccount(account);
    }

    @Override
    public void deleteAccount(@NonNull String account) {
        // TODO
    }

    @Override
    public void deleteTables() {
        // TODO
    }

    public void setDirty() {
        isDirty = true;
    }

    public void clearDirty() {
        isDirty = false;
    }

    public boolean isDirty() {
        return isDirty;
    }
}
