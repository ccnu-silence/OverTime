package wistcat.overtime.data.datasource;

import android.support.annotation.NonNull;

import java.util.List;

import wistcat.overtime.interfaces.GetDataListCallback;
import wistcat.overtime.model.Episode;
import wistcat.overtime.model.Record;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;
import wistcat.overtime.model.TaskState;

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

    void deleteTaskGroups(@NonNull List<Integer> taskGroupIds);

    void getTaskGroups(@NonNull GetDataListCallback<TaskGroup> callback);

    // ----task-----

    /* 0/Activate->Activate,  Completed->Completed */
    void saveTask(@NonNull Task task);

    /* Activate -> Running */
    void startRunningTask(@NonNull Task task);

    void startRunningTask(int taskId);

    /* Running -> Activate/Completed */
    void stopRunningTask(@NonNull Task task, TaskState state);

    /* 更改Task所属 Activate -> Activate */
    void transformTasks(@NonNull List<Integer> taskIds, @NonNull TaskGroup from, @NonNull TaskGroup to);

    /* Activiate/Running -> Completed */
    void completeTask(@NonNull Task task);

    /* 只在TaskGroup打开页才有编辑功能 */
    void completeTasks(@NonNull List<Integer> taskIds, int groupId);

    /* Completed/Recycled -> Activate (need new groupTask) */
    void activateTask(@NonNull Task task, @NonNull TaskGroup group);

    /* 只在TaskGroup打开页才有编辑功能 */
    void activateTasks(@NonNull List<Integer> taskIds, @NonNull TaskGroup group);

    /* Activate/Completed -> Recycled */
    void recycleTask(@NonNull Task task);

    /* 只在TaskGroup打开页才有编辑功能 */
    void recycleTasks(@NonNull List<Integer> taskIds, int groupId);

    /* delete */
    void deleteTask(@NonNull Task task);

    void deleteTask(int taskId);

    void deleteTasks(@NonNull List<Integer> taskIds, int groupId);

    // ----Record----

    void initAndCheckRecords(@NonNull GetDataListCallback<Record> callback);

    void saveRecord(@NonNull Record record);

    void deleteRecord(@NonNull Record record);

    void deleteRecord(int recordId);

    void deleteRecords(@NonNull List<Integer> recordIds);

    // ----Episode----

    void saveEpisode(@NonNull Episode episode);

    void deleteEpisode(@NonNull Episode episode);

    void deleteEpisode(int episodeId);

    void deleteEpisodes(@NonNull List<Integer> episodeIds);

    // ----account----

    void initAccount(@NonNull String account);

    void deleteAccount(@NonNull String account);

    //  ----other----

    void deleteTables();

}
