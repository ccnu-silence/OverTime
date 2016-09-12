package wistcat.overtime.main.editlist;

import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.List;

import wistcat.overtime.base.BasePresenter;
import wistcat.overtime.base.BaseView;
import wistcat.overtime.interfaces.GetDataListCallback;

/**
 * @author wistcat 2016/9/9
 */
public interface EditListContract {

    interface View extends BaseView<Presenter> {

        /** 显示列表 */
        void showList(Cursor cursor);

        /** 全选 */
        void showMenuAllSelected();

        /** 取消全选 */
        void showMenuReverseAllSelected();

        /** 标题显示选中总数 */
        void showSelectedCount(int count);

        void showAdapterChanged(List<Integer> list);

        void getAdapterState(@NonNull GetDataListCallback<Integer> callback);

        void showAlertDialog();

        void showErrorToast(String str);

        void dismissAlertDialog();

        void quit();
    }

    interface Presenter extends BasePresenter {

        /** 初始化列表 */
        void loadList();

        /** list选项点击时 */
        void doItemChanged(int count);

        /** 全选/取消全选 */
        void selectAll();

        void askForCancel();

        /** 删除选项，并退出Activity */
        void doCancel();

        /** 退出Acitivty */
        void doQuit();

        /** 取消询问 */
        void cancelDialog();
    }

}
