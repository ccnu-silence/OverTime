package wistcat.overtime.main.editlist;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.data.datasource.TaskRepository;
import wistcat.overtime.data.db.SelectionBuilder;
import wistcat.overtime.data.db.TaskContract;
import wistcat.overtime.data.db.TaskTableHelper;
import wistcat.overtime.interfaces.GetDataListCallback;
import wistcat.overtime.util.Const;

/**
 * @author wistcat 2016/9/9
 */
public class EditListPresenter implements EditListContract.Presenter,
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LIST_QUERY = 0x03;

    private final EditListContract.View mView;
    private final LoaderManager mLoaderManager;
    private final TaskRepository mRepository;
    private List<Integer> mDataList;
    private int mCount;
    private boolean isShowAllSelect = true;

    @Inject
    public EditListPresenter(EditListContract.View view, LoaderManager manager, TaskRepository repository) {
        mView = view;
        mLoaderManager = manager;
        mRepository = repository;
    }

    @Inject
    public void setup() {
        mView.setPresenter(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == LIST_QUERY) {
            SelectionBuilder builder = new SelectionBuilder();
            builder.notIn(TaskContract.TaskGroupEntry._ID, null)
                    .notIn(null, String.valueOf(Const.DEFAULT_GROUP_ID))
                    .notIn(null, String.valueOf(Const.COMPLETED_GROUP_ID))
                    .notIn(null, String.valueOf(Const.RECYCLED_GROUP_ID));
            return new CursorLoader(
                    App.getInstance(),
                    TaskContract.buildTaskGroupUri(getAccount()),
                    TaskTableHelper.TASK_GROUP_PROJECTION,
                    builder.getSelection(),
                    builder.getSelectionArgs(),
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == LIST_QUERY) {
            initDataList(data);
            mView.showList(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == LIST_QUERY) {
            mDataList = null;
            mView.showList(null);
        }
    }

    @Override
    public void loadList() {
        mLoaderManager.initLoader(LIST_QUERY, null, this);
    }

    @Override
    public void doItemChanged(int count) {
        mCount = count;
        mView.showSelectedCount(count);
        if (count == mDataList.size() && isShowAllSelect) {
            isShowAllSelect = false;
            mView.showMenuReverseAllSelected();
        } else if (count < mDataList.size() && !isShowAllSelect){
            isShowAllSelect = true;
            mView.showMenuAllSelected();
        }
    }

    @Override
    public void selectAll() {
        if (isShowAllSelect) {
            // 全选
            isShowAllSelect = false;
            mView.showAdapterChanged(mDataList);
            mView.showMenuReverseAllSelected();
            mCount = mDataList.size();
            mView.showSelectedCount(mCount);
        } else {
            // 取消全选
            isShowAllSelect = true;
            mView.showAdapterChanged(null);
            mView.showMenuAllSelected();
            mCount = 0;
            mView.showSelectedCount(0);
        }
    }

    @Override
    public void doCancel() {
        mView.getAdapterState(new GetDataListCallback<Integer>() {

            @Override
            public void onDataLoaded(List<Integer> dataList) {
                mRepository.deleteTaskGroups(dataList);
                mView.quit();
            }

            @Override
            public void onError() {
                // TODO
                Log.e("TAG", "Unkown Error");
                mView.dismissAlertDialog();
            }
        });
    }

    @Override
    public void doQuit() {
        mView.quit();
    }

    @Override
    public void cancelDialog() {
        mView.dismissAlertDialog();
    }

    @Override
    public void askForCancel() {
        if (mCount == 0) {
            mView.showErrorToast("请选择要删除的任务组");
        } else {
            mView.showAlertDialog();
        }
    }

    @Override
    public void start() {
        loadList();
    }

    private void initDataList(Cursor cursor) {
        List<Integer> list = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getInt(0));
                } while (cursor.moveToNext());
            }
            cursor.moveToFirst();
        }
        mDataList = list;
    }

    private String getAccount() {
        return App.getInstance().getAccountName();
    }

}
