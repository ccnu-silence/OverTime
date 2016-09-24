package wistcat.overtime.model;

import android.support.annotation.NonNull;

/**
 * 任务基类
 *
 * @author wistcat 2016/8/28
 */
public class Task extends Entity implements Comparable<Task> {
    public static final int TYPE_MANUAL = 0x01;
    public static final int TYPE_TIMING = 0x02;
    public static final int TYPE_SHORT = 0x03;
    public static final int TYPE_LIMITED = 0x04;

    private final int mGroupId;
    private final String mGroupName;
    private final int mType;
    private final TaskState mState;
    private final String mName;
    private final String mDescription;
    private final String mRemark;
    private final int mCompletedDegree;
    private final long mSumTime;
    /* 用于放置创建日期 */
    private String extra_1;
    private String extra_2;
    private String extra_3;
    private String extra_4;

    public Task(int groupId, String groupName, int id, int uuid, int type, TaskState taskState, String name,
                String description, long sumTime) {
        this(groupId, groupName, id, uuid, type, taskState, name, description, sumTime, null, -1);
    }

    public Task(int groupId, String groupName, int id, int uuid, int type, TaskState taskState, String name,
                String description, long sumTime, String remark, int completedDegree) {
        super(id, uuid);
        mGroupId = groupId;
        mGroupName = groupName;
        mType = type;
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

    public String getGroupName() {
        return mGroupName;
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

    public long getSumTime() {
        return mSumTime;
    }

    @Override
    public int getType() {
        return mType;
    }

    /* 设置创建日期 */
    public Task setExtra1(String extra) {
        extra_1 = extra;
        return this;
    }
    /* 获取创建日期 */
    public String getExtra_1() {
        return extra_1;
    }

    public String getExtra_2() {
        return extra_2;
    }

    public Task setExtra2(String extra) {
        extra_2 = extra;
        return this;
    }

    public String getExtra_3() {
        return extra_3;
    }

    public Task setExtra3(String extra) {
        extra_3 = extra;
        return this;
    }

    public String getExtra_4() {
        return extra_4;
    }

    public Task setExtra4(String extra) {
        extra_4 = extra;
        return this;
    }

    @Override
    public int compareTo(@NonNull Task t) {
        // FIXME
        return 0;
    }

}
