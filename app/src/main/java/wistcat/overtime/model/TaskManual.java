package wistcat.overtime.model;

/**
 * 无限期手动任务
 *
 * @author wistcat 2016/8/29
 */
public class TaskManual extends Task {
    public static final int TYPE = 0x01;

    public TaskManual(int groupId, int id, TaskState taskState, String name, String description, long calcTime) {
        this(groupId, id, taskState, name, description, calcTime,  null, -1);
    }

    public TaskManual(int groupId, int id, TaskState taskState, String name,
                      String description, long calcTime, String remark, int completedDegree) {
        super(groupId, id, taskState, name, description, calcTime, remark, completedDegree);
    }

    @Override
    public int getType() {
        return TYPE;
    }

}
