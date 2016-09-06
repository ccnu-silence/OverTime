package wistcat.overtime.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.HashMap;

import wistcat.overtime.data.db.TaskContract;
import wistcat.overtime.data.db.TaskDatabase;
import wistcat.overtime.data.db.TaskTableHelper;

import static wistcat.overtime.data.db.TaskContract.EpisodeEntry;
import static wistcat.overtime.data.db.TaskContract.RecordEntry;
import static wistcat.overtime.data.db.TaskContract.TaskEntry;
import static wistcat.overtime.data.db.TaskContract.TaskGroupEntry;

/**
 * @author wistcat
 */
public class TaskProvider extends ContentProvider {

    private static final String AUTHORITY = TaskContract.AUTHORITY;
    public static final int ROUTE_DELETE        = 100;
    public static final int ROUTE_TASKS         = 1;
    public static final int ROUTE_TASKS_ID      = 2;
    public static final int ROUTE_RECORDS       = 3;
    public static final int ROUTE_RECORDS_ID    = 4;
    public static final int ROUTE_EPISODES      = 5;
    public static final int ROUTE_EPISODES_ID   = 6;
    public static final int ROUTE_TASK_GROUPS   = 7;
    public static final int ROUTE_TASK_GROUPS_ID = 8;

    /* 提供列别名到真实列名的映射 TODO ... */
    private static HashMap<String, String> mProjectionMap = new HashMap<>();

    private static final UriMatcher mUriMatcher;
    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, "tasks/*", ROUTE_TASKS);
        mUriMatcher.addURI(AUTHORITY, "tasks/*/#", ROUTE_TASKS_ID);
        mUriMatcher.addURI(AUTHORITY, "records/*", ROUTE_TASKS);
        mUriMatcher.addURI(AUTHORITY, "records/*/#", ROUTE_TASKS_ID);
        mUriMatcher.addURI(AUTHORITY, "episodes/*", ROUTE_TASKS);
        mUriMatcher.addURI(AUTHORITY, "episodes/*/#", ROUTE_TASKS_ID);
        mUriMatcher.addURI(AUTHORITY, "taskgroups/*", ROUTE_TASK_GROUPS);
        mUriMatcher.addURI(AUTHORITY, "taskgroups/*/#", ROUTE_TASK_GROUPS_ID);
        mUriMatcher.addURI(AUTHORITY, "delete", ROUTE_DELETE);
    }

    private TaskDatabase mDatabase;

    public TaskProvider() {
    }

    @Override
    public boolean onCreate() {
        mDatabase = new TaskDatabase(getContext());
        return true;
    }

    /*
     * 单独的条目以及TaskGroup不需要提供selection和selectionArgs参数，
     * 其他更多匹配方式则在.../* 路径下进行，注意使用
     */
    @Override
    public Cursor query(@NonNull Uri uri, String[] columns, String selection,
                        String[] selectionArgs, String sortOrder) {
        String account;
        String table;
        String where = selection;
        String[] whereArgs = selectionArgs;

        switch (mUriMatcher.match(uri)) {
            case ROUTE_TASKS:
                account = getLastSeg(uri);
                table = TaskEntry.getTableName(account);
                break;
            case ROUTE_TASKS_ID:
                account = getSecondLastSeg(uri);
                table = TaskEntry.getTableName(account);
                where = TaskTableHelper.WHERE_TASK_ID;
                whereArgs = new String[]{getLastSeg(uri)};
                break;
            case ROUTE_RECORDS:
                account = getLastSeg(uri);
                table = RecordEntry.getTableName(account);
                break;
            case ROUTE_RECORDS_ID:
                account = getSecondLastSeg(uri);
                table = RecordEntry.getTableName(account);
                where = TaskTableHelper.WHERE_RECORD_ID;
                whereArgs = new String[]{getLastSeg(uri)};
                break;
            case ROUTE_EPISODES:
                account = getLastSeg(uri);
                table = EpisodeEntry.getTableName(account);
                break;
            case ROUTE_EPISODES_ID:
                account = getSecondLastSeg(uri);
                table = EpisodeEntry.getTableName(account);
                where = TaskTableHelper.WHERE_EPISODE_ID;
                whereArgs = new String[]{getLastSeg(uri)};
                break;
            case ROUTE_TASK_GROUPS:
                account = null;
                table = TaskGroupEntry.TABLE_NAME;
                where = TaskTableHelper.WHERE_TASK_GOUP_ACCOUNT;
                whereArgs = new String[]{getLastSeg(uri)};
                break;
            case ROUTE_TASK_GROUPS_ID:
                account = null;
                table = TaskGroupEntry.TABLE_NAME;
                where = TaskTableHelper.WHERE_TASK_GOUP_ID;
                whereArgs = new String[]{getLastSeg(uri)};
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        Cursor retCursor = mDatabase.query(
                account,
                table,
                columns,
                where,
                whereArgs,
                sortOrder
        );
        //
        Context ctx = getContext();
        assert ctx != null;
        retCursor.setNotificationUri(ctx.getContentResolver(), uri);
        return retCursor;
    }

    /* insert or update */
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri retUri;
        // 注意，values中必须包含各自的如task_id，record_id，episode_id的列
        switch (mUriMatcher.match(uri)) {
            case ROUTE_TASKS:
                retUri = mDatabase.insertTask(getLastSeg(uri), values);
                break;
            case ROUTE_TASKS_ID:
                throw new UnsupportedOperationException("Insert not supported on uri: " + uri);
            case ROUTE_RECORDS:
                retUri = mDatabase.insertRecord(getLastSeg(uri), values);
                break;
            case ROUTE_RECORDS_ID:
                throw new UnsupportedOperationException("Insert not supported on uri: " + uri);
            case ROUTE_EPISODES:
                retUri = mDatabase.insertEpisode(getLastSeg(uri), values);
                break;
            case ROUTE_EPISODES_ID:
                throw new UnsupportedOperationException("Insert not supported on uri: " + uri);
            case ROUTE_TASK_GROUPS:
                retUri = mDatabase.insertTaskGroup(getLastSeg(uri), values);
                break;
            case ROUTE_TASK_GROUPS_ID:
                throw new UnsupportedOperationException("Insert not supported on uri: " + uri);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // notify
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        // 返回的uri的结尾id为_ID，但是query的解析并不依赖于_ID而是自定义id，因此无法使用
        return retUri;
    }

    /*
     * 单独的条目不需要提供selection和selectionArgs参数，
     * 其他更多匹配方式则在.../* 路径下进行，注意使用
     */
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated;
        String account;
        String table;
        String where = selection;
        String[] whereArgs = selectionArgs;
        switch (mUriMatcher.match(uri)) {
            case ROUTE_TASKS:
                account = getLastSeg(uri);
                table = TaskEntry.getTableName(account);
                break;
            case ROUTE_TASKS_ID:
                account = getSecondLastSeg(uri);
                table = TaskEntry.getTableName(account);
                where = TaskTableHelper.WHERE_TASK_ID;
                whereArgs = new String[]{getLastSeg(uri)};
                break;
            case ROUTE_RECORDS:
                account = getLastSeg(uri);
                table = RecordEntry.getTableName(account);
                break;
            case ROUTE_RECORDS_ID:
                account = getSecondLastSeg(uri);
                table = RecordEntry.getTableName(account);
                where = TaskTableHelper.WHERE_RECORD_ID;
                whereArgs = new String[]{getLastSeg(uri)};
                break;
            case ROUTE_EPISODES:
                account = getLastSeg(uri);
                table = EpisodeEntry.getTableName(account);
                break;
            case ROUTE_EPISODES_ID:
                account = getSecondLastSeg(uri);
                table = EpisodeEntry.getTableName(account);
                where = TaskTableHelper.WHERE_EPISODE_ID;
                whereArgs = new String[]{getLastSeg(uri)};
                break;
            case ROUTE_TASK_GROUPS:
                table = TaskGroupEntry.TABLE_NAME;
                break;
            case ROUTE_TASK_GROUPS_ID:
                table = TaskGroupEntry.TABLE_NAME;
                where = TaskTableHelper.WHERE_TASK_GOUP_ID;
                whereArgs = new String[]{getLastSeg(uri)};
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        rowsUpdated = mDatabase.update(table, values, where, whereArgs);
        // notify
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return rowsUpdated;
    }

    /*
     * 单独的条目不需要提供selection和selectionArgs参数，
     * 其他更多匹配方式则在.../* 路径下进行，注意使用
     */
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted;
        String account;
        String table;
        String where = selection;
        String[] whereArgs = selectionArgs;
        switch (mUriMatcher.match(uri)) {
            case ROUTE_TASKS:
                account = getLastSeg(uri);
                table = TaskEntry.getTableName(account);
                break;
            case ROUTE_TASKS_ID:
                account = getSecondLastSeg(uri);
                table = TaskEntry.getTableName(account);
                where = TaskTableHelper.WHERE_TASK_ID;
                whereArgs = new String[]{getLastSeg(uri)};
                break;
            case ROUTE_RECORDS:
                account = getLastSeg(uri);
                table = RecordEntry.getTableName(account);
                break;
            case ROUTE_RECORDS_ID:
                account = getSecondLastSeg(uri);
                table = RecordEntry.getTableName(account);
                where = TaskTableHelper.WHERE_RECORD_ID;
                whereArgs = new String[]{getLastSeg(uri)};
                break;
            case ROUTE_EPISODES:
                account = getLastSeg(uri);
                table = EpisodeEntry.getTableName(account);
                break;
            case ROUTE_EPISODES_ID:
                account = getSecondLastSeg(uri);
                table = EpisodeEntry.getTableName(account);
                where = TaskTableHelper.WHERE_EPISODE_ID;
                whereArgs = new String[]{getLastSeg(uri)};
                break;
            case ROUTE_TASK_GROUPS:
                table = TaskGroupEntry.TABLE_NAME;
                break;
            case ROUTE_TASK_GROUPS_ID:
                table = TaskGroupEntry.TABLE_NAME;
                where = TaskTableHelper.WHERE_TASK_GOUP_ID;
                whereArgs = new String[]{getLastSeg(uri)};
                break;
            case ROUTE_DELETE:
                /* 删除与账户相关的表 Caution！！ */
                mDatabase.deleteTables(getLastSeg(uri));
                return 0;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        rowsDeleted = mDatabase.delete(table, where, whereArgs);
        // notify
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return rowsDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case ROUTE_TASKS:
                return TaskEntry.CONTENT_TYPE;
            case ROUTE_TASKS_ID:
                return TaskEntry.CONTENT_TYPE_ITEM;
            case ROUTE_RECORDS:
                return RecordEntry.CONTENT_TYPE;
            case ROUTE_RECORDS_ID:
                return RecordEntry.CONTENT_TYPE_ITEM;
            case ROUTE_EPISODES:
                return EpisodeEntry.CONTENT_TYPE;
            case ROUTE_EPISODES_ID:
                return EpisodeEntry.CONTENT_TYPE_ITEM;
            case ROUTE_TASK_GROUPS:
                return TaskGroupEntry.CONTENT_TYPE;
            case ROUTE_TASK_GROUPS_ID:
                return TaskGroupEntry.CONTENT_TYPE_ITEM;
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }
    }

    private String getLastSeg(Uri uri) {
        return uri.getLastPathSegment();
    }

    private String getSecondLastSeg(Uri uri) {
        return uri.getPathSegments().get(1);
    }

}
