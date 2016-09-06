package wistcat.overtime.data.datasource;

import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.List;

import wistcat.overtime.interfaces.GetDataListCallback;
import wistcat.overtime.model.Episode;
import wistcat.overtime.model.Record;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;

/**
 * 数据源
 *
 * @author wistcat 2016/8/31
 */
public interface TaskDataSource {

    // ----taskgroup---
    void saveTaskGroup(@NonNull TaskGroup taskGroup);

    void deleteTaskGroup(@NonNull TaskGroup taskGroup);

    void deleteTaskGroup(int taskGroupId);

    void getCachedTaskGroup(@NonNull GetDataListCallback<TaskGroup> callback);

    void getCachedTaskGroup(@NonNull GetDataListCallback<TaskGroup> callback, boolean forceRefresh);

    void setTaskGroupCache(@NonNull List<TaskGroup> data);

    void setTaskGroupCache(Cursor cursor);

    boolean isGroupCacheAvailable();

    // ----task-----

    // NOTE: Query功能由Loader框架完成
//    void getTasks(GetDataListCallback<Task> callback);
//    void getTask(int taskId, GetDataCallback<Task> callback);

    /* 0/Activate->Activate,  Completed->Completed */
    void saveTask(@NonNull Task task);

    /* Activate -> Running */
    void startRunningTask(@NonNull Task task);

    void startRunningTask(int taskId);

    /* Running -> Activate/Completed */
    void stopRunningTask(@NonNull Task task, String toState);

    void stopRunningTask(int taskId, String toState);

    /* Activiate/Recycled/Running -> Completed */
    void completeTask(@NonNull Task task);

    void completeTask(int taskId);

    /* completed/Recycled -> Activate */
    void activateTask(@NonNull Task task);

    void activateTask(int taskId);

    void deleteTask(@NonNull Task task);

    void deleteTask(int taskId);

    void deleteAllTasks(int groupId);

    // ----Record----

    // NOTE: Query功能由Loader框架完成
//    void getRecord(@NonNull Task task, @NonNull Record record, GetDataCallback<Record> callback);
//    void getRecords(@NonNull Task task, GetDataListCallback<Record> callback);

    void saveRecord(@NonNull Record record);

    void deleteRecord(@NonNull Record record);

    void deleteRecord(int recordId);

    void deleteAllRecords(@NonNull Task task);

    void deleteAllRecords(int taskId);

    // ----Episode----

    // NOTE: Query功能由Loader框架完成
//    void getEpisode(@NonNull Record record, GetDataCallback<Episode> callback);
//    void getEpisodes(@NonNull Record record, GetDataListCallback<Episode> callback);

    void saveEpisode(@NonNull Episode episode);

    void deleteEpisode(@NonNull Episode episode);

    void deleteEpisode(int episodeId);

    //  ----other----

    void deleteTables();

}
