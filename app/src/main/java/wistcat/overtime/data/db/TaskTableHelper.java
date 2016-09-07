package wistcat.overtime.data.db;

import android.database.sqlite.SQLiteDatabase;

import wistcat.overtime.model.TaskState;

import static wistcat.overtime.data.db.TaskContract.EpisodeEntry;
import static wistcat.overtime.data.db.TaskContract.RecordEntry;
import static wistcat.overtime.data.db.TaskContract.TaskEntry;
import static wistcat.overtime.data.db.TaskContract.TaskGroupEntry;

/**
 * 表操作语句
 *
 * @author wistcat 2016/8/30
 */
public final class TaskTableHelper {

    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String COMMA_SEP = ",";

    /** 创建TaskGroup表语句 */
    public static final String CREATE_TASK_GROUP_TABLE =
            "CREATE TABLE IF NOT EXISTS %s" + TaskGroupEntry.TABLE_NAME + " (" +
                    TaskGroupEntry._ID + " INTEGER PRIMARY KEY," +
                    TaskGroupEntry.COLUMN_NAME_GROUP_ID + TYPE_INTEGER + COMMA_SEP +
                    TaskGroupEntry.COLUMN_NAME_GROUP_NAME +  TYPE_TEXT + COMMA_SEP +
                    TaskGroupEntry.COLUMN_NAME_GROUP_ACCOUNT + TYPE_TEXT + COMMA_SEP +
                    TaskGroupEntry.COLUMN_NAME_COUNT_ACTIVATE + TYPE_INTEGER + COMMA_SEP +
                    TaskGroupEntry.COLUMN_NAME_COUNT_RUNNING + TYPE_INTEGER + COMMA_SEP +
                    TaskGroupEntry.COLUMN_NAME_COUNT_COMPLETED + TYPE_INTEGER + COMMA_SEP +
                    TaskGroupEntry.COLUMN_NAME_COUNT_RECYCLED + TYPE_INTEGER + ")";

    /** 创建任务表语句 */
    private static final String CREATE_TASK_TABLE =
            "CREATE TABLE IF NOT EXISTS %s" + TaskEntry.TABLE_NAME + " (" +
                    TaskEntry._ID + " INTEGER PRIMARY KEY," +
                    TaskEntry.COLUMN_NAME_GROUP_ID + TYPE_INTEGER + COMMA_SEP +
                    TaskEntry.COLUMN_NAME_TASK_STATE + TYPE_TEXT + COMMA_SEP +
                    TaskEntry.COLUMN_NAME_TASK_ID + TYPE_INTEGER + COMMA_SEP +
                    TaskEntry.COLUMN_NAME_TASK_NAME + TYPE_TEXT + COMMA_SEP +
                    TaskEntry.COLUMN_NAME_TASK_TYPE + TYPE_INTEGER + COMMA_SEP +
                    TaskEntry.COLUMN_NAME_DESCREPTION + TYPE_TEXT + COMMA_SEP +
                    TaskEntry.COLUMN_NAME_REMARK + TYPE_TEXT + COMMA_SEP +
                    TaskEntry.COLUMN_NAME_COMPLETED_DEGREE + TYPE_INTEGER + COMMA_SEP +
                    TaskEntry.COLUMN_NAME_ACCUMULATED_TIME +  TYPE_INTEGER + COMMA_SEP +
                    TaskEntry.COLUMN_NAME_EXTRA_1 + TYPE_TEXT + COMMA_SEP +
                    TaskEntry.COLUMN_NAME_EXTRA_2 + TYPE_TEXT + COMMA_SEP +
                    TaskEntry.COLUMN_NAME_EXTRA_3 + TYPE_TEXT + COMMA_SEP +
                    TaskEntry.COLUMN_NAME_EXTRA_4 + TYPE_TEXT + ")";

    /** 创建记录表语句 */
    private static final String CREATE_RECORD_TABLE =
            "CREATE TABLE IF NOT EXISTS %s" + RecordEntry.TABLE_NAME + " (" +
                    RecordEntry._ID + " INTEGER PRIMARY KEY," +
                    RecordEntry.COLUMN_NAME_TASK_ID + TYPE_INTEGER + COMMA_SEP +
                    RecordEntry.COLUMN_NAME_RECORD_ID + TYPE_INTEGER + COMMA_SEP +
                    RecordEntry.COLUMN_NAME_REOCRD_TYPE + TYPE_INTEGER + COMMA_SEP +
                    RecordEntry.COLUMN_NAME_USED_TIME + TYPE_INTEGER + COMMA_SEP +
                    RecordEntry.COLUMN_NAME_START_TIME + TYPE_INTEGER + COMMA_SEP +
                    RecordEntry.COLUMN_NAME_END_TIME + TYPE_INTEGER + COMMA_SEP +
                    RecordEntry.COLUMN_NAME_REMARK + TYPE_TEXT + COMMA_SEP +
                    RecordEntry.COLUMN_NAME_EXTRA_1 + TYPE_TEXT + COMMA_SEP +
                    RecordEntry.COLUMN_NAME_EXTRA_2 + TYPE_TEXT + COMMA_SEP +
                    RecordEntry.COLUMN_NAME_EXTRA_3 + TYPE_TEXT + COMMA_SEP +
                    RecordEntry.COLUMN_NAME_EXTRA_4 + TYPE_TEXT + ")";

    /** 创建Episode表语句 */
    private static final String CREATE_EPISODE_TABLE =
            "CREATE TABLE IF NOT EXISTS %s" + EpisodeEntry.TABLE_NAME + " (" +
                    EpisodeEntry._ID + " INTEGER PRIMARY KEY," +
                    EpisodeEntry.COLUMN_NAME_RECORD_ID + TYPE_INTEGER + COMMA_SEP +
                    EpisodeEntry.COLUMN_NAME_EPISODE_ID + TYPE_INTEGER + COMMA_SEP +
                    EpisodeEntry.COLUMN_NAME_EPISODE_NAME + TYPE_TEXT + COMMA_SEP +
                    EpisodeEntry.COLUMN_NAME_EPISODE_TYPE + TYPE_INTEGER + COMMA_SEP +
                    EpisodeEntry.COLUMN_NAME_EPISODE_REMARK + TYPE_TEXT + COMMA_SEP +
                    EpisodeEntry.COLUMN_NAME_EPISODE_START_TIME + TYPE_INTEGER + COMMA_SEP +
                    EpisodeEntry.COLUMN_NAME_EPISODE_SEQ + TYPE_INTEGER + COMMA_SEP +
                    EpisodeEntry.COLUMN_NAME_EXTRA_1 + TYPE_TEXT + COMMA_SEP +
                    EpisodeEntry.COLUMN_NAME_EXTRA_2 + TYPE_TEXT + COMMA_SEP +
                    EpisodeEntry.COLUMN_NAME_EXTRA_3 + TYPE_TEXT + COMMA_SEP +
                    EpisodeEntry.COLUMN_NAME_EXTRA_4 + TYPE_TEXT + ")";

    /** 删除TaskGroup表语句 */
    public static final String DELETE_TASK_GROUP_TABLE = "DROP IF EXISTS %s" + TaskGroupEntry.TABLE_NAME;
    /** 删除任务表语句 */
    private static final String DELETE_TASK_TABLE = "DROP IF EXISTS %s" + TaskEntry.TABLE_NAME;
    /** 删除记录表语句 */
    private static final String DELETE_RECORD_TABLE = "DROP IF EXISTS %s" + RecordEntry.TABLE_NAME;
    /** 删除Episode表语句 */
    private static final String DELETE_EPISODE_TABLE = "DROP IF EXISTS %s" + EpisodeEntry.TABLE_NAME;

    /** 匹配group_id的where语句 */
    public static final String WHERE_TASK_GOUP_ID = TaskGroupEntry.COLUMN_NAME_GROUP_ID + " = ?";
    /** 匹配task_id的where语句 */
    public static final String WHERE_TASK_ID = TaskEntry.COLUMN_NAME_TASK_ID + " = ?";
    /** 匹配record_id的where语句 */
    public static final String WHERE_RECORD_ID = RecordEntry.COLUMN_NAME_RECORD_ID + " = ?";
    /** 匹配episode_id的where语句 */
    public static final String WHERE_EPISODE_ID = EpisodeEntry.COLUMN_NAME_EPISODE_ID + " = ?";

    /** 匹配taskState的where语句*/
    public static final String WHERE_TASK_STATE = TaskEntry.COLUMN_NAME_TASK_STATE + " = ?";

    /** whereArgs: TastState == Completed */
    public static final String[] WHERE_TASK_STATE_COMLETED = new String[] {
            TaskState.Completed.name()
    };

    /** whereArgs: TastState == Activate */
    public static final String[] WHERE_TASK_STATE_ACTIVATE = new String[] {
            TaskState.Activate.name()
    };

    /** whereArgs: TastState == Running */
    public static final String[] WHERE_TASK_STATE_RUNNING = new String[] {
            TaskState.Running.name()
    };
    /** whereArgs: TastState == Recycled */
    public static final String[] WHERE_TASK_STATE_Recycled = new String[] {
            TaskState.Recycled.name()
    };

    /** 全部TaskGroup列名 */
    public static final String[] TASK_GROUP_PROJECTION = new String[] {
            TaskGroupEntry._ID,
            TaskGroupEntry.COLUMN_NAME_GROUP_ID,
            TaskGroupEntry.COLUMN_NAME_GROUP_NAME,
            TaskGroupEntry.COLUMN_NAME_GROUP_ACCOUNT,
            TaskGroupEntry.COLUMN_NAME_COUNT_ACTIVATE,
            TaskGroupEntry.COLUMN_NAME_COUNT_RUNNING,
            TaskGroupEntry.COLUMN_NAME_COUNT_COMPLETED,
            TaskGroupEntry.COLUMN_NAME_COUNT_RECYCLED
    };

    /** 全部Task列名 */
    public static final String[] TASK_PROJECTION = new String[] {
            TaskEntry._ID,
            TaskEntry.COLUMN_NAME_GROUP_ID,
            TaskEntry.COLUMN_NAME_TASK_STATE,
            TaskEntry.COLUMN_NAME_TASK_ID,
            TaskEntry.COLUMN_NAME_TASK_NAME,
            TaskEntry.COLUMN_NAME_TASK_TYPE,
            TaskEntry.COLUMN_NAME_DESCREPTION,
            TaskEntry.COLUMN_NAME_REMARK,
            TaskEntry.COLUMN_NAME_COMPLETED_DEGREE,
            TaskEntry.COLUMN_NAME_ACCUMULATED_TIME,
            TaskEntry.COLUMN_NAME_EXTRA_1,
            TaskEntry.COLUMN_NAME_EXTRA_2,
            TaskEntry.COLUMN_NAME_EXTRA_3,
            TaskEntry.COLUMN_NAME_EXTRA_4
    };

    /** 全部Record列名 */
    public static final String[] RECORD_PROJECTION = new String[] {
            RecordEntry._ID,
            RecordEntry.COLUMN_NAME_TASK_ID,
            RecordEntry.COLUMN_NAME_RECORD_ID,
            RecordEntry.COLUMN_NAME_REOCRD_TYPE,
            RecordEntry.COLUMN_NAME_USED_TIME,
            RecordEntry.COLUMN_NAME_START_TIME,
            RecordEntry.COLUMN_NAME_END_TIME,
            RecordEntry.COLUMN_NAME_REMARK,
            RecordEntry.COLUMN_NAME_EXTRA_1,
            RecordEntry.COLUMN_NAME_EXTRA_2,
            RecordEntry.COLUMN_NAME_EXTRA_3,
            RecordEntry.COLUMN_NAME_EXTRA_4
    };

    /** 全部Episode列名 */
    public static final String[] EPISODE_PROJECTION = new String[] {
            EpisodeEntry._ID,
            EpisodeEntry.COLUMN_NAME_RECORD_ID,
            EpisodeEntry.COLUMN_NAME_EPISODE_ID,
            EpisodeEntry.COLUMN_NAME_EPISODE_NAME,
            EpisodeEntry.COLUMN_NAME_EPISODE_TYPE,
            EpisodeEntry.COLUMN_NAME_EPISODE_REMARK,
            EpisodeEntry.COLUMN_NAME_EPISODE_START_TIME,
            EpisodeEntry.COLUMN_NAME_EPISODE_SEQ,
            EpisodeEntry.COLUMN_NAME_EXTRA_1,
            EpisodeEntry.COLUMN_NAME_EXTRA_2,
            EpisodeEntry.COLUMN_NAME_EXTRA_3,
            EpisodeEntry.COLUMN_NAME_EXTRA_4
    };

    /** 根据账户名创建表 */
    public static void createTables(SQLiteDatabase db, String account) {
        db.execSQL(String.format(CREATE_TASK_GROUP_TABLE, account));
        db.execSQL(String.format(CREATE_TASK_TABLE, account));
        db.execSQL(String.format(CREATE_RECORD_TABLE, account));
        db.execSQL(String.format(CREATE_EPISODE_TABLE, account));
    }

    /** 根据账户名删除表 */
    public static void deleteTables(SQLiteDatabase db, String account) {
        db.execSQL(String.format(DELETE_TASK_GROUP_TABLE, account));
        db.execSQL(String.format(DELETE_TASK_TABLE, account));
        db.execSQL(String.format(DELETE_RECORD_TABLE, account));
        db.execSQL(String.format(DELETE_EPISODE_TABLE, account));
    }

    public static String[] createSearchSelection(String search) {
        return new String[]{"%" + search + "%"};
    }

    public interface QUERY_TASK_GROUP_PROJECTION {
        int _ID = 0;
        int COLUMN_NAME_GROUP_ID = 1;
        int COLUMN_NAME_GROUP_NAME = 2;
        int COLUMN_NAME_GROUP_ACCOUNT = 3;
        int COLUMN_NAME_COUNT_ACTIVATE = 4;
        int COLUMN_NAME_COUNT_RUNNING = 5;
        int COLUMN_NAME_COUNT_COMPLETED = 6;
        int COLUMN_NAME_COUNT_RECYCLED = 7;
    }

    /** 返回{@link TaskTableHelper#TASK_PROJECTION}中查询的列号，用于结果Cursor的查询 */
    public interface QUERY_TASK_PROJECTION {
        int _ID = 0;
        int GROUP_ID = 1;
        int TASK_STATE = 2;
        int TASK_ID = 3;
        int TASK_NAME = 4;
        int TASK_TYPE = 5;
        int TASK_DESCRIPTION = 6;
        int TASK_REMARK = 7;
        int TASK_COMPLETED_DEGREE = 8;
        int TASK_ACCUMULATED_TIME = 9;
        int EXTRA_1 = 10;
        int EXTRA_2 = 11;
        int EXTRA_3 = 12;
        int EXTRA_4 = 13;
    }

    public interface QUERY_RECORD_PROJECTION {
        int _ID = 0;
        int COLUMN_NAME_TASK_ID = 1;
        int COLUMN_NAME_RECORD_ID = 2;
        int COLUMN_NAME_REOCRD_TYPE = 3;
        int COLUMN_NAME_USED_TIME = 4;
        int COLUMN_NAME_START_TIME = 5;
        int COLUMN_NAME_END_TIME = 6;
        int COLUMN_NAME_REMARK = 7;
        int COLUMN_NAME_EXTRA_1 = 8;
        int COLUMN_NAME_EXTRA_2 = 9;
        int COLUMN_NAME_EXTRA_3 = 10;
        int COLUMN_NAME_EXTRA_4 = 11;
    }

    public interface QUERY_EPISODE_PROJECTION {
        int _ID = 0;
        int COLUMN_NAME_RECORD_ID = 1;
        int COLUMN_NAME_EPISODE_ID = 2;
        int COLUMN_NAME_EPISODE_NAME = 3;
        int COLUMN_NAME_EPISODE_TYPE = 4;
        int COLUMN_NAME_EPISODE_REMARK = 5;
        int COLUMN_NAME_EPISODE_START_TIME = 6;
        int COLUMN_NAME_EPISODE_SEQ = 7;
        int COLUMN_NAME_EXTRA_1 = 8;
        int COLUMN_NAME_EXTRA_2 = 9;
        int COLUMN_NAME_EXTRA_3 = 10;
        int COLUMN_NAME_EXTRA_4 = 11;
    }

}
