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

import static wistcat.overtime.data.db.TaskContract.EpisodeEntry;
import static wistcat.overtime.data.db.TaskContract.RecordEntry;
import static wistcat.overtime.data.db.TaskContract.TaskEntry;

/**
 * @author wistcat
 */
public class TaskProvider extends ContentProvider {

    private static final String AUTHORITY = TaskContract.AUTHORITY;
    public static final int ROUTE_DELETE        = -1;
    public static final int ROUTE_TASKS         = 1;
    public static final int ROUTE_TASKS_ID      = 2;
    public static final int ROUTE_RECORDS       = 3;
    public static final int ROUTE_RECORDS_ID    = 4;
    public static final int ROUTE_EPISODES      = 5;
    public static final int ROUTE_EPISODES_ID   = 6;

    /* 提供列别名到真实列名的映射 TODO ... */
    private static HashMap<String, String> mProjectionMap = new HashMap<>();

    private static final UriMatcher mUriMatcher;
    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, "delete", ROUTE_DELETE);
        mUriMatcher.addURI(AUTHORITY, "tasks/*", ROUTE_TASKS);
        mUriMatcher.addURI(AUTHORITY, "tasks/*/#", ROUTE_TASKS_ID);
        mUriMatcher.addURI(AUTHORITY, "records/*", ROUTE_TASKS);
        mUriMatcher.addURI(AUTHORITY, "records/*/#", ROUTE_TASKS_ID);
        mUriMatcher.addURI(AUTHORITY, "episodes/*", ROUTE_TASKS);
        mUriMatcher.addURI(AUTHORITY, "episodes/*/#", ROUTE_TASKS_ID);
    }


    private TaskDatabase mDatabase;

    public TaskProvider() {

    }

    @Override
    public boolean onCreate() {
        mDatabase = new TaskDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] columns, String selection,
                        String[] selectionArgs, String sortOrder) {

        String account;
        String table;
        String select = selection;
        String[] where = selectionArgs;

        switch (mUriMatcher.match(uri)) {
            case ROUTE_TASKS:
                account = getLastSeg(uri);
                table = TaskEntry.getTableName(account);
                break;
            case ROUTE_TASKS_ID:
                account = getSecondLastSeg(uri);
                table = TaskEntry.getTableName(account);
                select = TaskEntry.COLUMN_NAME_TASK_ID + " = ?";
                where = new String[]{getLastSeg(uri)};
                break;
            case ROUTE_RECORDS:
                account = getLastSeg(uri);
                table = RecordEntry.getTableName(account);
                break;
            case ROUTE_RECORDS_ID:
                account = getSecondLastSeg(uri);
                table = RecordEntry.getTableName(account);
                select = RecordEntry.COLUMN_NAME_RECORD_ID + " = ?";
                where = new String[]{getLastSeg(uri)};
                break;
            case ROUTE_EPISODES:
                account = getLastSeg(uri);
                table = EpisodeEntry.getTableName(account);
                break;
            case ROUTE_EPISODES_ID:
                account = getSecondLastSeg(uri);
                table = EpisodeEntry.getTableName(account);
                select = EpisodeEntry.COLUMN_NAME_EPISODE_ID + " = ?";
                where = new String[]{getLastSeg(uri)};
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        Cursor retCursor = mDatabase.query(
                account,
                table,
                columns,
                select,
                where,
                sortOrder
        );

        Context ctx = getContext();
        assert ctx != null;
        retCursor.setNotificationUri(ctx.getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        Uri retUri = null;

        switch (mUriMatcher.match(uri)) {
            case ROUTE_TASKS:
                break;
            case ROUTE_TASKS_ID:
                throw new UnsupportedOperationException("Insert not supported on uri: " + uri);
            case ROUTE_RECORDS:
                break;
            case ROUTE_RECORDS_ID:
                throw new UnsupportedOperationException("Insert not supported on uri: " + uri);
            case ROUTE_EPISODES:
                break;
            case ROUTE_EPISODES_ID:
                throw new UnsupportedOperationException("Insert not supported on uri: " + uri);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // notify
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return retUri;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int rowsUpdated = -1;

        switch (mUriMatcher.match(uri)) {
            case ROUTE_TASKS:
                break;
            case ROUTE_TASKS_ID:
                break;
            case ROUTE_RECORDS:
                break;
            case ROUTE_RECORDS_ID:
                break;
            case ROUTE_EPISODES:
                break;
            case ROUTE_EPISODES_ID:
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // notify
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return rowsUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        int rowsDeleted = -1;

        switch (mUriMatcher.match(uri)) {
            case ROUTE_DELETE:
                /* 删除与账户相关的表 Caution！！ */
                mDatabase.deleteTables(getLastSeg(uri));
                break;
            case ROUTE_TASKS:
                break;
            case ROUTE_TASKS_ID:
                break;
            case ROUTE_RECORDS:
                break;
            case ROUTE_RECORDS_ID:
                break;
            case ROUTE_EPISODES:
                break;
            case ROUTE_EPISODES_ID:
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

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
