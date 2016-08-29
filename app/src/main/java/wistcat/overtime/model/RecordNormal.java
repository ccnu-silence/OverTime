package wistcat.overtime.model;

/**
 * @author wistcat 2016/8/29
 */
public class RecordNormal extends Record {

    public static final int TYPE = 0x11;

    public RecordNormal(int id, int taskId, long usedTime, String startTime, String endTime, String remark) {
        super(id, taskId, usedTime, startTime, endTime, remark);
    }

    @Override
    public int getType() {
        return TYPE;
    }

}
