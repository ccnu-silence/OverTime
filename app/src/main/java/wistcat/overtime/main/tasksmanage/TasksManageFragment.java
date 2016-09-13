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
import android.widget.TextView;

import wistcat.overtime.R;
import wistcat.overtime.adapter.TaskGroupsAdapter;
import wistcat.overtime.interfaces.ItemSelectListener;
import wistcat.overtime.main.editlist.EditListActivity;
import wistcat.overtime.main.taskslist.TasksListActivity;
import wistcat.overtime.model.TaskGroup;

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

        // listView
        TaskGroupsAdapter adapter = new TaskGroupsAdapter(getContext());
        adapter.setTaskGroupItemListener(this);
        setListAdapter(adapter);

        // more
        TextView tmore = (TextView) root.findViewById(R.id.more_title);
        tmore.setText("任务组");
        View more = root.findViewById(R.id.more_menu);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.openMoreMenu(view);
            }
        });

        return root;
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
                mPresenter.redirectToCompleted();
                return true;
            case R.id.action_recycled:
                mPresenter.redirectToRecycled();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Override
    public void setPresenter(TasksManageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onSelected(TaskGroup item) {
        mPresenter.openTaskGroup(item);
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
        dismissFragment("MoreMenu");
        Intent intent = new Intent(getActivity(), EditListActivity.class);
        // TODO maybe add flags
        startActivity(intent);
    }

    @Override
    public void showCompletedTasks(@NonNull TaskGroup group) {
        showTaskList(group);
    }

    @Override
    public void showRecycledTasks(@NonNull TaskGroup group) {
        showTaskList(group);
    }

    @Override
    public void showTaskList(@NonNull TaskGroup group) {
        Intent intent = new Intent(getActivity(), TasksListActivity.class);
        Bundle data = new Bundle();
        data.putSerializable(TasksListActivity.KEY_TASK_GROUP, group);
        intent.putExtras(data);
        startActivity(intent);
    }

    @Override
    public void showMoreMenu(View view) {
        TasksManageActivity activity = (TasksManageActivity) getActivity();
        activity.popupMoreMenu();
    }

    @Override
    public void clearCursor() {
        showTaskGroups(null);
    }

    @Override
    public void hideCreateDialog() {
        dismissFragment("AddTaskGroup");
    }

    public void dismissFragment(@NonNull String tag) {
        TasksManageActivity activity = (TasksManageActivity) getActivity();
        activity.dismissFragment(tag);
    }

}
