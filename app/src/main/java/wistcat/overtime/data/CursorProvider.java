package wistcat.overtime.data;

import android.support.v4.content.CursorLoader;

import wistcat.overtime.App;
import wistcat.overtime.data.db.TaskContract;
import wistcat.overtime.data.db.TaskTableHelper;

/**
 * @author wistcat 2016/9/27
 */
public class CursorProvider {

    private CursorProvider() {}

    public static CursorLoader queryRunningRecordsCount() {
        return new CursorLoader(
                App.getInstance(),
                TaskContract.buildRecordsUriWith(getAccount()),
                new String[]{TaskContract.RecordEntry._ID},
                TaskTableHelper.WHERE_RECORD_RUN,
                null,
                null
        );
    }

    public static CursorLoader queryRunningRecords() {
        return new CursorLoader(
                App.getInstance(),
                TaskContract.buildRecordsUriWith(getAccount()),
                TaskTableHelper.RECORD_PROJECTION,
                TaskTableHelper.WHERE_RECORD_RUN,
                null,
                null
        );
    }

    public static CursorLoader queryEpisodeList(int recordId) {
        return new CursorLoader(
                App.getInstance(),
                TaskContract.buildEpisodesUriWith(getAccount()),
                TaskTableHelper.EPISODE_PROJECTION,
                TaskTableHelper.WHERE_EPISODES_RECORD_ID,
                new String[]{String.valueOf(recordId)},
                null
        );
    }

    public static String getAccount() {
        return App.getInstance().getAccountName();
    }
}
