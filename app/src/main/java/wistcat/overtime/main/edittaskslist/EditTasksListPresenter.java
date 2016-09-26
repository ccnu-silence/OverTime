package wistcat.overtime.main.edittaskslist;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.base.BottomFragment;
import wistcat.overtime.data.datasource.TaskRepository;
import wistcat.overtime.data.db.TaskContract;
import wistcat.overtime.data.db.TaskTableHelper;
import wistcat.overtime.interfaces.GetDataListCallback;
import wistcat.overtime.model.TaskGroup;
import wistcat.overtime.util.Const;

/**
 * @author wistcat 2016/9/12
 */
public class EditTasksListPresenter implements EditTasksListContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TASKS_LIST_QUERY = 0x05;
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_COMPLETED = 2;
    private static final int TYPE_RECYCLED = 3;

    private int mGroupType;
    private final LoaderManager mLoaderManager;
    private final TaskRepository mRepository;
    private final EditTasksListContract.View mView;
    private final TaskGroup mTaskGroup;

    private List<Integer> mDataList;
    private List<TaskGroup> mGroups;
    private int mCount;
    private boolean isShowAllSelect = true;

    private String mBottomTitle;
    private String[] mBottomItems;

    @Inject
    public EditTasksListPresenter(LoaderManager manager, TaskRepository repository,
                                  EditTasksListContract.View view, TaskGroup group) {
        mLoaderManager = manager;
        mRepository = repository;
        mView = view;
        mTaskGroup = group;
        switch (group.getId()) {
            case Const.COMPLETED_GROUP_ID:
                mGroupType = TYPE_COMPLETED;
                break;
            case Const.RECYCLED_GROUP_ID:
                mGroupType = TYPE_RECYCLED;
                break;
            default:
                mGroupType = TYPE_NORMAL;
                break;
        }
    }

    @Inject
    public void setup() {
        mView.setPresenter(this);
        mRepository.getTaskGroups(new GetDataListCallback<TaskGroup>() {
            @Override
            public void onDataLoaded(List<TaskGroup> dataList) {
                mGroups = filterTaskGroup(dataList);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void start() {
        loadList();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == TASKS_LIST_QUERY) {
            return new CursorLoader(
                    App.getInstance(),
                    TaskContract.buildTasksUriWith(getAccount()),
                    TaskTableHelper.TASK_PROJECTION,
                    TaskContract.TaskEntry.COLUMN_NAME_GROUP_ID + " = ?",
                    new String[]{String.valueOf(mTaskGroup.getId())},
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == TASKS_LIST_QUERY) {
            initDataList(data);
            mView.showList(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == TASKS_LIST_QUERY) {
            mDataList = null;
            mView.clearList();
        }
    }

    @Override
    public void loadList() {
        mLoaderManager.initLoader(TASKS_LIST_QUERY, null, this);
    }

    @Override
    public void loadBottomSheet() {
        if (TextUtils.isEmpty(mBottomTitle)) {
            mBottomTitle = "选择操作";
            switch (mGroupType) {
                case TYPE_NORMAL:
                    mBottomItems = new String[]{"变更分组", "移入收藏", "删除任务"};
                    break;
                case TYPE_COMPLETED:
                    mBottomItems = new String[]{"恢复任务", "删除任务"};
                    break;
                case TYPE_RECYCLED:
                    mBottomItems = new String[]{"恢复任务", "彻底删除"};
                    break;
                default:
                    return;
            }
        }
        mView.showBottomSheet(mBottomTitle, mBottomItems);
    }

    @Override
    public void doItemChanged(int count) {
        mCount = count;
        mView.showSelectedCount(count);
        if (count == mDataList.size() && isShowAllSelect) {
            isShowAllSelect = false;
            mView.showMenuReverseAllSelect();
        } else if (count < mDataList.size() && !isShowAllSelect) {
            isShowAllSelect =true;
            mView.showMenuAllSelect();
        }
    }

    @Override
    public void doSelectAll() { // FIXME
        if (isShowAllSelect) {
            // 全选
            isShowAllSelect = false;
            mView.showAdapterChanged(mDataList);
            mView.showMenuReverseAllSelect();
            mCount = mDataList.size();
            mView.showSelectedCount(mCount);
        } else {
            // 取消全选
            isShowAllSelect = true;
            mView.showAdapterChanged(null);
            mView.showMenuAllSelect();
            mCount = 0;
            mView.showSelectedCount(0);
        }
    }

    @Override
    public void askForHandle(int id) {
        mView.dismissDialog(BottomFragment.TAG);
        int type;
        switch (id) {
            case 0:
                type = HandleSelectedFragment.DIALOG_TYPE_MOVE;
                break;
            case 1:
                if (mGroupType == TYPE_NORMAL) {
                    type = HandleSelectedFragment.DIALOG_TYPE_RESTORE;
                } else if (mGroupType == TYPE_COMPLETED) {
                    type = HandleSelectedFragment.DIALOG_TYPE_RECYCLE;
                } else {
                    type = HandleSelectedFragment.DIALOG_TYPE_DELETE;
                }
                break;
            case 2:
                type = HandleSelectedFragment.DIALOG_TYPE_RECYCLE;
                break;
            default:
                mView.showErrorToast("unkown bottom sheet case");
                return;
        }
        mView.showHandleDialog(type);
    }

    @Override
    public void doRemove(@NonNull final TaskGroup group) {
        // FIXME
        mView.getAdapterState(new GetDataListCallback<Integer>() {
            @Override
            public void onDataLoaded(List<Integer> dataList) {
                switch (group.getId()) {
                    case Const.DELETE_GROUP_ID:
                        mRepository.deleteTasks(dataList, mTaskGroup.getId());
                        break;
                    case Const.COMPLETED_GROUP_ID:
                        mRepository.completeTasks(dataList, mTaskGroup.getId());
                        break;
                    case Const.RECYCLED_GROUP_ID:
                        mRepository.recycleTasks(dataList, mTaskGroup.getId());
                        break;
                    default:
                        if (group.getId() > 0){
                            mRepository.transformTasks(dataList, mTaskGroup, group);
                        }
                        break;
                }
                doQuit();
            }

            @Override
            public void onError() {
                mView.showErrorToast("get data error");
            }
        });
    }

    @Override
    public void doQuit() {
        mView.quit();
    }

    @Override
    public void cancelDialog() {
        mView.dismissDialog(HandleSelectedFragment.TAG);
    }

    @Override
    public List<TaskGroup> getGroups() {
        return mGroups;
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

    private List<TaskGroup> filterTaskGroup(List<TaskGroup> list) {
        List<TaskGroup> ret = new ArrayList<>();
        for (TaskGroup t : list) {
            if (t.getId() != mTaskGroup.getId()) {
                ret.add(t);
            }
        }
        return ret;
    }

    private String getAccount() {
        return App.getInstance().getAccountName();
    }

}
