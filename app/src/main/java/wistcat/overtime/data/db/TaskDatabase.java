package wistcat.overtime.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import wistcat.overtime.util.Const;

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
        TaskTableHelper.createTables(db, account);
        return db.query(table, columns, selection, SelectionArgs, groupBy, having, orderBy);
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        return mDatabaseHelper.getWritableDatabase().insert(table, nullColumnHack, values);
    }

    public int update() {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        // FIXME
        return -1;
    }

    public int delete() {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        // FIXME
        return -1;
    }

    public void deleteTables(String account) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        TaskTableHelper.deleteTables(db, account);
    }

    /* */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        public static final int DB_VERSION = 1;

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
