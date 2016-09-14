package wistcat.overtime.main.edittaskslist;

import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.List;

import wistcat.overtime.base.BasePresenter;
import wistcat.overtime.base.BaseView;
import wistcat.overtime.interfaces.GetDataListCallback;
import wistcat.overtime.model.TaskGroup;

/**
 * @author wistcat 2016/9/12
 */
public interface EditTasksListContract {

    interface View extends BaseView<Presenter> {

        /** 显示列表 */
        void showList(Cursor data);

        /** 清空列表 */
        void clearList();

        /** 显示全选按钮 */
        void showMenuAllSelect();

        /** 显示取消全选按钮 */
        void showMenuReverseAllSelect();

        /** 显示标题选中数量 */
        void showSelectedCount(int count);

        /** 全选/取消全选时，通知CursorAdapter更新数据 */
        void showAdapterChanged(List<Integer> list);

        /** 获取CursorAdapter中的选中数据，用于提交 */
        void getAdapterState(@NonNull GetDataListCallback<Integer> callback);

        /** 根据BottomSheet的列表，显示相应处理对话框 */
        void showHandleDialog(int groupType);

        /** 显示BottomSheet */
        void showBottomSheet(@NonNull String title, @NonNull String[] items);

        /** 显示错误信息 */
        void showErrorToast(String msg);

        /** 取消对话框 */
        void dismissDialog(String tag);

        /** 退出 */
        void quit();
    }

    interface Presenter extends BasePresenter {

        /** 初始化列表 */
        void loadList();

        /** 初始化TaskGroup */
        void loadTaskGroup(@NonNull TaskGroup group);

        /** 初始化BottomSheet的参数 */
        void loadBottomSheet();

        /** list选项点击回调 */
        void doItemChanged(int count);

        /** 全选/取消全选 */
        void doSelectAll();

        /** 处理选择结果 */
        void askForHandle(int type);

        /** 处理所选Task */
        void doRemove(@NonNull TaskGroup group);

        /** 退出Acitivty */
        void doQuit();

        /** 取消询问 */
        void cancelDialog();

        List<TaskGroup> getGroups();
    }
}
