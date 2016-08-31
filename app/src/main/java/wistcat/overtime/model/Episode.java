package wistcat.overtime.model;

import android.support.annotation.NonNull;

/**
 * 在一次任务记录中的插入事件
 *
 * @author wistcat 2016/8/28
 */
public abstract class Episode extends Entity implements Comparable<Episode> {

    private final int mRecordId;
    private final String mName;
    private final String mRemark;
    private final String mStartTime;
    private final int mSeq;

    public Episode(int id, int recordId, String name, String remark, String startTime, int seq) {
        super(id);
        mRecordId = recordId;
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
    public int compareTo(@NonNull Episode other) {
        return mSeq - other.getSeq(); // 升序
    }

}
