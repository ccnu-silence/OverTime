package wistcat.overtime.data.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

/**
 * 表名和域名常量，用于{@link wistcat.overtime.contentprovider.TaskProvider}
 * <br/>每个账户三张表：task、record、episode
 *
 * @author wistcat 2016/8/28
 */
public final class TaskContract {

    /** ContentProvider的Authority */
    public static final String AUTHORITY = "wistcat.overtime.TaskProvider";
    /** 基础URI */
    public static final Uri BASE_CONTENT_URI = Uri.parse("Content://" + AUTHORITY);

    private TaskContract(){}

    /** 使用账户名创建相应的Uri路径， 匹配整个Task表 */
    public static Uri buildTasksUriWith(@NonNull String account) {
        return TaskEntry.CONTENT_URI.buildUpon().appendPath(account).build();
    }

    /** 使用账户名和条目id创建相应的Uri路径，匹配单个Task条目 */
    public static Uri buildTasksUriWith(@NonNull String account, long id) {
        Uri base = TaskEntry.CONTENT_URI.buildUpon().appendPath(account).build();
        return ContentUris.withAppendedId(base, id);
    }

    /** 使用账户名和条目id创建相应的Uri路径，匹配单个Task条目 */
    public static Uri buildTasksUriWith(@NonNull String account, String id) {
        return TaskEntry.CONTENT_URI.buildUpon().appendPath(account).appendPath(id).build();
    }

    /** 使用账户名创建相应的Uri路径， 匹配整个Record表 */
    public static Uri buildRecordsUriWith(@NonNull String account) {
        return RecordEntry.CONTENT_URI.buildUpon().appendPath(account).build();
    }

    /** 使用账户名和条目id创建相应的Uri路径，匹配单个Record条目 */
    public static Uri buildRecordsUriWith(@NonNull String account, long id) {
        Uri base = RecordEntry.CONTENT_URI.buildUpon().appendPath(account).build();
        return ContentUris.withAppendedId(base, id);
    }

    /** 使用账户名和条目id创建相应的Uri路径，匹配单个Record条目 */
    public static Uri buildRecordsUriWith(@NonNull String account, String id) {
        return RecordEntry.CONTENT_URI.buildUpon().appendPath(account).appendPath(id).build();
    }

    /** 使用账户名创建相应的Uri路径， 匹配整个Episode表 */
    public static Uri buildEpisodesUriWith(@NonNull String account) {
        return EpisodeEntry.CONTENT_URI.buildUpon().appendPath(account).build();
    }

    /** 使用账户名和条目id创建相应的Uri路径，匹配单个Episode条目 */
    public static Uri buildEpisodesUriWith(@NonNull String account, long id) {
        Uri base = EpisodeEntry.CONTENT_URI.buildUpon().appendPath(account).build();
        return ContentUris.withAppendedId(base, id);
    }

    /** 使用账户名和条目id创建相应的Uri路径，匹配单个Episode条目 */
    public static Uri buildEpisodesUriWith(@NonNull String account, String id) {
        return EpisodeEntry.CONTENT_URI.buildUpon().appendPath(account).appendPath(id).build();
    }


    /**
     * Task表数据操作相关常量
     */
    public static abstract class TaskEntry implements BaseColumns {
        /** Task表名后缀：（Account名）+ TABLE_NAME */
        public static final String TABLE_NAME = "_tasktable";
        /** 资源路径 */
        public static final String PATH = "tasks";
        /** MIME类型：多个task */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.wistcat.tasks";
        /** MIME类型：单个Task */
        public static final String CONTENT_TYPE_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.wistcat.task";
        /** 资源URI */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();
        /** 任务组别 */
        public static final String COLUMN_NAME_GROUP_ID = "group_id";
        /** 任务状态 */
        public static final String COLUMN_NAME_TASK_STATE = "task_state";
        /** 任务ID */
        public static final String COLUMN_NAME_TASK_ID = "task_id";
        /** 任务名称 */
        public static final String COLUMN_NAME_TASK_NAME = "name";
        /** 任务类型 */
        public static final String COLUMN_NAME_TASK_TYPE = "type";
        /** 任务描述*/
        public static final String COLUMN_NAME_DESCREPTION = "description";
        /** 任务评价 */
        public static final String COLUMN_NAME_REMARK = "remark";
        /** 任务完成度 */
        public static final String COLUMN_NAME_COMPLETED_DEGREE = "completed_degree";
        /** 任务累计时间 */
        public static final String COLUMN_NAME_ACCUMULATED_TIME = "accumulated_time";
        /** 扩展列 */
        public static final String COLUMN_NAME_EXTRA_1 = "extra1";
        /** 扩展列 */
        public static final String COLUMN_NAME_EXTRA_2 = "extra2";
        /** 扩展列 */
        public static final String COLUMN_NAME_EXTRA_3 = "extra3";
        /** 扩展列 */
        public static final String COLUMN_NAME_EXTRA_4 = "extra4";

        public static String getTableName(String account) {
            return account + TABLE_NAME;
        }
    }

    /**
     * Record表数据操作相关常量
     */
    public static abstract class RecordEntry implements BaseColumns {
        /** Record表名后缀：（Account名）+ TABLE_NAME */
        public static final String TABLE_NAME = "_recordtable";
        /** 资源路径 */
        public static final String PATH = "records";
        /** MIME类型：多个Record */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.wistcat.records";
        /** MIME类型：单个Record */
        public static final String CONTENT_TYPE_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.wistcat.record";
        /** 资源URI */
        public static Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();
        /** 所属任务的id */
        public static final String COLUMN_NAME_TASK_ID = "task_id";
        /** 记录id */
        public static final String COLUMN_NAME_RECORD_ID = "record_id";
        /** 记录类型 */
        public static final String COLUMN_NAME_REOCRD_TYPE = "type";
        /** 记录用时 */
        public static final String COLUMN_NAME_USED_TIME = "used_time";
        /** 记录开始时间 */
        public static final String COLUMN_NAME_START_TIME = "start_time";
        /** 记录结束时间 */
        public static final String COLUMN_NAME_END_TIME = "end_time";
        /** 记录评价 */
        public static final String COLUMN_NAME_REMARK = "remark";
        /** 扩展列 */
        public static final String COLUMN_NAME_EXTRA_1 = "extra1";
        /** 扩展列 */
        public static final String COLUMN_NAME_EXTRA_2 = "extra2";
        /** 扩展列 */
        public static final String COLUMN_NAME_EXTRA_3 = "extra3";
        /** 扩展列 */
        public static final String COLUMN_NAME_EXTRA_4 = "extra4";

        public static String getTableName(String account) {
            return account + TABLE_NAME;
        }
    }

    /**
     * Episode表数据操作相关常量
     */
    public static abstract class EpisodeEntry implements BaseColumns {
        /** Episode表名后缀：（Account名）+ TABLE_NAME */
        public static final String TABLE_NAME = "_episodetable";
        /** 资源路径 */
        public static final String PATH = "episodes";
        /** MIME类型：多个Episode */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.wistcat.episodes";
        /** MIME类型：单个Episode */
        public static final String CONTENT_TYPE_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.wistcat.episode";
        /** 资源URI */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();
        /** 所属记录的id */
        public static final String COLUMN_NAME_RECORD_ID = "record_id";
        /** Episode的id，EventStart和EventEnd有相同的ID */
        public static final String COLUMN_NAME_EPISODE_ID = "episode_id";
        /** Episode名 */
        public static final String COLUMN_NAME_EPISODE_NAME = "episode_name";
        /** Episode类型 */
        public static final String COLUMN_NAME_EPISODE_TYPE = "episode_type";
        /** Episode备注 */
        public static final String COLUMN_NAME_EPISODE_REMARK = "episode_remark";
        /** Episode发生时间 */
        public static final String COLUMN_NAME_EPISODE_START_TIME = "episode_start_time";
        /** Episode序号 */
        public static final String COLUMN_NAME_EPISODE_SEQ = "episode_seq";
        /** 扩展列 */
        public static final String COLUMN_NAME_EXTRA_1 = "extra1";
        /** 扩展列 */
        public static final String COLUMN_NAME_EXTRA_2 = "extra2";
        /** 扩展列 */
        public static final String COLUMN_NAME_EXTRA_3 = "extra3";
        /** 扩展列 */
        public static final String COLUMN_NAME_EXTRA_4 = "extra4";

        public static String getTableName(String account) {
            return account + TABLE_NAME;
        }
    }

}
