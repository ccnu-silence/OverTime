package wistcat.overtime.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import wistcat.overtime.util.Const;

import static wistcat.overtime.data.db.TaskContract.EpisodeEntry;
import static wistcat.overtime.data.db.TaskContract.RecordEntry;
import static wistcat.overtime.data.db.TaskContract.TaskEntry;
import static wistcat.overtime.data.db.TaskContract.TaskGroupEntry;

/**
 * Task 数据库
 *
 * @author wistcat 2016/8/28
 */
public class TaskDatabase {

    private static final String DB_NAME = "Task_Local.db";

    private DatabaseHelper mDatabaseHelper;

    public TaskDatabase(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    /** 查询 */
    public Cursor query(String account, String table,String[] columns, String selection,
                        String[] SelectionArgs, String orderBy) {
        return query(account, table, columns, selection, SelectionArgs, null, null, orderBy);
    }

    /** 查询 */
    public Cursor query(String account, String table, String[] columns, String selection,
                        String[] SelectionArgs, String groupBy, String having, String orderBy) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        if (!account.equals(Const.ACCOUNT_GUEST)) {
            TaskTableHelper.createTables(db, account);
        }

        return db.query(table, columns, selection, SelectionArgs, groupBy, having, orderBy);
    }

    /** 插入一条Task */
    public Uri insertTask(String account, ContentValues values) {
        return insertTask(account, null, values);
    }

    /**
     * 插入一条Task，values中需要指定uuid列
     * <br/> 先检查条目是否存在，再选择更新或者插入
     *
     * @return 返回的uri以_ID结尾
     */
    public Uri insertTask(String account, String nullColumnHack, ContentValues values) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String table = TaskEntry.getTableName(account);
        Uri ret;
        Cursor cursor = db.query(
                table,
                new String[]{TaskEntry.COLUMN_UUID},
                TaskTableHelper.WHERE_UUID,
                new String[]{values.getAsString(TaskEntry.COLUMN_UUID)},
                null,
                null,
                null
        );

        long _id;
        if (cursor.moveToLast()) { // return false if empty
            _id = db.update(table, values, TaskTableHelper.WHERE_UUID,
                    new String[]{values.getAsString(TaskEntry.COLUMN_UUID)});
        } else {
            _id = db.insert(table, nullColumnHack, values);
        }
        // rowid 自增
        if (_id < 0) {
            throw new android.database.SQLException("Failed to insert row");
        }
        ret = TaskContract.buildTasksUriWith(account, _id);
        cursor.close();
        return ret;
    }

    /** 插入一条Record */
    public Uri insertRecord(String account, ContentValues values) {
        return insertRecord(account, null, values);
    }

    /**
     * 插入一条Record
     * <br/> 先检查条目是否存在，再选择更新或者插入
     */
    public Uri insertRecord(String account, String nullColumnHack, ContentValues values) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String table = RecordEntry.getTableName(account);
        Uri ret;
        Cursor cursor = db.query(
                table,
                new String[]{RecordEntry.COLUMN_UUID},
                TaskTableHelper.WHERE_UUID,
                new String[]{values.getAsString(RecordEntry.COLUMN_UUID)},
                null,
                null,
                null
        );

        long _id;
        if (cursor.moveToLast()) { // return false if empty
            _id = db.update(table, values, TaskTableHelper.WHERE_UUID,
                    new String[]{values.getAsString(RecordEntry.COLUMN_UUID)});
        } else {
            _id = db.insert(table, nullColumnHack, values);
        }
        // rowid 自增
        if (_id < 0) {
            throw new android.database.SQLException("Failed to insert row");
        }
        ret = TaskContract.buildRecordsUriWith(account, _id);
        cursor.close();
        return ret;
    }

    /** 插入一条Episode */
    public Uri insertEpisode(String account, ContentValues values) {
        return insertEpisode(account, null, values);
    }

    /**
     * 插入一条Episode
     * <br/> 先检查条目是否存在，再选择更新或者插入
     */
    public Uri insertEpisode(String account, String nullColumnHack, ContentValues values) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        String table = EpisodeEntry.getTableName(account);
        Uri ret;
        Cursor cursor = db.query(
                table,
                new String[]{EpisodeEntry.COLUMN_UUID},
                TaskTableHelper.WHERE_UUID,
                new String[]{values.getAsString(EpisodeEntry.COLUMN_UUID)},
                null,
                null,
                null
        );

        long _id;
        if (cursor.moveToLast()) { // return false if empty
            _id = db.update(table, values, TaskTableHelper.WHERE_UUID,
                    new String[]{values.getAsString(EpisodeEntry.COLUMN_UUID)}
            );
        } else {
            _id = db.insert(table, nullColumnHack, values);
        }
        // rowid 自增
        if (_id < 0) {
            throw new android.database.SQLException("Failed to insert row");
        }
        ret = TaskContract.buildEpisodesUriWith(account, _id);
        cursor.close();
        return ret;
    }

    /**
     * 插入一条TaskGroup
     */
    public Uri insertTaskGroup(String account, ContentValues values) {
        return insertTaskGroup(account, null, values);
    }

    /**
     * 插入一条TaskGroup
     * <br/> 先检查条目是否存在，再选择更新或者插入
     */
    public Uri insertTaskGroup(String account, String nullColumnHack, ContentValues values) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        Uri ret;
        String table = TaskGroupEntry.getTableName(account);
        Cursor cursor = db.query(
                table,
                new String[]{TaskGroupEntry.COLUMN_UUID},
                TaskTableHelper.WHERE_UUID,
                new String[]{values.getAsString(TaskGroupEntry.COLUMN_UUID)},
                null,
                null,
                null
        );

        long _id;
        if (cursor.moveToLast()) { // return false if empty
            _id = db.update(table, values, TaskTableHelper.WHERE_UUID,
                    new String[]{values.getAsString(TaskGroupEntry.COLUMN_UUID)}
            );
        } else {
            _id = db.insert(table, nullColumnHack, values);
        }
        // rowid 自增
        if (_id < 0) {
            throw new android.database.SQLException("Failed to insert row");
        }
        ret = TaskContract.buildTaskGroupUriWith(account, _id);
        cursor.close();
        return ret;
    }

    /** 更新条目 */
    public int update(String table, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        return db.update(table, values, where, whereArgs);
    }

    public void update(String sentence) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.execSQL(sentence);
    }

    /** 删除条目 */
    public int delete(String table, String where, String[] whereArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        return db.delete(table, where, whereArgs);
    }

    /** 删除账户相关的表 */
    public void deleteTables(String account) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        TaskTableHelper.deleteTables(db, account);
    }

    /* */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        public static final int DB_VERSION = 2;

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            TaskTableHelper.createTables(sqLiteDatabase, Const.ACCOUNT_GUEST);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            TaskTableHelper.deleteTables(sqLiteDatabase, Const.ACCOUNT_GUEST);
            onCreate(sqLiteDatabase);
        }
    }

}
