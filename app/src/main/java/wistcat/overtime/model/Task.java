package wistcat.overtime.model;

import android.support.annotation.NonNull;

/**
 * 任务基类
 *
 * @author wistcat 2016/8/28
 */
public abstract class Task extends Entity implements Comparable<Task> {

    private final int mGroupId;
    private final TaskState mState;
    private final String mName;
    private final String mDescription;
    private final String mRemark;
    private final int mCompletedDegree;
    private final long mSumTime;

    public Task(int groupId, int id, TaskState taskState, String name, String description, long sumTime) {
        this(groupId, id, taskState, name, description, sumTime, null, -1);
    }

    public Task(int groupId, int id, TaskState taskState, String name,
                String description, long sumTime, String remark, int completedDegree) {

        super(id);
        mGroupId = groupId;
        mState = taskState;
        mName = name;
        mDescription = description;
        mSumTime = sumTime;
        mRemark = remark;
        mCompletedDegree = completedDegree;
    }

    public int getGroupId() {
        return mGroupId;
    }

    public TaskState getState() {
        return mState;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getRemark() {
        return mRemark;
    }

    public int getCompletedDegree() {
        return mCompletedDegree;
    }

    public long getCalcTime() {
        return mSumTime;
    }

    @Override
    public int compareTo(@NonNull Task t) {
        // FIXME
        return 0;
    }
}
