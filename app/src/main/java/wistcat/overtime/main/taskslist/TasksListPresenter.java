package wistcat.overtime.main.taskslist;

import android.database.Cursor;
import android.net.Uri;
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
import wistcat.overtime.data.datasource.TaskRepository;
import wistcat.overtime.data.db.SelectionBuilder;
import wistcat.overtime.data.db.TaskContract;
import wistcat.overtime.data.db.TaskTableHelper;
import wistcat.overtime.interfaces.GetDataListCallback;
import wistcat.overtime.interfaces.ResultCallback;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;

/**
 * @author wistcat 2016/9/12
 */
public class TasksListPresenter implements TasksListContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {

    private final int TASKS_QUERY = 0x04;
    private final String SEARCH = "search";
    private final TasksListContract.View mView;
    private final LoaderManager mLoaderManager;
    private final TaskRepository mRepository;
    private final TaskGroup mGroup;
    private boolean isFirst = true;
    private Task mSelectedTask;

    @Inject
    public TasksListPresenter(TasksListContract.View view, LoaderManager manager, TaskRepository repository, TaskGroup group) {
        mRepository = repository;
        mView = view;
        mLoaderManager = manager;
        mGroup = group;
    }

    @Inject
    public void setup() {
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if (isFirst) {
            isFirst = false;
            loadTasks(null);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == TASKS_QUERY) {
            String str = args.getString(SEARCH);
            return createLoader(str);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == TASKS_QUERY) {
            mView.showList(data);
            if (data == null || data.getCount() == 0) {
                mView.showNoText(true);
            } else {
                mView.showNoText(false);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == TASKS_QUERY) {
            mView.clearList();
        }
    }

    @Override
    public void loadTasks(String term) {
        Bundle data = new Bundle();
        data.putString(SEARCH, term);
        if (isFirst) {
            mLoaderManager.initLoader(TASKS_QUERY, data, this);
        } else {
            mLoaderManager.restartLoader(TASKS_QUERY, data, this);
        }
    }

    @Override
    public void onItemSelected(@NonNull Task task) {
        mView.redirectTaskDetails(task);
    }

    @Override
    public void onItemEditSelected(@NonNull Task task) {
        // 重要，记录选择的Task，配合之后的几个Dialog使用
        mSelectedTask = task;
        mView.showItemMenu(task);
    }

    @Override
    public void openMoreMenu() {
        mView.showMoreMenu();
    }

    @Override
    public void openTaskGroupDetails() {
        mView.dismissMoreMenu();
        mView.redirectTaskGroupDetails();
    }

    @Override
    public void openEditTasksList() {
        mView.dismissMoreMenu();
        mView.redirectEditTasksList();
    }

    @Override
    public void openMoveDialog() {
        mView.dismissItemMenu();
        mRepository.getTaskGroups(new GetDataListCallback<TaskGroup>() {
            @Override
            public void onDataLoaded(List<TaskGroup> dataList) {
                List<TaskGroup> temp = filterGroup(dataList);
                if (temp == null || temp.size() == 0) {
                    mView.showToast("没有可选的分组");
                    return;
                }
                mView.showMoveDialog(temp);
            }

            @Override
            public void onError() {
                mView.showToast("加载失败！");
            }
        });
    }

    @Override
    public void closeMoveDialog() {
        mView.dismissMoveDialog();
    }

    @Override
    public void openDeleteDialog() {
        mView.dismissItemMenu();
        mView.showDeleteDialog();
    }

    @Override
    public void closeDeleteDialog() {
        mView.dismissDeleteDialog();
    }

    @Override
    public void openSaveDialog() {
        mView.dismissItemMenu();
        mView.showSaveDialog();
    }

    @Override
    public void closeSaveDialog() {
        mView.dismissSaveDialog();
    }

    @Override
    public void createNewTask() {
        mView.dismissMoreMenu();
        mView.redirectCreateTask();
    }

    @Override
    public void doMove(@NonNull TaskGroup group) {
        mView.dismissMoveDialog();
        List<Integer> data = new ArrayList<>(1);
        data.add(mSelectedTask.getId());
        mRepository.transformTasks(data, mGroup, group, new ResultCallback() {
            @Override
            public void onSuccess() {
                mView.showToast("移动成功");
            }

            @Override
            public void onError() {
                mView.showToast("移动失败");
            }
        });
    }

    @Override
    public void doSave() {
        mView.dismissSaveDialog();
        mRepository.saveTask(mSelectedTask, new ResultCallback() {
            @Override
            public void onSuccess() {
                mView.showToast("保存成功");
            }

            @Override
            public void onError() {
                mView.showToast("保存失败");
            }
        });
    }

    @Override
    public void doDelete() {
        mView.dismissDeleteDialog();
        mRepository.recycleTask(mSelectedTask, new ResultCallback() {
            @Override
            public void onSuccess() {
                mView.showToast("删除成功");
            }

            @Override
            public void onError() {
                mView.showToast("删除失败");
            }
        });
    }

    private CursorLoader createLoader(String str) {
        Uri uri = TaskContract.buildTasksUriWith(getAccount());
        SelectionBuilder builder = new SelectionBuilder();
        builder.whereAnd(
                TaskContract.TaskEntry.COLUMN_NAME_GROUP_ID + " = ?",
                String.valueOf(mGroup.getId())
        );
        if (!TextUtils.isEmpty(str)) {
            builder.whereAnd(
                    TaskContract.TaskEntry.COLUMN_NAME_TASK_NAME + " LIKE ?",
                    TaskTableHelper.createSearchSelection(str)
            );
        }
        return new CursorLoader(
                App.getInstance(),
                uri,
                TaskTableHelper.TASK_PROJECTION,
                builder.getSelection(),
                builder.getSelectionArgs(),
                null
        );
    }

    private String getAccount() {
        return App.getInstance().getAccountName();
    }

    private List<TaskGroup> filterGroup(List<TaskGroup> groups) {
        if (groups  == null) {
            return null;
        }
        List<TaskGroup> ret = new ArrayList<>(groups.size());
        for (TaskGroup g : groups) {
            if (g.getId() != mGroup.getId()) {
                ret.add(g);
            }
        }
        return ret;
    }

}
