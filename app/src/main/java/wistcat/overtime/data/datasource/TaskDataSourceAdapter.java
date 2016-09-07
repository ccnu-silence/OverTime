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
 * {@link TaskDataSource}的空实现，用于重写部分方法
 *
 * @author wistcat 2016/9/2
 */
public abstract class TaskDataSourceAdapter implements TaskDataSource {

    @Override
    public void saveTaskGroup(@NonNull TaskGroup taskGroup) {

    }

    @Override
    public void deleteTaskGroup(@NonNull TaskGroup taskGroup) {

    }

    @Override
    public void deleteTaskGroup(int taskGroupId) {

    }

    @Override
    public void getCachedTaskGroup(@NonNull GetDataListCallback<TaskGroup> callback) {

    }

    @Override
    public void getCachedTaskGroup(@NonNull GetDataListCallback<TaskGroup> callback, boolean forceRefresh) {

    }

    @Override
    public void setTaskGroupCache(@NonNull List<TaskGroup> data) {

    }

    @Override
    public void setTaskGroupCache(Cursor cursor) {

    }

    @Override
    public boolean isGroupCacheAvailable() {
        return false;
    }

    @Override
    public void saveTask(@NonNull Task task) {

    }

    @Override
    public void startRunningTask(@NonNull Task task) {

    }

    @Override
    public void startRunningTask(int taskId) {

    }

    @Override
    public void stopRunningTask(@NonNull Task task, String toState) {

    }

    @Override
    public void stopRunningTask(int taskId, String toState) {

    }

    @Override
    public void completeTask(@NonNull Task task) {

    }

    @Override
    public void completeTask(int taskId) {

    }

    @Override
    public void activateTask(@NonNull Task task) {

    }

    @Override
    public void activateTask(int taskId) {

    }

    @Override
    public void deleteTask(@NonNull Task task) {

    }

    @Override
    public void deleteTask(int taskId) {

    }

    @Override
    public void deleteAllTasks(int groupId) {

    }

    @Override
    public void saveRecord(@NonNull Record record) {

    }

    @Override
    public void deleteRecord(@NonNull Record record) {

    }

    @Override
    public void deleteRecord(int recordId) {

    }

    @Override
    public void deleteAllRecords(@NonNull Task task) {

    }

    @Override
    public void deleteAllRecords(int taskId) {

    }

    @Override
    public void saveEpisode(@NonNull Episode episode) {

    }

    @Override
    public void deleteEpisode(@NonNull Episode episode) {

    }

    @Override
    public void deleteEpisode(int episodeId) {

    }

    @Override
    public void initAccount(@NonNull String account) {

    }

    @Override
    public void deleteAccount(@NonNull String account) {

    }

    @Override
    public void deleteTables() {

    }
}
