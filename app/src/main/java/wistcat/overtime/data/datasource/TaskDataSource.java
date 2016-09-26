package wistcat.overtime.data.datasource;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.util.List;

import wistcat.overtime.interfaces.GetDataListCallback;
import wistcat.overtime.interfaces.ResultCallback;
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

    void saveTaskGroup(@NonNull TaskGroup group, ResultCallback callback);

    void deleteTaskGroup(@NonNull TaskGroup taskGroup);

    void deleteTaskGroup(int taskGroupId);

    void deleteTaskGroup(@NonNull TaskGroup taskGroup, ResultCallback callback);

    void deleteTaskGroups(@NonNull List<Integer> taskGroupIds);

    void deleteTaskGroups(@NonNull List<Integer> taskGroupIds, ResultCallback callback);

    void getTaskGroups(@NonNull GetDataListCallback<TaskGroup> callback);

    // ----task-----

    /* 0/Activate->Activate,  Completed->Completed */
    void saveTask(@NonNull Task task);

    void saveTask(@NonNull Task task, ResultCallback callback);

    /* Activate */
    void startRunningTask(@NonNull Task task);

    void startRunningTask(@NonNull Task task, ResultCallback callback);

    /* Activate */
    void stopRunningTask(@NonNull Record record);

    void stopRunningTask(@NonNull Record record, ResultCallback callback);

    /* 更改Task所属 Activate -> Activate */
    void transformTasks(@NonNull List<Integer> taskIds, @NonNull TaskGroup from, @NonNull TaskGroup to);

    void transformTasks(@NonNull List<Integer> taskIds, @NonNull TaskGroup from,
                        @NonNull TaskGroup to, ResultCallback callback);

    /* Activiate/Running -> Completed */
    void completeTask(@NonNull Task task);

    void completeTask(@NonNull Task task, ResultCallback callback);

    /* 只在TaskGroup打开页才有编辑功能 */
    void completeTasks(@NonNull List<Integer> taskIds, int groupId);

    void completeTasks(@NonNull List<Integer> taskIds, int groupId, ResultCallback callback);

    /* Completed/Recycled -> Activate (need new groupTask) */
    void activateTask(@NonNull Task task, @NonNull TaskGroup group);

    void activateTask(@NonNull Task task, @NonNull TaskGroup group, ResultCallback callback);

    /* 只在TaskGroup打开页才有编辑功能 */
    void activateTasks(@NonNull List<Integer> taskIds, @NonNull TaskGroup group);

    void activateTasks(@NonNull List<Integer> taskIds, @NonNull TaskGroup group, ResultCallback callback);

    /* Activate/Completed -> Recycled */
    void recycleTask(@NonNull Task task);

    void recycleTask(@NonNull Task task, ResultCallback callback);

    /* 只在TaskGroup打开页才有编辑功能 */
    void recycleTasks(@NonNull List<Integer> taskIds, int groupId);

    void recycleTasks(@NonNull List<Integer> taskIds, int groupId, ResultCallback callback);

    /* delete */
    void deleteTask(@NonNull Task task);

    void deleteTask(int taskId);

    void deleteTask(@NonNull Task task, ResultCallback callback);

    void deleteTasks(@NonNull List<Integer> taskIds, int groupId);

    void deleteTasks(@NonNull List<Integer> taskIds, int groupId, ResultCallback callback);

    // ----Record----

    void initAndCheckRecords(@NonNull GetDataListCallback<Record> callback);

    void beginRecord(@NonNull Task task);

    void beginRecord(@NonNull Task task, ResultCallback callback);

    void endRecord(@NonNull Record record, String remark) throws ParseException;

    void endRecord(@NonNull Record record, String remark, ResultCallback callback);

    void updateRecord(@NonNull Record record, String remark);

    void updateRecord(@NonNull Record record, String remark, ResultCallback callback);

    void deleteRecord(@NonNull Record record);

    void deleteRecord(@NonNull Record record, ResultCallback callback);

    void deleteRecords(@NonNull Task task, long time, @NonNull List<Integer> recordIds);

    void deleteRecords(@NonNull Task task, long time, @NonNull List<Integer> recordIds, ResultCallback callback);

    // ----Episode----

    void saveEpisode(@NonNull Episode episode);

    void saveEpisode(@NonNull Episode episode, ResultCallback callback);

    void deleteEpisode(@NonNull Episode episode);

    void deleteEpisode(int episodeId);

    void deleteEpisode(@NonNull Episode episode, ResultCallback callback);

    void deleteEpisodes(@NonNull List<Integer> episodeIds);

    void deleteEpisodes(@NonNull List<Integer> episodeIds, ResultCallback callback);

    // ----account----

    void initAccount(@NonNull String account);

    void deleteAccount(@NonNull String account);

    //  ----other----

    void deleteTables();

}
