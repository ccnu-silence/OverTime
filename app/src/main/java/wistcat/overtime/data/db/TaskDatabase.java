package wistcat.overtime.data.db;

import android.content.Context;
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
