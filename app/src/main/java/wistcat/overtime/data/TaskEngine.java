package wistcat.overtime.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.util.UUID;

import wistcat.overtime.App;
import wistcat.overtime.R;
import wistcat.overtime.model.Episode;
import wistcat.overtime.model.Record;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;
import wistcat.overtime.model.TaskState;
import wistcat.overtime.util.Const;
import wistcat.overtime.util.Utils;

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

    /** 用于账户的TaskGroup表的默认组的生成: default、completed、recycled */
    public static ContentValues taskGroupToDefault(@NonNull String account, @NonNull String name, int id) {
        ContentValues values = new ContentValues();
        values.put(TaskGroupEntry._ID, id);
        values.put(TaskGroupEntry.COLUMN_UUID, createId());
        values.put(TaskGroupEntry.COLUMN_NAME_GROUP_NAME, name);
        values.put(TaskGroupEntry.COLUMN_NAME_GROUP_ACCOUNT, account);
        values.put(TaskGroupEntry.COLUMN_NAME_COUNT, 0);
        values.put(TaskGroupEntry.COLUMN_NAME_RUNNING, 0);
        values.put(TaskGroupEntry.COLUMN_NAME_EXTRA_1, "");
        values.put(TaskGroupEntry.COLUMN_NAME_EXTRA_1, "");
        return values;
    }

    /** 为一个TaskGroup生成一个ContentValues，用于插入数据库 */
    public static ContentValues taskGroupTo(@NonNull TaskGroup group) {
        ContentValues values = new ContentValues();
        values.put(TaskGroupEntry.COLUMN_UUID, group.getUUID());
        values.put(TaskGroupEntry.COLUMN_NAME_GROUP_NAME, group.getName());
        values.put(TaskGroupEntry.COLUMN_NAME_GROUP_ACCOUNT, group.getAccount());
        values.put(TaskGroupEntry.COLUMN_NAME_COUNT, group.getTaskCount());
        values.put(TaskGroupEntry.COLUMN_NAME_RUNNING, group.getRunningCount());
        values.put(TaskGroupEntry.COLUMN_NAME_EXTRA_1, group.getExtra_1());
        values.put(TaskGroupEntry.COLUMN_NAME_EXTRA_2, group.getExtra_2());
        return values;
    }

    /** 为一个Task生成一个ContentValues，用于插入数据库 */
    public static ContentValues taskTo(@NonNull Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_UUID, task.getUUID());
        values.put(TaskEntry.COLUMN_NAME_GROUP_ID, task.getGroupId());
        values.put(TaskEntry.COLUMN_NAME_GROUP_NAME, task.getGroupName());
        values.put(TaskEntry.COLUMN_NAME_TASK_STATE, task.getState().name());
        values.put(TaskEntry.COLUMN_NAME_IS_RUNNING, task.getRunning());
        values.put(TaskEntry.COLUMN_NAME_TASK_NAME, task.getName());
        values.put(TaskEntry.COLUMN_NAME_TASK_TYPE, task.getType());
        values.put(TaskEntry.COLUMN_NAME_DESCREPTION, task.getDescription());
        values.put(TaskEntry.COLUMN_NAME_CREATE_TIME, task.getCreateTime());
        values.put(TaskEntry.COLUMN_NAME_REMARK, task.getRemark());
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
        values.put(RecordEntry.COLUMN_UUID, record.getUUID());
        values.put(RecordEntry.COLUMN_NAME_TASK_ID, record.getTaskId());
        values.put(RecordEntry.COLUMN_NAME_TASK_NAME, record.getTaskName());
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

    public static ContentValues recordBegin(@NonNull Task task, int type) {
        ContentValues values = new ContentValues();
        String startTime = Utils.getDate(Utils.FORMAT_DATE_TEMPLATE_FULL);
        values.put(RecordEntry.COLUMN_UUID, createId());
        values.put(RecordEntry.COLUMN_NAME_TASK_ID, task.getId());
        values.put(RecordEntry.COLUMN_NAME_TASK_NAME, task.getName());
        values.put(RecordEntry.COLUMN_NAME_REOCRD_TYPE, type);
        values.put(RecordEntry.COLUMN_NAME_START_TIME, startTime);
        return values;
    }

    public static long recordUsedTime(@NonNull Record record) throws ParseException {
        return System.currentTimeMillis()
                - Utils.parseTime(Utils.FORMAT_DATE_TEMPLATE_FULL, record.getStartTime());
    }

    public static ContentValues recordEnd(long used, String remark) throws ParseException {
        ContentValues values = new ContentValues();
        String endTime = Utils.getDate(Utils.FORMAT_DATE_TEMPLATE_FULL);
        values.put(RecordEntry.COLUMN_NAME_USED_TIME, used);
        values.put(RecordEntry.COLUMN_NAME_END_TIME, endTime);
        values.put(RecordEntry.COLUMN_NAME_REMARK, remark);
        return values;
    }

    /** 为一个Episode生成一个ContentValues，用于插入数据库 */
    public static ContentValues episodeTo(@NonNull Episode episode) {
        ContentValues values = new ContentValues();
        values.put(EpisodeEntry.COLUMN_UUID, episode.getUUID());
        values.put(EpisodeEntry.COLUMN_NAME_RECORD_ID, episode.getRecordId());
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
        int id = cursor.getInt(QUERY_TASK_GROUP_PROJECTION._ID);
        int uuid = cursor.getInt(QUERY_TASK_GROUP_PROJECTION._UUID);
        String name = cursor.getString(QUERY_TASK_GROUP_PROJECTION.COLUMN_NAME_GROUP_NAME);
        String account = cursor.getString(QUERY_TASK_GROUP_PROJECTION.COLUMN_NAME_GROUP_ACCOUNT);
        int count = cursor.getInt(QUERY_TASK_GROUP_PROJECTION.COLUMN_NAME_COUNT);
        int running = cursor.getInt(QUERY_TASK_GROUP_PROJECTION.COLUMN_NAME_RUNNING);
        String extra1 = cursor.getString(QUERY_TASK_GROUP_PROJECTION.COLUMN_NAME_EXTRA_1);
        String extra2 = cursor.getString(QUERY_TASK_GROUP_PROJECTION.COLUMN_NAME_EXTRA_2);
        TaskGroup ret = new TaskGroup(id, uuid, name, account);
        ret.setCount(count).setRunningCount(running).setExtra_1(extra1).setExtra_2(extra2);
        return ret;
    }

    public static Task taskFrom(@NonNull Cursor cursor) {
        int id = cursor.getInt(QUERY_TASK_PROJECTION._ID);
        int uuid = cursor.getInt(QUERY_TASK_PROJECTION._UUID);
        int group_id = cursor.getInt(QUERY_TASK_PROJECTION.GROUP_ID);
        String group_name = cursor.getString(QUERY_TASK_PROJECTION.GROUP_NAME);
        TaskState state = stateFrom(cursor.getString(QUERY_TASK_PROJECTION.TASK_STATE));
        String name = cursor.getString(QUERY_TASK_PROJECTION.TASK_NAME);
        int type = cursor.getInt(QUERY_TASK_PROJECTION.TASK_TYPE);
        int isRunning = cursor.getInt(QUERY_TASK_PROJECTION.IS_RUNNING);
        String description = cursor.getString(QUERY_TASK_PROJECTION.TASK_DESCRIPTION);
        String start = cursor.getString(QUERY_TASK_PROJECTION.CREATE_TIME);
        String remark = cursor.getString(QUERY_TASK_PROJECTION.TASK_REMARK);
        long sum = cursor.getLong(QUERY_TASK_PROJECTION.TASK_ACCUMULATED_TIME);
        String extra1 = cursor.getString(QUERY_TASK_PROJECTION.EXTRA_1);
        String extra2 = cursor.getString(QUERY_TASK_PROJECTION.EXTRA_2);
        String extra3 = cursor.getString(QUERY_TASK_PROJECTION.EXTRA_3);
        String extra4 = cursor.getString(QUERY_TASK_PROJECTION.EXTRA_4);
        Task ret = new Task(group_id, group_name, id, uuid, type, isRunning, state, name, sum, description, start);
        return ret.setRemark(remark).setExtra1(extra1).setExtra2(extra2).setExtra3(extra3).setExtra4(extra4);
    }

    public static Record recordFrom(@NonNull Cursor cursor) {
        int id = cursor.getInt(QUERY_RECORD_PROJECTION._ID);
        int uuid = cursor.getInt(QUERY_RECORD_PROJECTION._UUID);
        int task_id = cursor.getInt(QUERY_RECORD_PROJECTION.COLUMN_NAME_TASK_ID);
        String task_name = cursor.getString(QUERY_RECORD_PROJECTION.COLUMN_NAME_TASK_NAME);
        int type = cursor.getInt(QUERY_RECORD_PROJECTION.COLUMN_NAME_REOCRD_TYPE);
        long used = cursor.getLong(QUERY_RECORD_PROJECTION.COLUMN_NAME_USED_TIME);
        String start = cursor.getString(QUERY_RECORD_PROJECTION.COLUMN_NAME_START_TIME);
        String end = cursor.getString(QUERY_RECORD_PROJECTION.COLUMN_NAME_END_TIME);
        String remark = cursor.getString(QUERY_RECORD_PROJECTION.COLUMN_NAME_REMARK);
        String extra1 = cursor.getString(QUERY_RECORD_PROJECTION.COLUMN_NAME_EXTRA_1);
        String extra2 = cursor.getString(QUERY_RECORD_PROJECTION.COLUMN_NAME_EXTRA_2);
        String extra3 = cursor.getString(QUERY_RECORD_PROJECTION.COLUMN_NAME_EXTRA_3);
        String extra4 = cursor.getString(QUERY_RECORD_PROJECTION.COLUMN_NAME_EXTRA_4);
        Record ret = new Record(id, uuid, task_id, task_name, type, used, start, end, remark);
        return ret.setExtra1(extra1).setExtra2(extra2).setExtra3(extra3).setExtra4(extra4);
    }

    public static Episode episodeFrom(@NonNull Cursor cursor) {
        int id = cursor.getInt(QUERY_EPISODE_PROJECTION._ID);
        int uuid = cursor.getInt(QUERY_EPISODE_PROJECTION._UUID);
        int record_id = cursor.getInt(QUERY_EPISODE_PROJECTION.COLUMN_NAME_RECORD_ID);
        String name = cursor.getString(QUERY_EPISODE_PROJECTION.COLUMN_NAME_EPISODE_NAME);
        int type = cursor.getInt(QUERY_EPISODE_PROJECTION.COLUMN_NAME_EPISODE_TYPE);
        String remark = cursor.getString(QUERY_EPISODE_PROJECTION.COLUMN_NAME_EPISODE_REMARK);
        String start = cursor.getString(QUERY_EPISODE_PROJECTION.COLUMN_NAME_EPISODE_START_TIME);
        int seq = cursor.getInt(QUERY_EPISODE_PROJECTION.COLUMN_NAME_EPISODE_SEQ);
        String extra1 = cursor.getString(QUERY_EPISODE_PROJECTION.COLUMN_NAME_EXTRA_1);
        String extra2 = cursor.getString(QUERY_EPISODE_PROJECTION.COLUMN_NAME_EXTRA_2);
        String extra3 = cursor.getString(QUERY_EPISODE_PROJECTION.COLUMN_NAME_EXTRA_3);
        String extra4 = cursor.getString(QUERY_EPISODE_PROJECTION.COLUMN_NAME_EXTRA_4);
        Episode ret = new Episode(id, uuid, record_id, type, name, remark, start, seq);
        return ret.setExtra1(extra1).setExtra2(extra2).setExtra3(extra3).setExtra4(extra4);
    }

    public static TaskState stateFrom(String state) {
        switch (state) {
            case "Activate":
                return TaskState.Activate;
            case "Completed":
                return TaskState.Completed;
            case "Recycled":
                return TaskState.Recycled;
            default:
                throw new IllegalArgumentException("unkown state: " + state);
        }
    }

    public static ContentValues taskToState(@NonNull TaskGroup group, @NonNull TaskState state) {
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_TASK_STATE, state.name());
        values.put(TaskEntry.COLUMN_NAME_GROUP_ID, group.getId());
        values.put(TaskEntry.COLUMN_NAME_GROUP_NAME, group.getName());
        return values;
    }

    public static ContentValues taskToCompleted() {
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_TASK_STATE, TaskState.Completed.name());
        values.put(TaskEntry.COLUMN_NAME_GROUP_ID, Const.COMPLETED_GROUP_ID);
        values.put(TaskEntry.COLUMN_NAME_GROUP_NAME, Const.DEFAULT_COMPLETED_GROUP);
        return values;
    }

    public static ContentValues taskToActivate(@NonNull TaskGroup group) {
        return taskToState(group, TaskState.Activate);
    }

    public static ContentValues taskToRecycled() {
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_TASK_STATE, TaskState.Recycled.name());
        values.put(TaskEntry.COLUMN_NAME_GROUP_ID, Const.RECYCLED_GROUP_ID);
        values.put(TaskEntry.COLUMN_NAME_GROUP_NAME, Const.DEFAULT_RECYCLED_GROUP);
        return values;
    }

    public static ContentValues taskToRunning(int isRunning) {
        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_IS_RUNNING, isRunning);
        return values;
    }

    public static TaskGroup clone(TaskGroup group) {
        TaskGroup ret = new TaskGroup(group.getId(), group.getUUID(), group.getName(), group.getAccount());
        ret.setCount(group.getTaskCount()).setRunningCount(group.getRunningCount())
                .setExtra_1(group.getExtra_1()).setExtra_2(group.getExtra_2());
        return ret;
    }

    public static Task clone(Task task) {
        Task ret = new Task(task.getGroupId(), task.getGroupName(), task.getId(), task.getUUID(),
                task.getType(), task.getRunning(), task.getState(), task.getName(), task.getSumTime(),
                task.getDescription(), task.getCreateTime());
        ret.setRemark(task.getRemark()).setExtra1(task.getExtra_1()).setExtra2(task.getExtra_2())
                .setExtra3(task.getExtra_3()).setExtra4(task.getExtra_4());
        return ret;
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

    public static int taskToColor(int taskType) {
        switch (taskType) {
            case Task.TYPE_MANUAL:
                return 0x88CE93D8;
            case Task.TYPE_TIMING:
                return 0x8872D572;
            case Task.TYPE_SHORT:
                return 0x88F48FB1;
            case Task.TYPE_LIMITED:
                return 0x8880DEEA;
            default:
                throw new IllegalArgumentException("unkown tast type: " + taskType);
        }
    }

    /** 用于更新Task时，生成一个临时的TaskGroup */
    public static TaskGroup mockTaskGroup(@NonNull Task task) {
        return new TaskGroup(task.getGroupId(), 0, task.getGroupName(), App.getInstance().getAccountName());
    }

    /** 根据Task类型获取Drawable资源，用于绘制Item颜色 */
    public static int taskToRes(@NonNull Task task) {
        return taskToRes(task.getType());
    }

    /** 为{@link wistcat.overtime.model.Entity}生成唯一ID */
    public static int createId() {
        return UUID.randomUUID().hashCode();
    }

}
