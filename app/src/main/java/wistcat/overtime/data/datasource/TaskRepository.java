package wistcat.overtime.data.datasource;

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import wistcat.overtime.data.datasource.local.Local;
import wistcat.overtime.model.Episode;
import wistcat.overtime.model.Record;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;

/**
 * @author wistcat 2016/9/1
 */
@Singleton
public class TaskRepository implements TaskDataSource {

    private final TaskDataSource mLocalDataSource;
    private boolean isDirty;

    @Inject
    public TaskRepository(@NonNull @Local TaskDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    @Override
    public void saveTaskGroup(@NonNull TaskGroup taskGroup) {
        mLocalDataSource.saveTaskGroup(taskGroup);
    }

    @Override
    public void deleteTaskGroup(@NonNull TaskGroup taskGroup) {
        mLocalDataSource.deleteTaskGroup(taskGroup);
    }

    @Override
    public void deleteTaskGroup(int taskGroupId) {
        mLocalDataSource.deleteTaskGroup(taskGroupId);
    }

    @Override
    public void saveTask(@NonNull Task task) {
        mLocalDataSource.saveTask(task);
    }

    @Override
    public void startRunningTask(@NonNull Task task) {
        mLocalDataSource.startRunningTask(task);
    }

    @Override
    public void startRunningTask(int taskId) {
        mLocalDataSource.startRunningTask(taskId);
    }

    @Override
    public void stopRunningTask(@NonNull Task task, String toState) {
        mLocalDataSource.stopRunningTask(task, toState);
    }

    @Override
    public void stopRunningTask(int taskId, String toState) {
        mLocalDataSource.stopRunningTask(taskId, toState);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        mLocalDataSource.completeTask(task);
    }

    @Override
    public void completeTask(int taskId) {
        mLocalDataSource.completeTask(taskId);
    }

    @Override
    public void activateTask(@NonNull Task task) {
        mLocalDataSource.activateTask(task);
    }

    @Override
    public void activateTask(int taskId) {
        mLocalDataSource.activateTask(taskId);
    }

    @Override
    public void deleteTask(@NonNull Task task) {
        mLocalDataSource.deleteTask(task);
    }

    @Override
    public void deleteTask(int taskId) {
        mLocalDataSource.deleteTask(taskId);
    }

    @Override
    public void deleteAllTasks(int groupId) {
        mLocalDataSource.deleteAllTasks(groupId);
    }

    @Override
    public void saveRecord(@NonNull Record record) {
        mLocalDataSource.saveRecord(record);
    }

    @Override
    public void deleteRecord(@NonNull Record record) {
        mLocalDataSource.deleteRecord(record);
    }

    @Override
    public void deleteRecord(int recordId) {
        mLocalDataSource.deleteRecord(recordId);
    }

    @Override
    public void deleteAllRecords(@NonNull Task task) {
        mLocalDataSource.deleteAllRecords(task);
    }

    @Override
    public void deleteAllRecords(int taskId) {
        mLocalDataSource.deleteAllRecords(taskId);
    }

    @Override
    public void saveEpisode(@NonNull Episode episode) {
        mLocalDataSource.saveEpisode(episode);
    }

    @Override
    public void deleteEpisode(@NonNull Episode episode) {
        mLocalDataSource.deleteEpisode(episode);
    }

    @Override
    public void deleteEpisode(int episodeId) {
        mLocalDataSource.deleteEpisode(episodeId);
    }

    @Override
    public void deleteTables() {
        // ...
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
