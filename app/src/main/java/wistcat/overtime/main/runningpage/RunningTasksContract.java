package wistcat.overtime.main.runningpage;

import android.database.Cursor;
import android.support.annotation.NonNull;

import wistcat.overtime.base.BasePresenter;
import wistcat.overtime.base.BaseView;
import wistcat.overtime.model.Record;

/**
 * @author wistcat 2016/9/25
 */
public interface RunningTasksContract {

    interface View extends BaseView<Presenter> {

        void showList(Cursor data);

        void clearList();

        void redirectRunningRecord(@NonNull Record record);
    }

    interface Presenter extends BasePresenter {

        void loadList();

        void onItemSelected(@NonNull Record record);

        void onRecordPaused(@NonNull Record record);
    }
}
