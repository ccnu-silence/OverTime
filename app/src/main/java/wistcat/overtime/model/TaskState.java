package wistcat.overtime.model;

/**
 * Task状态<br/>
 *
 * @author wistcat 2016/8/29
 */
public enum TaskState {

    /* Activate 可以转换为 Completed或Recycled */
    Activate,

    /* Completed 可转换为 Activate 或 Recycled */
    Completed,

    /* Recycled 可转换为 Activate */
    Recycled
}
