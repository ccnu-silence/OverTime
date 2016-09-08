package wistcat.overtime.main.tasksmanage;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import wistcat.overtime.R;
import wistcat.overtime.adapter.TaskGroupsAdapter;
import wistcat.overtime.interfaces.ItemSelectListener;
import wistcat.overtime.main.taskslist.TasksListActivity;
import wistcat.overtime.model.TaskGroup;
import wistcat.overtime.util.Const;

/**
 * 任务管理页面
 *
 * @author wistcat 2016/9/5
 */
public class TasksManageFragment extends ListFragment implements TasksManageContract.View, ItemSelectListener<TaskGroup> {

    private TasksManageContract.Presenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tasks_manage, container, false);

        // toolbar
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.inflateMenu(R.menu.menu_taskgroup_more);
            setHasOptionsMenu(true);
            // actionBar
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(R.string.title_tasks_manage);
            }
        }
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);

        // listView
        TaskGroupsAdapter adapter = new TaskGroupsAdapter(getContext());
        adapter.setTaskGroupItemListener(this);
        setListAdapter(adapter);

        // text_more
        View more = view.findViewById(R.id.more_menu);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.openMoreMenu(view);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_taskgroup_more, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.action_completed:
                // TODO
                return true;
            case R.id.action_recycled:
                // TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
        // TODO ...
    }

    @Override
    public void setPresenter(TasksManageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onSelected(TaskGroup item) {
        mPresenter.openTaskGroup(item.getId());
    }

    @Override
    public void showTaskGroups(Cursor data) {
        CursorAdapter adapter = (CursorAdapter) getListAdapter();
        if (adapter != null) {
            adapter.swapCursor(data);
        }
    }

    @Override
    public void showCreateDialog() {
        TasksManageActivity activity = (TasksManageActivity) getActivity();
        activity.popupAddDialog();
    }

    @Override
    public void showGroupManage() {
        // TODO
    }

    @Override
    public void showCompletedTasks() {
        // TODO
    }

    @Override
    public void showRecycledTasks() {
        // TODO
    }

    @Override
    public void showTaskList(int groupId) {
        // TODO FIXME
        Intent intent = new Intent(getActivity(), TasksListActivity.class);
        intent.putExtra(Const.BUNDLE_KEY_ITEM_ID, groupId);
        startActivity(intent);
    }

    @Override
    public void showMoreMenu(View view) {
        TasksManageActivity activity = (TasksManageActivity) getActivity();
        activity.popupMoreMenu();
    }

    @Override
    public void clearCursor() {
        CursorAdapter adapter = (CursorAdapter) getListAdapter();
        if (adapter != null) {
            adapter.swapCursor(null);
        }
    }

    @Override
    public void hideCreateDialog() {
        dismissFragment("AddTaskGroup");
        // TODO ..other..
    }

    private void dismissFragment(@NonNull String tag) {
        TasksManageActivity activity = (TasksManageActivity) getActivity();
        activity.dismissFragment(tag);
    }

}
