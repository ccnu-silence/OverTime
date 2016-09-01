package wistcat.overtime.model;

/**
 * 一次任务记录
 *
 * @author wistcat 2016/8/28
 */
public class Record extends Entity {
    public static final int TYPE_NORMAL = 0x01;

    private final int mTaskId;
    private final int mType;
    private final long mUsedTime;
    private final String mStartTime;
    private final String mEndTime;
    private final String mRemark;
    private String extra_1;
    private String extra_2;
    private String extra_3;
    private String extra_4;

    public Record(int id, int taskId, int type, long usedTime, String startTime, String endTime, String remark) {
        super(id);
        mTaskId = taskId;
        mType = type;
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

    @Override
    public int getType() {
        return mType;
    }

    public Record setExtra1(String extra) {
        extra_1 = extra;
        return this;
    }

    public String getExtra_1() {
        return extra_1;
    }

    public String getExtra_2() {
        return extra_2;
    }

    public Record setExtra2(String extra) {
        extra_2 = extra;
        return this;
    }

    public String getExtra_3() {
        return extra_3;
    }

    public Record setExtra3(String extra) {
        extra_3 = extra;
        return this;
    }

    public String getExtra_4() {
        return extra_4;
    }

    public Record setExtra4(String extra) {
        extra_4 = extra;
        return this;
    }
}
