package wistcat.overtime.model;

/**
 * 一次任务记录
 *
 * @author wistcat 2016/8/28
 */
public abstract class Record extends Entity {

    private final int mTaskId;
    private final long mUsedTime;
    private final String mStartTime;
    private final String mEndTime;
    private final String mRemark;

    public Record(int id, int taskId, long usedTime, String startTime, String endTime, String remark) {
        super(id);
        mTaskId = taskId;
        mUsedTime = usedTime;
        mStartTime = startTime;
        mEndTime = endTime;
        mRemark = remark;
    }

    public String getRemark() {
        return mRemark;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public long getUsedTime() {
        return mUsedTime;
    }

    public int getTaskId() {
        return mTaskId;
    }

}
