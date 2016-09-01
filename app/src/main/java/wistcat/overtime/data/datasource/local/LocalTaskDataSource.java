package wistcat.overtime.data.datasource.local;

import android.content.ContentResolver;
import android.content.Context;
import android.support.annotation.NonNull;

import wistcat.overtime.App;
import wistcat.overtime.data.TaskEngine;
import wistcat.overtime.data.datasource.TaskDataSource;
import wistcat.overtime.data.db.TaskContract;
import wistcat.overtime.model.Episode;
import wistcat.overtime.model.Record;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;
import wistcat.overtime.model.TaskState;

/**
 * @author wistcat 2016/9/1
 */
public class LocalTaskDataSource implements TaskDataSource {

    private final ContentResolver mContentResolver;

    public LocalTaskDataSource(@NonNull Context context) {
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void saveTaskGroup(@NonNull TaskGroup taskGroup) {
        mContentResolver.insert(
                TaskContract.buildTaskGroupUri(),
                TaskEngine.taskGroupTo(taskGroup)
        );
    }

    @Override
    public void deleteTaskGroup(@NonNull TaskGroup taskGroup) {
        deleteTaskGroup(taskGroup.getId());
    }

    @Override
    public void deleteTaskGroup(int taskGroupId) {
        mContentResolver.delete(
                TaskContract.buildTaskGroupUriWith(taskGroupId),
                null,
                null
        );
    }

    @Override
    public void saveTask(@NonNull Task task) {
        mContentResolver.insert(
                TaskContract.buildTasksUriWith(getAccount()),
                TaskEngine.taskTo(task));
    }

    @Override
    public void startRunningTask(@NonNull Task task) {
        startRunningTask(task.getId());
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
        stopRunningTask(task.getId(), toState);
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
        completeTask(task.getId());
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
        activateTask(task.getId());
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
        deleteTask(task.getId());
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
        deleteRecord(record.getId());
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
        deleteAllRecords(task.getId());
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
    public void deleteTables() {
        // TODO
    }

    private String getAccount() {
        return App.getInstance().getAccountName();
    }
}
