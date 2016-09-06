package wistcat.overtime.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import wistcat.overtime.R;
import wistcat.overtime.model.Episode;
import wistcat.overtime.model.Record;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;
import wistcat.overtime.model.TaskState;

import static wistcat.overtime.data.db.TaskContract.EpisodeEntry;
import static wistcat.overtime.data.db.TaskContract.RecordEntry;
import static wistcat.overtime.data.db.TaskContract.TaskEntry;
import static wistcat.overtime.data.db.TaskContract.TaskGroupEntry;
import static wistcat.overtime.data.db.TaskTableHelper.QUERY_EPISODE_PROJECTION;
import static wistcat.overtime.data.db.TaskTableHelper.QUERY_RECORD_PROJECTION;
import static wistcat.overtime.data.db.TaskTableHelper.QUERY_TASK_GROUP_PROJECTION;
import static wistcat.overtime.data.db.TaskTableHelper.QUERY_TASK_PROJECTION;

/**
 * ContentValues/Cursor等数据 与 TaskGroup/Task/Record/Episode的转换，以及其他的一些相关数据的转换
 *
 * @author wistcat 2016/9/1
 */
public class TaskEngine {

    private TaskEngine(){}

    /** 为一个TaskGroup生成一个ContentValues，用于插入数据库 */
    public static ContentValues taskGroupTo(@NonNull TaskGroup group) {
        ContentValues values = new ContentValues();
        values.put(TaskGroupEntry.COLUMN_NAME_GROUP_ID, group.getId());
        values.put(TaskGroupEntry.COLUMN_NAME_GROUP_NAME, group.getName());
        values.put(TaskGroupEntry.COLUMN_NAME_GROUP_ACCOUNT, group.getAccount());
        values.put(TaskGroupEntry.COLUMN_NAME_COUNT_ACTIVATE, group.getActive());
        values.put(TaskGroupEntry.COLUMN_NAME_COUNT_RUNNING, group.getRunning());
        values.put(TaskGroupEntry.COLUMN_NAME_COUNT_COMPLETED, group.getCompleted());
        values.put(TaskGroupEntry.COLUMN_NAME_COUNT_RECYCLED, group.getRecycled());
        return values;
    }

    /** 为一个Task生成一个ContentValues，用于插入数据库 */
    public static ContentValues taskTo(@NonNull Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_GROUP_ID, task.getGroupId());
        values.put(TaskEntry.COLUMN_NAME_TASK_STATE, task.getState().name());
        values.put(TaskEntry.COLUMN_NAME_TASK_ID, task.getId());
        values.put(TaskEntry.COLUMN_NAME_TASK_NAME, task.getName());
        values.put(TaskEntry.COLUMN_NAME_TASK_TYPE, task.getType());
        values.put(TaskEntry.COLUMN_NAME_DESCREPTION, task.getDescription());
        values.put(TaskEntry.COLUMN_NAME_REMARK, task.getRemark());
        values.put(TaskEntry.COLUMN_NAME_COMPLETED_DEGREE, task.getCompletedDegree());
        values.put(TaskEntry.COLUMN_NAME_ACCUMULATED_TIME, task.getSumTime());
        values.put(TaskEntry.COLUMN_NAME_EXTRA_1, task.getExtra_1());
        values.put(TaskEntry.COLUMN_NAME_EXTRA_2, task.getExtra_2());
        values.put(TaskEntry.COLUMN_NAME_EXTRA_3, task.getExtra_3());
        values.put(TaskEntry.COLUMN_NAME_EXTRA_4, task.getExtra_4());
        return values;
    }

    /** 为一个Record生成一个ContentValues，用于插入数据库 */
    public static ContentValues recordTo(@NonNull Record record) {
        ContentValues values = new ContentValues();
        values.put(RecordEntry.COLUMN_NAME_TASK_ID, record.getTaskId());
        values.put(RecordEntry.COLUMN_NAME_RECORD_ID, record.getId());
        values.put(RecordEntry.COLUMN_NAME_REOCRD_TYPE, record.getType());
        values.put(RecordEntry.COLUMN_NAME_USED_TIME, record.getUsedTime());
        values.put(RecordEntry.COLUMN_NAME_START_TIME, record.getStartTime());
        values.put(RecordEntry.COLUMN_NAME_END_TIME, record.getEndTime());
        values.put(RecordEntry.COLUMN_NAME_REMARK, record.getRemark());
        values.put(RecordEntry.COLUMN_NAME_EXTRA_1, record.getExtra_1());
        values.put(RecordEntry.COLUMN_NAME_EXTRA_2, record.getExtra_2());
        values.put(RecordEntry.COLUMN_NAME_EXTRA_3, record.getExtra_3());
        values.put(RecordEntry.COLUMN_NAME_EXTRA_4, record.getExtra_4());
        return values;
    }

    /** 为一个Episode生成一个ContentValues，用于插入数据库 */
    public static ContentValues episodeTo(@NonNull Episode episode) {
        ContentValues values = new ContentValues();
        values.put(EpisodeEntry.COLUMN_NAME_RECORD_ID, episode.getRecordId());
        values.put(EpisodeEntry.COLUMN_NAME_EPISODE_ID, episode.getId());
        values.put(EpisodeEntry.COLUMN_NAME_EPISODE_NAME, episode.getName());
        values.put(EpisodeEntry.COLUMN_NAME_EPISODE_TYPE, episode.getType());
        values.put(EpisodeEntry.COLUMN_NAME_EPISODE_REMARK, episode.getRemark());
        values.put(EpisodeEntry.COLUMN_NAME_EPISODE_START_TIME, episode.getStartTime());
        values.put(EpisodeEntry.COLUMN_NAME_EPISODE_SEQ, episode.getSeq());
        values.put(EpisodeEntry.COLUMN_NAME_EXTRA_1, episode.getExtra_1());
        values.put(EpisodeEntry.COLUMN_NAME_EXTRA_2, episode.getExtra_2());
        values.put(EpisodeEntry.COLUMN_NAME_EXTRA_3, episode.getExtra_3());
        values.put(EpisodeEntry.COLUMN_NAME_EXTRA_4, episode.getExtra_4());
        return values;
    }

    public static TaskGroup taskGroupFrom(@NonNull Cursor cursor) {
        int id = cursor.getInt(QUERY_TASK_GROUP_PROJECTION.COLUMN_NAME_GROUP_ID);
        String name = cursor.getString(QUERY_TASK_GROUP_PROJECTION.COLUMN_NAME_GROUP_NAME);
        String account = cursor.getString(QUERY_TASK_GROUP_PROJECTION.COLUMN_NAME_GROUP_ACCOUNT);
        int activate = cursor.getInt(QUERY_TASK_GROUP_PROJECTION.COLUMN_NAME_COUNT_ACTIVATE);
        int running = cursor.getInt(QUERY_TASK_GROUP_PROJECTION.COLUMN_NAME_COUNT_RUNNING);
        int completed = cursor.getInt(QUERY_TASK_GROUP_PROJECTION.COLUMN_NAME_COUNT_COMPLETED);
        int recycled = cursor.getInt(QUERY_TASK_GROUP_PROJECTION.COLUMN_NAME_COUNT_RECYCLED);
        TaskGroup ret = new TaskGroup(id, name, account);
        ret.init(activate, running, completed, recycled);
        return ret;
    }

    public static Task taskFrom(@NonNull Cursor cursor) {
        int group_id = cursor.getInt(QUERY_TASK_PROJECTION.GROUP_ID);
        TaskState state = stateFrom(cursor.getString(QUERY_TASK_PROJECTION.TASK_STATE));
        int task_id = cursor.getInt(QUERY_TASK_PROJECTION.TASK_ID);
        String name = cursor.getString(QUERY_TASK_PROJECTION.TASK_NAME);
        int type = cursor.getInt(QUERY_TASK_PROJECTION.TASK_TYPE);
        String description = cursor.getString(QUERY_TASK_PROJECTION.TASK_DESCRIPTION);
        String remark = cursor.getString(QUERY_TASK_PROJECTION.TASK_REMARK);
        int degree = cursor.getInt(QUERY_TASK_PROJECTION.TASK_COMPLETED_DEGREE);
        long time = cursor.getLong(QUERY_TASK_PROJECTION.TASK_ACCUMULATED_TIME);
        String extra1 = cursor.getString(QUERY_TASK_PROJECTION.EXTRA_1);
        String extra2 = cursor.getString(QUERY_TASK_PROJECTION.EXTRA_2);
        String extra3 = cursor.getString(QUERY_TASK_PROJECTION.EXTRA_3);
        String extra4 = cursor.getString(QUERY_TASK_PROJECTION.EXTRA_4);
        Task ret = new Task(group_id, task_id, type, state, name, description, time, remark, degree);
        return ret.setExtra1(extra1).setExtra2(extra2).setExtra3(extra3).setExtra4(extra4);
    }

    public static Record recordFrom(@NonNull Cursor cursor) {
        int task_id = cursor.getInt(QUERY_RECORD_PROJECTION.COLUMN_NAME_TASK_ID);
        int record_id = cursor.getInt(QUERY_RECORD_PROJECTION.COLUMN_NAME_RECORD_ID);
        int type = cursor.getInt(QUERY_RECORD_PROJECTION.COLUMN_NAME_REOCRD_TYPE);
        long used = cursor.getLong(QUERY_RECORD_PROJECTION.COLUMN_NAME_USED_TIME);
        String start = cursor.getString(QUERY_RECORD_PROJECTION.COLUMN_NAME_START_TIME);
        String end = cursor.getString(QUERY_RECORD_PROJECTION.COLUMN_NAME_END_TIME);
        String remark = cursor.getString(QUERY_RECORD_PROJECTION.COLUMN_NAME_REMARK);
        String extra1 = cursor.getString(QUERY_RECORD_PROJECTION.COLUMN_NAME_EXTRA_1);
        String extra2 = cursor.getString(QUERY_RECORD_PROJECTION.COLUMN_NAME_EXTRA_2);
        String extra3 = cursor.getString(QUERY_RECORD_PROJECTION.COLUMN_NAME_EXTRA_3);
        String extra4 = cursor.getString(QUERY_RECORD_PROJECTION.COLUMN_NAME_EXTRA_4);
        Record ret = new Record(record_id, task_id, type, used, start, end, remark);
        return ret.setExtra1(extra1).setExtra2(extra2).setExtra3(extra3).setExtra4(extra4);
    }

    public static Episode episodeFrom(@NonNull Cursor cursor) {
        int record_id = cursor.getInt(QUERY_EPISODE_PROJECTION.COLUMN_NAME_RECORD_ID);
        int episode_id = cursor.getInt(QUERY_EPISODE_PROJECTION.COLUMN_NAME_EPISODE_ID);
        String name = cursor.getString(QUERY_EPISODE_PROJECTION.COLUMN_NAME_EPISODE_NAME);
        int type = cursor.getInt(QUERY_EPISODE_PROJECTION.COLUMN_NAME_EPISODE_TYPE);
        String remark = cursor.getString(QUERY_EPISODE_PROJECTION.COLUMN_NAME_EPISODE_REMARK);
        String start = cursor.getString(QUERY_EPISODE_PROJECTION.COLUMN_NAME_EPISODE_START_TIME);
        int seq = cursor.getInt(QUERY_EPISODE_PROJECTION.COLUMN_NAME_EPISODE_SEQ);
        String extra1 = cursor.getString(QUERY_EPISODE_PROJECTION.COLUMN_NAME_EXTRA_1);
        String extra2 = cursor.getString(QUERY_EPISODE_PROJECTION.COLUMN_NAME_EXTRA_2);
        String extra3 = cursor.getString(QUERY_EPISODE_PROJECTION.COLUMN_NAME_EXTRA_3);
        String extra4 = cursor.getString(QUERY_EPISODE_PROJECTION.COLUMN_NAME_EXTRA_4);
        Episode ret = new Episode(episode_id, record_id, type, name, remark, start, seq);
        return ret.setExtra1(extra1).setExtra2(extra2).setExtra3(extra3).setExtra4(extra4);
    }

    public static TaskState stateFrom(String state) {
        switch (state) {
            case "Activate":
                return TaskState.Activate;
            case "Running":
                return TaskState.Running;
            case "Completed":
                return TaskState.Completed;
            case "Recycled":
                return TaskState.Recycled;
            default:
                throw new IllegalArgumentException("unkown state: " + state);
        }
    }

    /** 生成指定TaskState的ContentValues，用于更新数据库 */
    public static ContentValues taskToState(int taskId, String taskState) {
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_TASK_ID, taskId);
        values.put(TaskEntry.COLUMN_NAME_TASK_STATE, taskState);
        return values;
    }

    public static int taskToRes(int taskType) {
        switch (taskType) {
            case Task.TYPE_MANUAL:
                return R.drawable.item_color_header_manual;
            case Task.TYPE_TIMING:
                return R.drawable.item_color_header_timing;
            case Task.TYPE_SHORT:
                return R.drawable.item_color_header_short;
            case Task.TYPE_LIMITED:
                return R.drawable.item_color_header_limited;
            default:
                throw new IllegalArgumentException("unkown tast type: " + taskType);
        }
    }

    /** 根据Task类型获取Drawable资源，用于绘制Item颜色 */
    public static int taskToRes(@NonNull Task task) {
        return taskToRes(task.getType());
    }
}
