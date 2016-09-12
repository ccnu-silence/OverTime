package wistcat.overtime.model;

import android.support.annotation.NonNull;

/**
 * 在一次任务记录中的插入事件
 *
 * @author wistcat 2016/8/28
 */
public class Episode extends Entity implements Comparable<Episode> {
    public static final int TYPE_NOTE = 0x10;
    public static final int TYPE_EVENT_START = 0x11;
    public static final int TYPE_EVENT_END = 0x12;

    private final int mRecordId;
    private final int mType;
    private final String mName;
    private final String mRemark;
    private final String mStartTime;
    private final int mSeq;
    private String extra_1;
    private String extra_2;
    private String extra_3;
    private String extra_4;

    public Episode(int id, int uuid, int recordId, int type, String name, String remark, String startTime, int seq) {
        super(id, uuid);
        mRecordId = recordId;
        mType = type;
        mName = name;
        mRemark = remark;
        mStartTime = startTime;
        mSeq = seq;
    }

    public int getSeq() {
        return mSeq;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public String getRemark() {
        return mRemark;
    }

    public int getRecordId() {
        return mRecordId;
    }

    public String getName() {
        return mName;
    }

    @Override
    public int getType() {
        return mType;
    }

    public Episode setExtra1(String extra) {
        extra_1 = extra;
        return this;
    }

    public String getExtra_1() {
        return extra_1;
    }

    public String getExtra_2() {
        return extra_2;
    }

    public Episode setExtra2(String extra) {
        extra_2 = extra;
        return this;
    }

    public String getExtra_3() {
        return extra_3;
    }

    public Episode setExtra3(String extra) {
        extra_3 = extra;
        return this;
    }

    public String getExtra_4() {
        return extra_4;
    }

    public Episode setExtra4(String extra) {
        extra_4 = extra;
        return this;
    }

    @Override
    public int compareTo(@NonNull Episode other) {
        // FIXME
        return mSeq - other.getSeq();
    }

}
