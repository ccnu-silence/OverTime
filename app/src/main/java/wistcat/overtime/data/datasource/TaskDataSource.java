package wistcat.overtime.data.datasource;

import java.util.List;

import wistcat.overtime.model.Entity;

/**
 * 数据源
 *
 * @author wistcat 2016/8/31
 */
public interface TaskDataSource {

    interface GetDataListCallback<T extends Entity> {

        void onDataLoaded(List<T> dataSet);

        void onError();
    }

    interface GetDataCallback<T extends Entity> {

        void onDataLoaded(T data);

        void onError();
    }

    void getTasks();

    void getTask();

    void deleteTask(String taskId);

    void deleteAllTasks();

    void deleteRecord(String recordId);

    void deleteAllRecords(String taskId);

    void deleteTables();

}
