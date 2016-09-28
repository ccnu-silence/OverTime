package wistcat.overtime.main.runningpage;

import android.database.Cursor;
import android.support.annotation.NonNull;

import wistcat.overtime.base.BasePresenter;
import wistcat.overtime.base.BaseView;
import wistcat.overtime.model.Record;
import wistcat.overtime.model.Task;

/**
 * @author wistcat 2016/9/25
 */
public interface RunningTasksContract {

    interface View extends BaseView<Presenter> {

        void showList(Cursor data);

        void clearList();

        void setLoadingIndicator(boolean active);

        void loadRecordDetails(@NonNull Record record);

        void loadRecordEmpty();

        void showAddEpisodeDialog(String addTime);

        void dismissAddEpisodeDialog();

        void setStartTimeImage(boolean isEmpty);
    }

    interface RunningView {

        void showList(Cursor data);

        void clearList();

        void showDrawer();

        void hideDrawer();
    }

    interface Presenter extends BasePresenter {

        void initState(Task task);

        // view
        void loadList();

        void onItemSelected(@NonNull Record record);

        void onRecordPaused(@NonNull Record record);

        void openDrawer();

        void closeDrawer();

        void stopAll();

        // runningView

        void openAddDialog();

        void doAddEpisode(String remark);

        void doNotAdd();

        void doPause();

        void loadRecordByTask();

        void loadRecord(Record record);
    }
}
