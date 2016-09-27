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

    public static CursorLoader queryRunningRecords() {
        return new CursorLoader(
                App.getInstance(),
                TaskContract.buildRecordsUriWith(getAccount()),
                new String[]{TaskContract.RecordEntry._ID},
                TaskTableHelper.WHERE_RECORD_RUN,
                null,
                null
        );
    }

    public static String getAccount() {
        return App.getInstance().getAccountName();
    }
}
