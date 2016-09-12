package wistcat.overtime.model;

/**
 * Task状态<br/>
 *
 * @author wistcat 2016/8/29
 */
public enum TaskState {

    /* Activate 可以转换为 Running或Completed或Recycled // 查询时，需要同时包含Running的数据 */
    Activate,

    /* Running 可转换为 Activate或Completed // 另外，在统计上，Running状态包括在Activate状态里 */
    Running,

    /* Completed 可转换为 Activate 或 Recycled */
    Completed,

    /* Recycled 可转换为 Activate */
    Recycled
}
