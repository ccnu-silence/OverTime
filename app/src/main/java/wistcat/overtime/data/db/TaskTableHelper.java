package wistcat.overtime.data.db;

import android.database.sqlite.SQLiteDatabase;

import static wistcat.overtime.data.db.TaskContract.TaskEntry;
import static wistcat.overtime.data.db.TaskContract.RecordEntry;

/**
 * @author wistcat 2016/8/30
 */
public final class TaskTableHelper {

    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String COMMA_SEP = ",";

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
                    TaskEntry.COLUMN_NAME_EXTRA_4 + TYPE_TEXT + COMMA_SEP + ")";

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
                    RecordEntry.COLUMN_NAME_EXTRA_4 + TYPE_TEXT + COMMA_SEP + ")";

    // TODO: Episode table...


    /** 删除任务表语句 */
    private static final String DELETE_TASK_TABLE = "DROP IF EXISTS %s" + TaskContract.TaskEntry.TABLE_NAME;
    /** 删除记录表语句 */
    private static final String DELETE_RECORD_TABLE = "DROP IF EXISTS %s" + TaskContract.RecordEntry.TABLE_NAME;

    /** 根据账户名创建表 */
    public static void createTables(SQLiteDatabase db, String accountName) {
        db.execSQL(String.format(CREATE_TASK_TABLE, accountName));
        db.execSQL(String.format(CREATE_RECORD_TABLE, accountName));
        // TODO: Episode table
    }

    /** 根据账户名删除表 */
    public static void deleteTables(SQLiteDatabase db, String accountName) {
        db.execSQL(String.format(DELETE_TASK_TABLE, accountName));
        db.execSQL(String.format(DELETE_RECORD_TABLE, accountName));
    }

    public static String[] createSearchSelection(String search) {
        return new String[]{"%" + search + "%"};
    }



}
