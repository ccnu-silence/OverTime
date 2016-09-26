package wistcat.overtime.main.taskdetail;

import android.database.Cursor;

import wistcat.overtime.base.BasePresenter;
import wistcat.overtime.base.BaseView;

/**
 * @author wistcat 2016/9/22
 */
public interface TaskDetailsContract {

    interface View extends BaseView<Presenter> {

        void showList(Cursor data);

        void clearList();
    }

    interface Presenter extends BasePresenter {

        void loadList();
    }
}
