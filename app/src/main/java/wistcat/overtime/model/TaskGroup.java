package wistcat.overtime.model;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * 任务组
 *
 * @author wistcat 2016/8/31
 */
public class TaskGroup extends Entity {

    private List<Task> mTaskList;

    public TaskGroup(int id) {
        super(id);
    }

    @Override
    public int getType() {
        return 0;
    }

    public void setTaskList(@NonNull List<Task> list) {
        mTaskList = list;
    }

    public List<Task> getTaskList() {
        return mTaskList;
    }

    public int getTaskSize() {
        if (mTaskList != null) {
            return mTaskList.size();
        }
        return 0;
    }
}
